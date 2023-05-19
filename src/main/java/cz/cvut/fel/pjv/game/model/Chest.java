package cz.cvut.fel.pjv.game.model;

public class Chest extends Block {
    private boolean opened;
    public Chest(int x, int y, int value, boolean walkThrough, boolean shootThrough) {
        super(x, y, value, walkThrough, shootThrough);
        opened = false;
    }

    public void setOpened() {
        opened = true;
    }
}
