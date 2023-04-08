package cz.cvut.fel.pjv.game.model;

import cz.cvut.fel.pjv.game.GameData;

import java.util.ArrayList;

public class Model {
    private Player player;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private Board board;
    public Model(GameData data) {
        player = new Player(Integer.parseInt(data.getPlayer()[0]), Integer.parseInt(data.getPlayer()[1]), Integer.parseInt(data.getPlayer()[2]), Integer.parseInt(data.getPlayer()[3]), Integer.parseInt(data.getPlayer()[4]), Double.parseDouble(data.getPlayer()[5]), Double.parseDouble(data.getPlayer()[6]), data.getPlayer()[7]);
        for (int i = 0; i < data.getEnemies().length; i++) {
            enemies.add(new Enemy(Integer.parseInt(data.getEnemies()[i][0]), Integer.parseInt(data.getEnemies()[i][1]),Integer.parseInt(data.getEnemies()[i][2]),Integer.parseInt(data.getEnemies()[i][3]),Double.parseDouble(data.getEnemies()[i][4]), data.getEnemies()[i][5]));
        }
        board = new Board(data.getBoardWidth(), data.getBoardHeight(), data.getBoard());

    }
}
