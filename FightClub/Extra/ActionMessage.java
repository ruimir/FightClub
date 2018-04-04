package FightClub.Extra;

public class ActionMessage implements java.io.Serializable {
    private double enemyX, enemyY, targetBearing, distance;
    private int attackMode; //How should the droid attack someone?
    /*
    Ideas for attack modes:
    0=Point gun and shoot
    1="Kamikaze" Mode -> Go to target and continuously fire gun
    ...before you ask "is that a bit super risky? I mean, THAT'S DROID SUICIDE"
    THAT'S EXACTLY WHY IT'S CALLED KAMIKAZE MODE
    2=Confusion Mode -> Walk around like ->crazy<-, and try to shoot target
    */


    public ActionMessage(double enemyX, double enemyY, double targetBearing, double distance, int attackMode) {
        this.enemyX = enemyX;
        this.enemyY = enemyY;
        this.targetBearing = targetBearing;
        this.distance = distance;
        this.attackMode = attackMode;
    }

    //Action Message with no attackMode (point gun and shoot!)
    public ActionMessage(double enemyX, double enemyY, double targetBearing, double distance) {
        this.enemyX = enemyX;
        this.enemyY = enemyY;
        this.targetBearing = targetBearing;
        this.distance = distance;
        this.attackMode = 0;
    }

    public double getEnemyX() {
        return enemyX;
    }

    public double getEnemyY() {
        return enemyY;
    }

    public double getTargetBearing() {
        return targetBearing;
    }

    public double getDistance() {
        return distance;
    }

    public int getAttackMode() {
        return attackMode;
    }
}
