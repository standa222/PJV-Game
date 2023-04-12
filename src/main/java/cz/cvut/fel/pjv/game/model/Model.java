package cz.cvut.fel.pjv.game.model;

import cz.cvut.fel.pjv.game.Controller;
import cz.cvut.fel.pjv.game.GameData;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class Model {
    private final Player player;
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();
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
        checkHits();
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
        for (Enemy e : enemies) {
            e.shoot(directionToPlayer(e));
            if (e.getPosX()+e.getMovementSpeed()[0] <= 1) {
                e.setPosX(1);
            } else if (e.getPosX()+e.getMovementSpeed()[0] >= 20) {
                e.setPosX(20);
            } else {
                e.setPosX(e.getPosX() + e.getMovementSpeed()[0]);
            }
            if (e.getPosY()+e.getMovementSpeed()[1] <= 1) {
                e.setPosY(1);
            } else if (e.getPosY()+e.getMovementSpeed()[1] >= 16) {
                e.setPosY(16);
            } else {
                e.setPosY(e.getPosY() + e.getMovementSpeed()[1]);
            }
            e.update();
        }
    }

    public String directionToPlayer(Enemy enemy) {
        if (abs(enemy.getPosX()- player.getPosX())<abs(enemy.getPosY()- player.getPosY())) {
            if(enemy.getPosY()- player.getPosY() > 0) {
                return "up";
            } else {
                return "down";
            }
        } else {
            if (enemy.getPosX()- player.getPosX() > 0) {
                return "left";
            } else {
                return "right";
            }
        }
    }

    public void updateProjectiles() {
        ArrayList<Projectile> toBeRemoved = new ArrayList<>();
        for (Projectile p : player.getProjectiles()) {
            switch (p.getDirection()) {
                case "up":
                    p.setPosY(p.getPosY()-p.getSpeed());
                    if (p.getPosY()<0) {
                        toBeRemoved.add(p);
                    }
                    break;
                case "down":
                    p.setPosY(p.getPosY()+p.getSpeed());
                    if (p.getPosY()>17) {
                        toBeRemoved.add(p);
                    }
                    break;
                case "right":
                    p.setPosX(p.getPosX()+p.getSpeed());
                    if (p.getPosX()>21) {
                        toBeRemoved.add(p);
                    }
                    break;
                case "left":
                    p.setPosX(p.getPosX()-p.getSpeed());
                    if (p.getPosX()<0) {
                        toBeRemoved.add(p);
                    }
                    break;
            }
        }
        for (Projectile p : toBeRemoved) {
            player.getProjectiles().remove(p);
        }
        for (Enemy e : enemies) {
            toBeRemoved = new ArrayList<>();
            for (Projectile p : e.getProjectiles()) {
                switch (p.getDirection()) {
                    case "up":
                        p.setPosY(p.getPosY() - p.getSpeed());
                        if (p.getPosY()<0) {
                            toBeRemoved.add(p);
                        }
                        break;
                    case "down":
                        p.setPosY(p.getPosY() + p.getSpeed());
                        if (p.getPosY()>17) {
                            toBeRemoved.add(p);
                        }
                        break;
                    case "right":
                        p.setPosX(p.getPosX() + p.getSpeed());
                        if (p.getPosX()>21) {
                            toBeRemoved.add(p);
                        }
                        break;
                    case "left":
                        p.setPosX(p.getPosX() - p.getSpeed());
                        if (p.getPosX()<0) {
                            toBeRemoved.add(p);
                        }
                        break;
                }
            }
            for (Projectile p : toBeRemoved) {
                e.getProjectiles().remove(p);
            }
        }
    }

    public void checkHits() {

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
