package cz.cvut.fel.pjv.game.model;

public class Projectile {
    private double posX;
    private double posY;
    private enum source {Player, Enemy};
    private String direction;
    private double speed;

    public Projectile(double posX, double posY, String direction, double speed) {
        this.posX = posX;
        this.posY = posY;
        this.direction = direction;
        this.speed = speed;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public String getDirection() {
        return direction;
    }

    public double getSpeed() {
        return speed;
    }
}
