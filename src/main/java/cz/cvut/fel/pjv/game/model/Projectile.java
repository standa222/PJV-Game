package cz.cvut.fel.pjv.game.model;

public class Projectile {
    private int posX;
    private int posY;
    private enum source {Player, Enemy};
    private String direction;
    private int speed;

    public Projectile(int posX, int posY, String direction, int speed) {
        this.posX = posX;
        this.posY = posY;
        this.direction = direction;
        this.speed = speed;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }
}
