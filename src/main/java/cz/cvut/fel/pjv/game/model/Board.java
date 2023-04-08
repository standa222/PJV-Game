package cz.cvut.fel.pjv.game.model;

public class Board {
    private int width;
    private int height;
    private int[][] blockMap;

    public Board(int width, int height, int[][] blockMap) {
        this.width = width;
        this.height = height;
        this.blockMap = blockMap;
    }
}
