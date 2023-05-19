package cz.cvut.fel.pjv.game.model;

public class Projectile {
    private double posX;
    private double posY;
    private String source;
    private String direction;
    private double[] speed;

    public Projectile(double posX, double posY, String source, String direction) {
        this.posX = posX;
        this.posY = posY;
        this.source = source;
        this.direction = direction;
        double maxSpeed = 0.1;
        switch (direction) {
            case "up" -> speed = new double[]{0, -maxSpeed};
            case "down" -> speed = new double[]{0, maxSpeed};
            case "left" -> speed = new double[]{-maxSpeed, 0};
            case "right" -> speed = new double[]{maxSpeed, 0};
        }
    }

    public Projectile() {
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

    public double[] getSpeed() {
        return speed;
    }

}
