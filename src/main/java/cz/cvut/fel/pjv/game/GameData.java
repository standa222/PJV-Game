package cz.cvut.fel.pjv.game;

public class GameData {
    private int [][] board;
    private String[][] enemies;
    private String[] player;

    public GameData(){
    }

    public GameData(int[][] board, String[][] enemies, String[] player) {
        this.board = board;
        this.enemies = enemies;
        this.player = player;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public String[][] getEnemies() {
        return enemies;
    }

    public void setEnemies(String[][] enemies) {
        this.enemies = enemies;
    }

    public String[] getPlayer() {
        return player;
    }

    public void setPlayer(String[] player) {
        this.player = player;
    }
}