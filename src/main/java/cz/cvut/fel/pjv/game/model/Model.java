package cz.cvut.fel.pjv.game.model;

import cz.cvut.fel.pjv.game.Controller;
import cz.cvut.fel.pjv.game.GameData;

import java.util.ArrayList;

import static java.lang.Math.abs;
import static java.lang.Math.signum;

public class Model {
    private final Player player;
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();
    private final Board board;
    private final int boardWidth;
    private final int boardHeight;

    private int helperCounter = 0; // DELETE
    private Controller controller;
    public Model(GameData data, Controller controller) {
        player = new Player(Integer.parseInt(data.getPlayer()[0]), Integer.parseInt(data.getPlayer()[1]), Integer.parseInt(data.getPlayer()[2]), Integer.parseInt(data.getPlayer()[3]), Integer.parseInt(data.getPlayer()[4]), Double.parseDouble(data.getPlayer()[5]), Double.parseDouble(data.getPlayer()[6]), data.getPlayer()[7]);
        for (int i = 0; i < data.getEnemies().length; i++) {
            enemies.add(new Enemy(Integer.parseInt(data.getEnemies()[i][0]), Integer.parseInt(data.getEnemies()[i][1]),Integer.parseInt(data.getEnemies()[i][2]),Integer.parseInt(data.getEnemies()[i][3]),Double.parseDouble(data.getEnemies()[i][4]), data.getEnemies()[i][5]));
        }
        boardWidth = data.getBoard()[0].length;
        boardHeight = data.getBoard().length;
        board = new Board(data.getBoard()[0].length, data.getBoard().length, data.getBoard());
        this.controller = controller;
    }

    /**
     * Call correct move function based on pressed key.
     * @param c
     */
    public void movePlayer(char c) {
        switch (c) {
            case 'w':
                player.setFacing("up");
                player.setMovementSpeed(0,-player.getMaxMovementSpeed());
                break;
            case 'a':
                player.setFacing("left");
                player.setMovementSpeed(-player.getMaxMovementSpeed(),0);
                break;
            case 's':
                player.setFacing("down");
                player.setMovementSpeed(0,player.getMaxMovementSpeed());
                break;
            case 'd':
                player.setFacing("right");
                player.setMovementSpeed(player.getMaxMovementSpeed(), 0);
                break;
            default:
        }
        //Block nextBlock = board.getBlock((int)Math.round(player.getPosX()+player.getMovementSpeed()[0]+1*signum(player.getMovementSpeed()[0])), (int)Math.round(player.getPosY()+player.getMovementSpeed()[1]+1*signum(player.getMovementSpeed()[1])));
        //System.out.println("Next Block X: " + nextBlock.getX() + "   Y: " + nextBlock.getY() + "  can be walked on? " + nextBlock.isWalkThrough());
    }

    /**
     * Stop player movement.
     */
    public void stopPlayer() {
        player.setMovementSpeed(0,0);
    }

    /**
     * Advances state of the game, key method of the game.
     */
    public void update() {
        if (player.isDead()) {
            controller.gameOver();
        } else if (allEnemiesDead()) {
            controller.ggwp();
        } else {
            updatePlayer();
            updateEnemies();
            updateProjectiles();
            checkHits();
            checkItemPickUp();
            helperFunctionToBeDeleted();
        }
    }

    /**
     * Advances the state of player
     */
    public void updatePlayer() {
        Block nextBlock = board.getBlock((int) Math.round(player.getPosX()+1*signum(player.getMovementSpeed()[0])), (int) Math.round(player.getPosY()+1*signum(player.getMovementSpeed()[1])));
        Block thisBlock = board.getBlock((int) Math.round(player.getPosX()), (int) Math.round(player.getPosY()));
        if (nextBlock.isWalkThrough()) {
            if (player.getMovementSpeed()[0]!=0) {
                if ((signum(player.getMovementSpeed()[0]) * (player.getPosX() - thisBlock.getX()) > 0) && (!board.getBlock(nextBlock.getX(), (int) (nextBlock.getY()+1*signum(player.getPosY()- thisBlock.getY()))).isWalkThrough())) {
                    player.setPosX(player.getPosX());
                } else {
                    player.setPosX(player.getPosX() + player.getMovementSpeed()[0]);
                }
            }
            if (player.getMovementSpeed()[1]!=0) {
                if ((signum(player.getMovementSpeed()[1]) * (player.getPosY() - thisBlock.getY()) > 0) && (!board.getBlock((int) (nextBlock.getX()+1*signum(player.getPosX()- thisBlock.getX())), nextBlock.getY()).isWalkThrough())) {
                    player.setPosY(player.getPosY());
                } else {
                    player.setPosY(player.getPosY() + player.getMovementSpeed()[1]);
                }
            }
        } else {
            if (player.getMovementSpeed()[0] != 0) {
                if ((signum(player.getMovementSpeed()[0]) * (player.getPosX() - thisBlock.getX()) > 0)) {
                    player.setPosX(thisBlock.getX());
                } else {
                    player.setPosX(player.getPosX() + player.getMovementSpeed()[0]);
                }
            }
            if (player.getMovementSpeed()[1]!=0) {
                if (signum(player.getMovementSpeed()[1]) * (player.getPosY() - thisBlock.getY()) > 0) {
                    player.setPosY(thisBlock.getY());
                } else {
                    player.setPosY(player.getPosY() + player.getMovementSpeed()[1]);
                }
            }
        }
        if (player.getPosX() < 1) {
            player.setPosX(1);
        }
        if (player.getPosX() > boardWidth) {
            player.setPosX(boardWidth);
        }
        if (player.getPosY() < 1) {
            player.setPosY(1);
        }
        if (player.getPosY() > boardHeight) {
            player.setPosY(boardHeight);
        }
        player.shoot();
    }

    public void helperFunctionToBeDeleted() {
        helperCounter++;
        if (helperCounter > 120) {
            Block thisBlock = board.getBlock((int) Math.round(player.getPosX()), (int) Math.round(player.getPosY()));
            System.out.println("Player X: " + String.format("%.2f",player.getPosX()) + " Y: " + String.format("%.2f",player.getPosY()));
            System.out.println("This Block X: " + thisBlock.getX() + " Y: " + thisBlock.getY());
            //System.out.println(thisBlock.getValue());
            //System.out.println();
            helperCounter = 0;
        }
    }

    /**
     * Advances state of enemies
     */
    public void updateEnemies() {
        for (Enemy e : enemies) {
            e.shoot(directionToPlayer(e));
            Block nextBlock = board.getBlock((int) Math.round(e.getPosX()+1*signum(e.getMovementSpeed()[0])), (int) Math.round(e.getPosY()+1*signum(e.getMovementSpeed()[1])));
            Block thisBlock = board.getBlock((int) Math.round(e.getPosX()), (int) Math.round(e.getPosY()));
            if (nextBlock.isWalkThrough()) {
                if (e.getMovementSpeed()[0]!=0) {
                    if ((signum(e.getMovementSpeed()[0]) * (e.getPosX() - thisBlock.getX()) > 0) && (!board.getBlock(nextBlock.getX(), (int) (nextBlock.getY()+1*signum(e.getPosY()- thisBlock.getY()))).isWalkThrough())) {
                        e.setPosX(e.getPosX());
                    } else {
                        e.setPosX(e.getPosX() + e.getMovementSpeed()[0]);
                    }
                }
                if (e.getMovementSpeed()[1]!=0) {
                    if ((signum(e.getMovementSpeed()[1]) * (e.getPosY() - thisBlock.getY()) > 0) && (!board.getBlock((int) (nextBlock.getX()+1*signum(e.getPosX()- thisBlock.getX())), nextBlock.getY()).isWalkThrough())) {
                        e.setPosY(e.getPosY());
                    } else {
                        e.setPosY(e.getPosY() + e.getMovementSpeed()[1]);
                    }
                }
            } else {
                if (e.getMovementSpeed()[0] != 0) {
                    if ((signum(e.getMovementSpeed()[0]) * (e.getPosX() - thisBlock.getX()) > 0)) {
                        e.setPosX(thisBlock.getX());
                    } else {
                        e.setPosX(e.getPosX() + e.getMovementSpeed()[0]);
                    }
                }
                if (e.getMovementSpeed()[1]!=0) {
                    if (signum(e.getMovementSpeed()[1]) * (e.getPosY() - thisBlock.getY()) > 0) {
                        e.setPosY(thisBlock.getY());
                    } else {
                        e.setPosY(e.getPosY() + e.getMovementSpeed()[1]);
                    }
                }
            }
            if (e.getPosX() < 1) {
                e.setPosX(1);
            }
            if (e.getPosX() > boardWidth) {
                e.setPosX(boardWidth);
            }
            if (e.getPosY() < 1) {
                e.setPosY(1);
            }
            if (e.getPosY() > boardHeight) {
                e.setPosY(boardHeight);
            }
            e.update();
        }
    }

    /**
     * Calculates the direction in which should the enemy shoot. The projectile in this direction will be the closest to the player.
     * @param enemy enemy, whose position is used in the calculation
     * @return the direction in String
     */
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


    /**
     * Advances state of all projectiles. Iterates through all projectiles of player, then iterates through all enemies and their projectiles.
     */
    public void updateProjectiles() {
        ArrayList<Projectile> toBeRemoved = new ArrayList<>();
        for (Projectile p : player.getProjectiles()) {
            p.setPosX(p.getPosX()+p.getSpeed()[0]);
            p.setPosY(p.getPosY()+p.getSpeed()[1]);
            Block nextBlock = board.getBlock((int) Math.round(p.getPosX()+0.01*signum(p.getSpeed()[0])), (int) Math.round(p.getPosY()+0.01*signum(p.getSpeed()[1])));
            if (p.getPosX()>boardWidth || p.getPosX()<0 || p.getPosY()>boardHeight || p.getPosY()<0 || !nextBlock.isShootThrough()) {
                toBeRemoved.add(p);
            }
        }
        for (Projectile p : toBeRemoved) {
            player.getProjectiles().remove(p);
        }
        for (Enemy e : enemies) {
            toBeRemoved = new ArrayList<>();
            for (Projectile p : e.getProjectiles()) {
                p.setPosX(p.getPosX()+p.getSpeed()[0]);
                p.setPosY(p.getPosY()+p.getSpeed()[1]);
                Block nextBlock = board.getBlock((int) Math.round(p.getPosX()+0.01*signum(p.getSpeed()[0])), (int) Math.round(p.getPosY()+0.01*signum(p.getSpeed()[1])));
                if (p.getPosX()>boardWidth || p.getPosX()<0 || p.getPosY()>boardHeight || p.getPosY()<0 || !nextBlock.isShootThrough()) {
                    toBeRemoved.add(p);
                }
            }
            for (Projectile p : toBeRemoved) {
                e.getProjectiles().remove(p);
            }
        }
    }

    /**
     * Check if any of the projectiles hit something. Player projectiles can only hurt enemies and Enemy projectiles can only hurt the player.
     */
    public void checkHits() {
        // iterate through all the projectiles
        ArrayList<Projectile> projectilesToRemove = new ArrayList<>();
        ArrayList<Enemy> enemiesToRemove = new ArrayList<>();
        for (Projectile p : player.getProjectiles()) {
            for (Enemy e : enemies) {
                if (abs(p.getPosY()-e.getPosY()) < 0.5 && abs(p.getPosX()-e.getPosX()) < 0.5) {
                    e.setHp(e.getHp()-player.getAttackDamage());
                    if (e.getHp() <= 0) {
                        killEnemy(e);
                        enemiesToRemove.add(e);
                    }
                    projectilesToRemove.add(p);
                }
            }
        }
        player.getProjectiles().removeAll(projectilesToRemove);
        enemies.removeAll(enemiesToRemove);
        for (Enemy e : enemies) {
            for (Projectile p : e.getProjectiles()) {
                // TODO: very similar to player projectiles above
            }
        }
    }

    /**
     * Check if player is standing on any items on the board and should pick them up.
     */
    public void checkItemPickUp() {
        for (Item item : items) {
            // TODO
        }
    }

    /**
     * When enemy is killed, update its isDead attribute, drop item that it should drop and remove it from enemies list, so it doesn't render anymore.
     *
     * @param enemy the enemy whose HP went to 0 or below
     */
    public void killEnemy(Enemy enemy) {
        Item item;
        enemy.setDead(true);
        switch (enemy.getDrop()) {
            case "coin":
                item = new Coin(enemy.getPosX(), enemy.getPosY());
                items.add(item);
                break;
            case "potion":
                item = new HealingPotion(enemy.getPosX(), enemy.getPosY());
                items.add(item);
                break;
            case "key":
                item = new Key(enemy.getPosX(), enemy.getPosY());
                items.add(item);
                break;
        }
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

    public ArrayList<Item> getItems() {
        return items;
    }

    /**
     * Check if all enemies are dead, so the level is done.
     *
     * @return {@code true} if all enemies are dead or the list is empty, {@code false} otherwise.
     */
    public boolean allEnemiesDead() {
        for (Enemy e : enemies) {
            if (!e.isDead()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Save Player data like attack damage, attack speed, maximum HP, movement speed into .json format so in next level the Player is alreadz upgraded.
     */
    public void savePlayerData() {
        // TODO
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }
}
