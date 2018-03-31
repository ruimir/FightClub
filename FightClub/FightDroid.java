package FightClub;

import FightClub.Extra.ActionMessage;
import FightClub.Extra.Bot;
import FightClub.Extra.HeritageMessage;
import FightClub.Extra.InformationMessage;
import robocode.Droid;
import robocode.MessageEvent;
import robocode.TeamRobot;

import static robocode.util.Utils.normalRelativeAngleDegrees;

public class FightDroid extends TeamRobot implements Droid {

    private String leaderID; //current leader

    public void run() {
        out.println("Annyong.");
        while (true) {
            this.ahead(100.0D);
            this.back(100.0D);
        }
    }

    public void onMessageReceived(MessageEvent event) {
        out.println(event.getSender() + " sent me: " + event.getMessage());
        Object message = event.getMessage();
        String messageType = message.getClass().getCanonicalName();
        //react depending on message type
        if (messageType.equals("FightClub.Extra.InformationMessage")) {
            InformationMessage im = (InformationMessage) message;
        } else if (messageType.equals("FightClub.Extra.HeritageMessage")) {
            HeritageMessage hs = (HeritageMessage) message;
            this.leaderID = hs.getNewLeader();
        } else if (messageType.equals("FightClub.Extra.ActionMessage")) {
            ActionMessage am = (ActionMessage) message;
            double dx = am.getEnemyX() - this.getX();
            double dy = am.getEnemyY() - this.getY();
            // Calculate angle to target
            double theta = Math.toDegrees(Math.atan2(dx, dy));

            // Turn gun to target
            turnGunRight(normalRelativeAngleDegrees(theta - getGunHeading()));
            // Fire hard!
            fire(3);

        }


    }


}
