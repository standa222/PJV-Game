package cz.cvut.fel.pjv.game.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Board {
    private int width;
    private int height;
    private int[][] blockMap;
    private ArrayList<Block> blocks;

    public Board(int width, int height, int[][] blockMap) {
        this.width = width;
        this.height = height;
        this.blockMap = blockMap;
        blocks = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Block block = new Block(i, j, blockMap[j][i],true, true);
                blocks.add(block);
            }
        }
    }

    public int[][] getBlockMap() {
        return blockMap;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
    }
}
