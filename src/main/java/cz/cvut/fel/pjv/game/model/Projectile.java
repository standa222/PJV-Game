package cz.cvut.fel.pjv.game.model;

public class Projectile {
    private double posX;
    private double posY;
    private Class source;
    private String direction;
    private double speed[];
    private double maxSpeed = 0.1;

    public Projectile(double posX, double posY, Class source, String direction) {
        this.posX = posX;
        this.posY = posY;
        this.source = source;
        this.direction = direction;
        switch (direction) {
            case "up":
                speed = new double[]{ 0, -maxSpeed};
                break;
            case "down":
                speed = new double[] {0, maxSpeed};
                break;
            case "left":
                speed = new double[] {-maxSpeed, 0};
                break;
            case "right":
                speed = new double[] {maxSpeed, 0};
                break;
        }
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
