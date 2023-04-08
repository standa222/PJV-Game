package cz.cvut.fel.pjv.game;

public class GameData {
    private int boardWidth;
    private int boardHeight;
    private int [][] board;
    private String[][] enemies;
    private String[] player;

    public GameData(){
    }

    public GameData(int boardWidth, int boardHeight, int[][] board, String[][] enemies, String[] player) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.board = board;
        this.enemies = enemies;
        this.player = player;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public void setBoardWidth(int boardWidth) {
        this.boardWidth = boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public void setBoardHeight(int boardHeight) {
        this.boardHeight = boardHeight;
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
