package cz.cvut.fel.pjv.game.model;

public class HealingPotion extends Item {

    private int healValue;

    public HealingPotion(double posX, double posY) {
        super(posX, posY);
        this.healValue = 25;
    }

    public int getHealValue() {
        return healValue;
    }

    @Override
    public void pickUp() {

    }
}
