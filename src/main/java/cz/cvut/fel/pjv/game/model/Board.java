package cz.cvut.fel.pjv.game.model;

import java.util.ArrayList;

public class Board {
    private int width;
    private int height;
    private int[][] blockMap;
    private ArrayList<Block> blocks = new ArrayList<>();

    public Board(int width, int height, int[][] blockMap) {
        this.width = width;
        this.height = height;
        this.blockMap = blockMap;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (blockMap[j][i] == 5) {
                    Chest chest = new Chest(i+1, j+1, blockMap[j][i], assignWalkThrough(blockMap[j][i]), assignShootThrough(blockMap[j][i]));
                    blocks.add(chest);
                } else {
                    Block block = new Block(i + 1, j + 1, blockMap[j][i], assignWalkThrough(blockMap[j][i]), assignShootThrough(blockMap[j][i]));
                    blocks.add(block);
                }
            }
        }
    }

    public Board() {
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    public Block getBlock(int x, int y) {
        for (Block b : blocks) {
            if (b.getX()==x && b.getY()==y) {
                return b;
            }
        }
        return new Block(-1,-1,-1, true,true);
    }

    public void changeBlockValue(int x, int y, int newValue) {
        getBlock(x,y).setValue(newValue);
    }

    private boolean assignWalkThrough(int number) {
        return number%2==0;
    }

    private boolean assignShootThrough(int number) {
        return number%3==0;
    }

    public int[][] getBlockMap(){
        return blockMap;
    }

}
