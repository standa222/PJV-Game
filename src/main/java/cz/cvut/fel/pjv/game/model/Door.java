package cz.cvut.fel.pjv.game.model;

public class Door extends Block {
    private boolean opened;
    public Door(int x, int y, int value, boolean walkThrough, boolean shootThrough) {
        super(x, y, value, walkThrough, shootThrough);
        opened = false;
    }
}
