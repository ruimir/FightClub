package FightClub;

import FightClub.Extra.ActionMessage;
import FightClub.Extra.Bot;
import FightClub.Extra.HeritageMessage;
import FightClub.Extra.InformationMessage;
import robocode.Droid;
import robocode.MessageEvent;
import robocode.TeamRobot;

public class FightDroid extends TeamRobot implements Droid {

    String leaderID; //current leader

    public void run() {


    }

    public void onMessageReceived(MessageEvent event) {
        out.println(event.getSender() + " sent me: " + event.getMessage());
        Object message = event.getMessage();
        String messageType = message.getClass().getCanonicalName();
        //react depending on message type
        switch (messageType) {
            case ("FightClub.Extra.InformationMessage"): {
            }
            case ("FightClub.Extra.HeritageMessage"): {
                HeritageMessage hs = (HeritageMessage) message;
                this.leaderID = hs.getNewLeader();
            }
            case ("FightClub.Extra.ActionMessage"): {

            }

        }

    }
}
