package FightClub;

import FightClub.Extra.Bot;
import FightClub.Extra.InformationMessage;
import robocode.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FightBot extends TeamRobot {

    int role; // 0 if Leader, 1 if Melee
    boolean critical; // If health is low
    Map<String, Bot> enemies;
    Map<String, Bot> friends;


    public void run() {
        critical = false;
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
            } else {
                out.println("I'm a Melee!");
                role = 1; // The rest are melee!
            }
        }

        while (true) {
            switch (role) {
                case 0:
                    scan();

                case 1: {
                    scan();
                }
            }
        }


    }

    public void onDeath(DeathEvent event) {
        out.println("DEAD.");
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

            enemies.put(event.getName(), new Bot(event.getEnergy(), 0, 0)); //TODO:Add target coordinate code
            //sending enemy information
            InformationMessage infoMessage = new InformationMessage(this.getEnergy(), event.getEnergy(), event.getName(), this.getX(), this.getY(), 0, 0); // TODO:Add target coordinate code
            try {
                broadcastMessage(infoMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (event.getDistance() < 100) {
                fire(3);
            } else {
                fire(1);
            }
        } else {
            out.println("Not Killing a Teammate!");
            friends.put(event.getName(), new Bot(event.getEnergy(), 0, 0)); //TODO:Add target coordinate code
            InformationMessage infoMessage = new InformationMessage(this.getEnergy(), this.getX(), this.getY());
            try {
                broadcastMessage(infoMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void onMessageReceived(MessageEvent event) {
        out.println(event.getSender() + " sent me: " + event.getMessage());
        Object message = event.getMessage();
        String messageType = message.getClass().getCanonicalName();
        switch (messageType) {
            case ("FightClub.Extra.InformationMessage"): {
            }
            case ("FightClub.Extra.HeritageMessage"): {
            }
            case ("FightClub.Extra.ActionMessage"): {
            }

        }

    }


}
