package cz.cvut.fel.pjv.game.model;

public class Chest extends Block {
    private boolean opened = false;
    public Chest(int x, int y, int value, boolean walkThrough, boolean shootThrough) {
        super(x, y, value, walkThrough, shootThrough);
    }
}
