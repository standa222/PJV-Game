package cz.cvut.fel.pjv.game.model;

public class Coin extends Item {
    private int value;

    public Coin(double posX, double posY) {
        super(posX, posY);
        this.value = 50;
    }

    @Override
    public void pickUp() {
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
