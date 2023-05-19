package cz.cvut.fel.pjv.game.model;

public class Item {
    private double posX;
    private double posY;

    public Item(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public Item() {
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }
}
