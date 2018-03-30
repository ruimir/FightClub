package FightClub.Extra;

public class HeritageMessage implements java.io.Serializable {

    private String newLeader;

    public String getNewLeader() {
        return newLeader;
    }

    public void setNewLeader(String newLeader) {
        this.newLeader = newLeader;
    }

    public HeritageMessage(String newLeader) {
        this.newLeader = newLeader;
    }
}
