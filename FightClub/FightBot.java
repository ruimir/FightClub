package FightClub;

import FightClub.Extra.*;
import robocode.*;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.Color;
import java.awt.Graphics2D;


/*
TODO:
Emotion Engine
Movement
Melee/Leader Behaviour
Droid Behaviour
 */

public class FightBot extends TeamRobot {

    private int role; // 0 if Leader, 1 if Melee
    private String leaderID; //current leader
    private boolean critical; // If health is low
    private Map<String, Bot> enemies;
    private Map<String, Bot> friends;
    private EnemyDodgingMovement edm;

    //VARS for Leader:
    double O, C, E, A, N;
    double pleasure, arousal, dominance;
    int emotion; //Carly Rae Jepsen's Iconic Album!
    //Joy=1
    //Pride=2
    //Fear=3
    //Anger=4


    public void run() {
        critical = false;
        edm = new EnemyDodgingMovement(this);
        enemies = new HashMap<>();
        friends = new HashMap<>();
        String intValue = this.getName().replaceAll("[^0-9]", ""); // Extract integer from name
        if (intValue.equals("")) {
            out.println("Only one TeamRobot!");
            role = 1;
        } else {
            int robotnum = Integer.parseInt(intValue);
            if (robotnum == 1) {
                out.println("I'm a leader!");
                role = 0; // FightBot1, leader!
                leaderID = this.getName();
                try {
                    broadcastMessage(new HeritageMessage(this.getName()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //All that we could do with this emotion...
                prepareEmotion();

            } else {
                out.println("I'm a Melee!");
                role = 1; // The rest are melee!
            }
        }

        while (true) {
            ArrayList<Point2D.Double> enemyPoints = new ArrayList<>();
            Iterator<Bot> it = enemies.values().iterator();
            Point2D.Double point;
            switch (role) {
                case 0:

                    while (it.hasNext()) {
                        Bot bot = it.next();
                        enemyPoints.add(new Point2D.Double(bot.getX(), bot.getY()));
                    }
                    out.println(enemyPoints.size());
                    point = edm.getDestination(enemyPoints);
                    if (point != null) {
                        gotoXY(point.x, point.y);
                    } else {
                        turnRight(45);
                    }
                case 1: {

                    while (it.hasNext()) {
                        Bot bot = it.next();
                        enemyPoints.add(new Point2D.Double(bot.getX(), bot.getY()));
                    }
                    point = edm.getDestination(enemyPoints);
                    if (point != null) {
                        gotoXY(point.x, point.y);
                    } else {
                        turnRight(45);
                    }
                }
            }
        }


    }

    public void onDeath(DeathEvent event) {
        out.println("DEAD.");
        //Implement feedback features here
    }

    public void onHitByBullet(HitByBulletEvent event) {
        double energy = this.getEnergy();
        out.println(energy);
        if (energy <= 20 && !critical) {
            critical = true;
            try {
                broadcastMessage(this.getName() + "has low health.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onScannedRobot(ScannedRobotEvent event) {
        //First step: check if robot scanned is friend or foe:
        String target = event.getName();
        String[] teammates = this.getTeammates();
        boolean isTeammate = false;
        for (String teammate : teammates) {
            if (target.equals(teammate)) {
                isTeammate = true;
                break;
            }
        }
        // Assuming radar and gun are aligned...
        if (!isTeammate) {
            //ENEMY CODE
            double angleToEnemy = event.getBearing();

            // Calculate the angle to the scanned robot
            double angle = Math.toRadians((this.getHeading() + angleToEnemy % 360));

            // Calculate the coordinates of the robot
            double enemyX = (this.getX() + Math.sin(angle) * event.getDistance());
            double enemyY = (this.getY() + Math.cos(angle) * event.getDistance());

            enemies.put(event.getName(), new Bot(event.getEnergy(), enemyX, enemyY));
            //sending enemy information
            InformationMessage infoMessage = new InformationMessage(this.getEnergy(), event.getEnergy(), event.getName(), this.getX(), this.getY(), enemyX, enemyY, true);
            //TODO:BETA VERSION, EMOTION ENGINE NEEDS TO BE IMPLEMENTED:
            ActionMessage actionMessage = new ActionMessage(enemyX, enemyY);
            try {
                broadcastMessage(infoMessage);
                broadcastMessage(actionMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (event.getDistance() < 100) {
                fire(3);
            } else {
                fire(1);
            }
        } else {
            //FRIEND CODE
            double angleToEnemy = event.getBearing();

            // Calculate the angle to the scanned robot
            double angle = Math.toRadians((this.getHeading() + angleToEnemy % 360));

            // Calculate the coordinates of the robot
            double friendX = (this.getX() + Math.sin(angle) * event.getDistance());
            double friendY = (this.getY() + Math.cos(angle) * event.getDistance());

            friends.put(event.getName(), new Bot(event.getEnergy(), friendX, friendY));
            InformationMessage infoMessage = new InformationMessage(this.getEnergy(), event.getEnergy(), event.getName(), this.getX(), this.getY(), friendX, friendY, false);
            try {
                broadcastMessage(infoMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void prepareEmotion() {
        O = ThreadLocalRandom.current().nextDouble(-1, 1);
        C = ThreadLocalRandom.current().nextDouble(-1, 1);
        E = ThreadLocalRandom.current().nextDouble(-1, 1);
        A = ThreadLocalRandom.current().nextDouble(-1, 1);
        N = ThreadLocalRandom.current().nextDouble(-1, 1);
        pleasure = 0.59 * A + 0.19 * N + 0.21 * E;
        arousal = 0.57 * N + 0.30 * A + 0.15 * O;
        dominance = 0.60 * N + 0.32 * A + 0.25 * O;
        //Define emotion from
        if (pleasure < -0.4) {
            if (dominance < 0.2) {
                emotion = 1;
            } else emotion = 2;
        } else if (pleasure >= -0.4) {
            if (dominance > 0) {
                emotion = 3;
            } else emotion = 4;
        }
    }

    public void onMessageReceived(MessageEvent event) {
        out.println(event.getSender() + " sent me: " + event.getMessage());
        Object message = event.getMessage();
        String messageType = message.getClass().getCanonicalName();
        //react depending on message type
        if (messageType.equals("FightClub.Extra.InformationMessage")) {
            InformationMessage im = (InformationMessage) message;
            if (im.isEnemy()) {
                //Scanned bot is a enemy:
                enemies.put(im.getTargetName(), new Bot(im.getTargetEnergy(), im.getTargetX(), im.getTargetY()));
            } else {
                //Scanned bot is a friend
                friends.put(im.getTargetName(), new Bot(im.getTargetEnergy(), im.getTargetX(), im.getTargetY()));
            }
            //Updating info about sender:
            friends.put(event.getSender(), new Bot(im.getCurrentEnergy(), im.getX(), im.getY()));

        } else if (messageType.equals("FightClub.Extra.HeritageMessage")) {
            HeritageMessage hs = (HeritageMessage) message;
            this.leaderID = hs.getNewLeader();
            if (hs.getNewLeader().equals(this.getName())) {
                role = 1;
                out.println("I'm the boss now!");
            }
        } else if (messageType.equals("FightClub.Extra.ActionMessage")) {
        }


    }


    // Go to GPS position (x,y)
    private void gotoXY(double x, double y) {
        double dx = x - getX();
        double dy = y - getY();
        double turnDegrees;

        // Determine how much to turn
        turnDegrees = (Math.toDegrees(Math.atan2(dx, dy)) - getHeading()) % 360;
        turnRight(turnDegrees);
        ahead(Math.sqrt(dx * dx + dy * dy));
    } // end gotoXY()


    public void onPaint(Graphics2D g) {
        ArrayList<Point2D.Double> enemyPoints = new ArrayList<>();
        Iterator<Bot> it = enemies.values().iterator();
        while (it.hasNext()) {
            Bot bot = it.next();
            enemyPoints.add(new Point2D.Double(bot.getX(), bot.getY()));
        }
        out.println(enemyPoints.size());
        edm.paint(g,enemyPoints);


    }


}
