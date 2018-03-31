package FightClub.Extra;

import java.util.Objects;

public class InformationMessage implements java.io.Serializable {

    private double currentEnergy;
    private double targetEnergy;
    private String targetName;
    private double x, y,targetX,targetY;
    private boolean isEnemy;

    public InformationMessage(double currentEnergy, double targetEnergy, String targetName, double x, double y, double targetX, double targetY, boolean isEnemy) {
        this.currentEnergy = currentEnergy;
        this.targetEnergy = targetEnergy;
        this.targetName = targetName;
        this.x = x;
        this.y = y;
        this.targetX = targetX;
        this.targetY = targetY;
        this.isEnemy = isEnemy;
    }

    public double getCurrentEnergy() {
        return currentEnergy;
    }

    public double getTargetEnergy() {
        return targetEnergy;
    }

    public String getTargetName() {
        return targetName;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getTargetX() {
        return targetX;
    }

    public double getTargetY() {
        return targetY;
    }

    public boolean isEnemy() {
        return isEnemy;
    }
}
