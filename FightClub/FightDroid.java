package FightClub;

import robocode.Droid;
import robocode.MessageEvent;
import robocode.TeamRobot;

public class FightDroid extends TeamRobot implements Droid {

    public void run() {


    }

    public void onMessageReceived(MessageEvent event) {
        out.println(event.getSender() + " sent me: " + event.getMessage());
    }

}
