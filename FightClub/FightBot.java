package FightClub;

import robocode.DeathEvent;
import robocode.HitByBulletEvent;
import robocode.MessageEvent;
import robocode.TeamRobot;

import java.awt.*;
import java.io.IOException;

public class FightBot extends TeamRobot {

    int role; // 0 if Leader, 1 if Melee

    public void run() {
        String[] a= getTeammates();
        // Prepare RobotColors object
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


    }

    public void onDeath(DeathEvent event) {
        out.println("DEAD.");
        try {
            broadcastMessage(this.getName() + "is dead.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onHitByBullet(HitByBulletEvent event) {
        double energy = this.getEnergy();
        out.println(energy);
        if (energy <= 20) {
            try {
                broadcastMessage(this.getName() + "has low health.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void onMessageReceived(MessageEvent event) {
        out.println(event.getSender() + " sent me: " + event.getMessage());
    }


}
