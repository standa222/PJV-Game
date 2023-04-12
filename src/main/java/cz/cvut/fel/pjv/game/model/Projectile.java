package cz.cvut.fel.pjv.game.model;

public class Projectile {
    private double posX;
    private double posY;
    private Class source;
    private String direction;
    private double speed;

    public Projectile(double posX, double posY, Class source, String direction, double speed) {
        this.posX = posX;
        this.posY = posY;
        this.source = source;
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
