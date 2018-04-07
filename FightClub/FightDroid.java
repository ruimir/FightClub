package FightClub;

import FightClub.Extra.*;
import robocode.*;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static robocode.util.Utils.normalRelativeAngleDegrees;

public class FightDroid extends TeamRobot implements Droid {

    private String leaderID; //current leader
    private Map<String, Bot> enemies;
    private EnemyDodgingMovement edm;
    boolean movingForward, enemyAlive;


    /**
     * onHitWall:  Handle collision with wall.
     */
    public void onHitWall(HitWallEvent e) {
        // Bounce off!
        reverseDirection();
    }

    /**
     * reverseDirection:  Switch from ahead to back & vice versa
     */
    public void reverseDirection() {
        if (movingForward) {
            setBack(40000);
            movingForward = false;
        } else {
            setAhead(40000);
            movingForward = true;
        }
    }


    /**
     * onHitRobot:  Back up!
     */
    public void onHitRobot(HitRobotEvent e) {
        // If we're moving the other robot, reverse!
        if (e.isMyFault()) {
            reverseDirection();
        }
    }


    public void run() {
        out.println("Annyong.");
        enemies = new HashMap<>();
        edm = new EnemyDodgingMovement(this);
        while (true) {

            ArrayList<Point2D.Double> enemyPoints = new ArrayList<>();
            Iterator<Bot> it = enemies.values().iterator();
            Point2D.Double point;

            while (it.hasNext()) {
                Bot bot = it.next();
                enemyPoints.add(new Point2D.Double(bot.getX(), bot.getY()));
            }
            point = edm.getDestination(enemyPoints);
            if (point != null) {
                gotoXY(point.x, point.y);
            } else {
                setAhead(200);
                setTurnRight(90);
                waitFor(new TurnCompleteCondition(this));
            }


        }
    }

    public void onMessageReceived(MessageEvent event) {
        //out.println(event.getSender() + " sent me: " + event.getMessage());
        Object message = event.getMessage();
        String messageType = message.getClass().getCanonicalName();
        //react depending on message type
        if (messageType.equals("FightClub.Extra.InformationMessage")) {
            InformationMessage im = (InformationMessage) message;
            if (im.isEnemy()) {
                //Scanned bot is a enemy:
                enemies.put(im.getTargetName(), new Bot(im.getTargetEnergy(), im.getTargetX(), im.getTargetY()));
            }
        } else if (messageType.equals("FightClub.Extra.HeritageMessage")) {
            HeritageMessage hs = (HeritageMessage) message;
            this.leaderID = hs.getNewLeader();
        } else if (messageType.equals("FightClub.Extra.ActionMessage")) {
            ActionMessage am = (ActionMessage) message;
            switch (am.getAttackMode()) {
                case 0: {
                    pointAndShoot(am.getEnemyX(), am.getEnemyY());
                }
                case 1: {
                    if (this.getEnergy() >= 150) {
                        kamikazeMode(am.getEnemyX(), am.getEnemyY());
                    } else pointAndShoot(am.getEnemyX(), am.getEnemyY());

                }
                case 2: {
                    crazyMode(am.getEnemyX(), am.getEnemyY());
                }
                default: {
                    pointAndShoot(am.getEnemyX(), am.getEnemyY());
                }
            }


        }


    }

    private void pointAndShoot(double X, double Y) {
        double dx = X - this.getX();
        double dy = Y - this.getY();
        // Calculate angle to target
        double theta = Math.toDegrees(Math.atan2(dx, dy));

        // Turn gun to target
        turnGunRight(normalRelativeAngleDegrees(theta - getGunHeading()));
        // Fire hard!
        fire(3);
    }


    private void crazyMode(double X, double Y) {
        // Tell the game we will want to move ahead 40000 -- some large number
        setAhead(40000);
        movingForward = true;
        // Tell the game we will want to turn right 90
        setTurnRight(90);
        // At this point, we have indicated to the game that *when we do something*,
        // we will want to move ahead and turn right.  That's what "set" means.
        // It is important to realize we have not done anything yet!
        // In order to actually move, we'll want to call a method that
        // takes real time, such as waitFor.
        // waitFor actually starts the action -- we start moving and turning.
        // It will not return until we have finished turning.
        waitFor(new TurnCompleteCondition(this));
        for (int i = 0; i < 5; i++) {
            pointAndShoot(X, Y);

        }
        // Note:  We are still moving ahead now, but the turn is complete.
        // Now we'll turn the other way...
        setTurnLeft(180);
        // ... and wait for the turn to finish ...
        waitFor(new TurnCompleteCondition(this));
        for (int i = 0; i < 5; i++) {
            pointAndShoot(X, Y);

        }
        // ... then the other way ...
        setTurnRight(180);
        // .. and wait for that turn to finish.
        waitFor(new TurnCompleteCondition(this));
        for (int i = 0; i < 5; i++) {
            pointAndShoot(X, Y);

        }
        // then back to the top to do it all again*/

    }


    private void kamikazeMode(double X, double Y) {
        out.println("Kamikaze Mode ONLINE");
        gotoXY(X, Y);
        double dx = X - this.getX();
        double dy = Y - this.getY();
        // Calculate angle to target
        double theta = Math.toDegrees(Math.atan2(dx, dy));

        // Turn gun to target
        turnGunRight(normalRelativeAngleDegrees(theta - getGunHeading()));
        // Fire hard!
        enemyAlive = true;
        for (int i = 0; i < 10; i++) {
            fire(3);
        }
        out.println("I hope I made my country proud!");


    }

    public void onPaint(Graphics2D g) {
        ArrayList<Point2D.Double> enemyPoints = new ArrayList<>();
        Iterator<Bot> it = enemies.values().iterator();
        while (it.hasNext()) {
            Bot bot = it.next();
            enemyPoints.add(new Point2D.Double(bot.getX(), bot.getY()));
        }
        edm.paint(g, enemyPoints);


    }

    public void onBulletHit(BulletHitEvent event) {
        if (this.isTeammate(event.getName()) || event.getEnergy() < 20) {
            enemyAlive = false;
        }
        setAhead(40000);
        movingForward = true;
        // Tell the game we will want to turn right 90
        setTurnRight(90);
        ArrayList<Point2D.Double> enemyPoints = new ArrayList<>();
        Iterator<Bot> it = enemies.values().iterator();
        Point2D.Double point;

        while (it.hasNext()) {
            Bot bot = it.next();
            enemyPoints.add(new Point2D.Double(bot.getX(), bot.getY()));
        }
        point = edm.getDestination(enemyPoints);
        if (point != null) {
            gotoXY(point.x, point.y);
        } else {
            setAhead(200);
            setTurnRight(90);
            waitFor(new TurnCompleteCondition(this));
        }


    }


    private void gotoXY(double x, double y) {
        double dx = x - getX();
        double dy = y - getY();
        double turnDegrees;

        // Determine how much to turn
        turnDegrees = (Math.toDegrees(Math.atan2(dx, dy)) - getHeading()) % 360;
        turnRight(turnDegrees);
        ahead(Math.sqrt(dx * dx + dy * dy));
    } // end gotoXY()


}
