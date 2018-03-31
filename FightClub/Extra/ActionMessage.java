package FightClub.Extra;

public class ActionMessage implements java.io.Serializable {
    private double enemyX, enemyY;

    public ActionMessage(double enemyX, double enemyY) {
        this.enemyX = enemyX;
        this.enemyY = enemyY;
    }

    public double getEnemyX() {
        return enemyX;
    }

    public double getEnemyY() {
        return enemyY;
    }
}
