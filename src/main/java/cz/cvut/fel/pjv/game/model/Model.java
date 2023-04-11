package cz.cvut.fel.pjv.game.model;

import cz.cvut.fel.pjv.game.Controller;
import cz.cvut.fel.pjv.game.GameData;

import java.util.ArrayList;

public class Model {
    private Player player;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private Board board;
    public Model(GameData data, Controller controller) {
        player = new Player(Integer.parseInt(data.getPlayer()[0]), Integer.parseInt(data.getPlayer()[1]), Integer.parseInt(data.getPlayer()[2]), Integer.parseInt(data.getPlayer()[3]), Integer.parseInt(data.getPlayer()[4]), Double.parseDouble(data.getPlayer()[5]), Double.parseDouble(data.getPlayer()[6]), data.getPlayer()[7]);
        for (int i = 0; i < data.getEnemies().length; i++) {
            enemies.add(new Enemy(Integer.parseInt(data.getEnemies()[i][0]), Integer.parseInt(data.getEnemies()[i][1]),Integer.parseInt(data.getEnemies()[i][2]),Integer.parseInt(data.getEnemies()[i][3]),Double.parseDouble(data.getEnemies()[i][4]), data.getEnemies()[i][5]));
        }
        board = new Board(data.getBoardWidth(), data.getBoardHeight(), data.getBoard());
    }

    public void movePlayer(char c) {
        switch (c) {
            case 'w':
                if (board.getBlock((int) player.getPosX(), (int) (player.getPosY()-1)).isWalkThrough()) {
                    player.moveUp();
                }
                break;
            case 'a':
                if (board.getBlock((int) (player.getPosX()-1), (int) player.getPosY()).isWalkThrough()) {
                    player.moveLeft();
                }
                break;
            case 's':
                if (board.getBlock((int) player.getPosX(), (int) (player.getPosY()+1)).isWalkThrough()) {
                    player.moveDown();
                }
                break;
            case 'd':
                if (board.getBlock((int)player.getPosX()+1, (int) player.getPosY()).isWalkThrough()) {
                    player.moveRight();
                }
                break;
            default:
        }
    }

    public void stopPlayer() {
        player.setMovementSpeed(0);
    }

    public void update() {
        updatePlayer();
        updateEnemies();
        updateProjectiles();
        detectHits();
    }

    public void updatePlayer() {
        double playerSpeed = player.getMovementSpeed();
        switch (player.getFacing()) {
            case "up":
                player.setPosY(player.getPosY()-playerSpeed);
                break;
            case "down":
                player.setPosY(player.getPosY()+playerSpeed);
                break;
            case "right":
                player.setPosX(player.getPosX()+playerSpeed);
                break;
            case "left":
                player.setPosX(player.getPosX()-playerSpeed);
                break;
        }
        player.shoot();
    }

    public void updateEnemies() {

    }

    public void updateProjectiles() {
        for (Projectile p : player.getProjectiles()) {
            switch (p.getDirection()) {
                case "up":
                    p.setPosY(p.getPosY()-p.getSpeed());
                    break;
                case "down":
                    p.setPosY(p.getPosY()+p.getSpeed());
                    break;
                case "right":
                    p.setPosX(p.getPosX()+p.getSpeed());
                    break;
                case "left":
                    p.setPosX(p.getPosX()-p.getSpeed());
                    break;
            }
        }
    }

    public void detectHits() {

    }

    public Board getBoard() {
        return board;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }
}
