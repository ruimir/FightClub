package FightClub.Extra;

import java.util.Objects;

public class InformationMessage implements java.io.Serializable {

    private double currentEnergy;
    private double targetEnergy;
    private String targetName;
    private double x, y,targetX,targetY;

    public double getCurrentEnergy() {
        return currentEnergy;
    }

    public void setCurrentEnergy(double currentEnergy) {
        this.currentEnergy = currentEnergy;
    }

    public double getTargetEnergy() {
        return targetEnergy;
    }

    public void setTargetEnergy(double targetEnergy) {
        this.targetEnergy = targetEnergy;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getTargetX() {
        return targetX;
    }

    public void setTargetX(double targetX) {
        this.targetX = targetX;
    }

    public double getTargetY() {
        return targetY;
    }

    public void setTargetY(double targetY) {
        this.targetY = targetY;
    }

    public InformationMessage(double currentEnergy, double targetEnergy, String targetName, double x, double y, double targetX, double targetY) {
        this.currentEnergy = currentEnergy;
        this.targetEnergy = targetEnergy;
        this.targetName = targetName;
        this.x = x;
        this.y = y;
        this.targetX = targetX;
        this.targetY = targetY;
    }

    public InformationMessage(double currentEnergy, double x, double y) {
        this.currentEnergy = currentEnergy;
        this.x = x;
        this.y = y;
    }
}
