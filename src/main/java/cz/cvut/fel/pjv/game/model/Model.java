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
    private Controller controller;
    public Model(GameData data, Controller controller) {
        player = new Player(Integer.parseInt(data.getPlayer()[0]), Integer.parseInt(data.getPlayer()[1]), Integer.parseInt(data.getPlayer()[2]), Integer.parseInt(data.getPlayer()[3]), Integer.parseInt(data.getPlayer()[4]), Double.parseDouble(data.getPlayer()[5]), data.getPlayer()[6]);
        for (int i = 0; i < data.getEnemies().length; i++) {
            enemies.add(new Enemy(Integer.parseInt(data.getEnemies()[i][0]), Integer.parseInt(data.getEnemies()[i][1]),Integer.parseInt(data.getEnemies()[i][2]),Integer.parseInt(data.getEnemies()[i][3]),Double.parseDouble(data.getEnemies()[i][4]), data.getEnemies()[i][5]));
        }
        board = new Board(data.getBoardWidth(), data.getBoardHeight(), data.getBoard());
        this.controller = controller;
    }

    /**
     * Call correct move function based on pressed key.
     * @param c
     */
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

    /**
     * Stop player movement.
     */
    public void stopPlayer() {
        player.setMovementSpeed(0);
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
        }
    }

    /**
     * Advances the state of player
     */
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

    /**
     * Advances state of enemies
     */
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
}
