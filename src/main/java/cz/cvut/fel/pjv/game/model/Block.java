package cz.cvut.fel.pjv.game.model;

public class Block {
    private int x;
    private int y;
    private int value;
    private boolean walkThrough;
    private boolean shootThrough;

    public Block(int x, int y, int value, boolean walkThrough, boolean shootThrough) {
        this.x = x;
        this.y = y;
        this.value = value;
        this.walkThrough = walkThrough;
        this.shootThrough = shootThrough;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getValue() {
        return value;
    }
}
