package cz.cvut.fel.pjv.game.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fel.pjv.game.Controller;
import cz.cvut.fel.pjv.game.GameData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import static java.lang.Math.*;

public class Model {

    private static final Logger LOGGER = Logger.getLogger(Model.class.getName());
    private final Player player;
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final ArrayList<Item> items = new ArrayList<>();
    private final Board board;
    private final int boardWidth;
    private final int boardHeight;
    private final Controller controller;
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
     * Loads player data from a file and updates the player accordingly.
     *
     * @throws IOException if an error occurs while reading the file.
     */
    public void loadPlayerData() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String playerData = objectMapper.readValue(new File("src/main/resources/player.txt"), String.class);
        String[] data = playerData.split(" ");
        player.setMax_hp(Integer.parseInt(data[0]));
        player.setHp(Integer.parseInt(data[0]));
        player.setAttackDamage(Integer.parseInt(data[1]));
        player.setAttackSpeed(Double.parseDouble(data[2]));
        player.setMaxMovementSpeed(Double.parseDouble(data[3]));
    }

    /**
     * Sets player movement speed and facing attribute in the specified direction based on pressed key.
     * When moving, player is always moving at max speed.
     *
     * @param c The direction character ('w' for up, 'a' for left, 's' for down, 'd' for right).
     */
    public void movePlayer(char c) {
        switch (c) {
            case 'w' -> {
                player.setFacing("up");
                player.setMovementSpeed(0, -player.getMaxMovementSpeed());
            }
            case 'a' -> {
                player.setFacing("left");
                player.setMovementSpeed(-player.getMaxMovementSpeed(), 0);
            }
            case 's' -> {
                player.setFacing("down");
                player.setMovementSpeed(0, player.getMaxMovementSpeed());
            }
            case 'd' -> {
                player.setFacing("right");
                player.setMovementSpeed(player.getMaxMovementSpeed(), 0);
            }
            default -> {
            }
        }
    }

    /**
     * Stops player movement.
     */
    public void stopPlayer() {
        player.setMovementSpeed(0,0);
    }

    /**
     * Advances state of the game, key method of model and whole game.
     * If the player is dead, triggers the "gameOver" method in the controller.
     * Otherwise, updates the player, enemies, projectiles, and checks for hits and item pickups.
     */
    public void update() {
        if (player.isDead()) {
            controller.gameOver();
        } else {
            updatePlayer();
            updateEnemies();
            updateProjectiles();
            checkHits();
            checkItemPickUp();
        }
    }

    /**
     * Advances the state of player. Updates the player's position and actions.
     * Prevents the movement in case of hitting block that cannot be walked on or the edge of the board.
     */
    public void updatePlayer() {
        // get current and next block which are used later
        Block nextBlock = getBoard().getBlock((int) Math.round(player.getPosX()+1*signum(player.getMovementSpeed()[0])), (int) Math.round(player.getPosY()+1*signum(player.getMovementSpeed()[1])));
        Block thisBlock = getBoard().getBlock((int) Math.round(player.getPosX()), (int) Math.round(player.getPosY()));
        // open chest if next block is chest and player has key
        if (nextBlock.getValue() == 5 && player.hasKey()) {
            openChest((Chest) nextBlock);
        }
        // move if next block  is walkThrough
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
        // keep player on the board
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

    /**
     * Opens a chest, triggering the necessary actions and updates, like replacing the block, removing key from player's inventory or dropping coins.
     *
     * @param chest The chest to be opened.
     */
    public void openChest(Chest chest) {
        // replace the chest with a new grass block
        Block newBlock = new Block(chest.getX(), chest.getY(), 6, true, true);
        Coin chestCoin = new Coin(chest.getX(), chest.getY(), (int) (79+ random()*20));
        player.removeKey();
        items.add(chestCoin);
        chest.setOpened();
        board.getBlocks().add(newBlock);
        board.getBlocks().remove(chest);
        controller.redrawBoard();
        LOGGER.info("Chest opened! " + chestCoin.getValue() + " coins dropped.");
    }

    /**
     * Updates the positions and actions of enemies.
     * Prevents the movement in case of hitting block that cannot be walked on or the edge of the board.
     */
    public void updateEnemies() {
        for (Enemy e : enemies) {
            e.shoot(directionToPlayer(e));
            // get current and next block which are used later
            Block nextBlock = board.getBlock((int) Math.round(e.getPosX()+1*signum(e.getMovementSpeed()[0])), (int) Math.round(e.getPosY()+1*signum(e.getMovementSpeed()[1])));
            Block thisBlock = board.getBlock((int) Math.round(e.getPosX()), (int) Math.round(e.getPosY()));
            // move if next block  is walkThrough
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
            // keep enemy on the board
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
     * @return The direction from the enemy to the player ("up", "down", "left", or "right").
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
     * Updates the positions of projectiles.
     * Delete those that are out of bounds or collide with obstacles.
     */
    public void updateProjectiles() {
        // iterate through all player projectiles
        ArrayList<Projectile> toBeRemoved = new ArrayList<>();
        for (Projectile p : player.getProjectiles()) {
            p.setPosX(p.getPosX()+p.getSpeed()[0]);
            p.setPosY(p.getPosY()+p.getSpeed()[1]);
            Block nextBlock = board.getBlock((int) Math.round(p.getPosX()+0.01*signum(p.getSpeed()[0])), (int) Math.round(p.getPosY()+0.01*signum(p.getSpeed()[1])));
            if (p.getPosX()>boardWidth+1 || p.getPosX()<0 || p.getPosY()>boardHeight+1 || p.getPosY()<0 || nextBlock.isShootThrough()) {
                toBeRemoved.add(p);
            }
        }
        // remove those are out of the board or hit blocks that are not shootThrough
        player.getProjectiles().removeAll(toBeRemoved);
        // iterate through projectiles of all every enemy
        for (Enemy e : enemies) {
            toBeRemoved = new ArrayList<>();
            for (Projectile p : e.getProjectiles()) {
                p.setPosX(p.getPosX()+p.getSpeed()[0]);
                p.setPosY(p.getPosY()+p.getSpeed()[1]);
                Block nextBlock = board.getBlock((int) Math.round(p.getPosX()+0.01*signum(p.getSpeed()[0])), (int) Math.round(p.getPosY()+0.01*signum(p.getSpeed()[1])));
                if (p.getPosX()>boardWidth+1 || p.getPosX()<0 || p.getPosY()>boardHeight+1 || p.getPosY()<0 || nextBlock.isShootThrough()) {
                    toBeRemoved.add(p);
                }
            }
            // remove those are out of the board or hit blocks that are not shootThrough
            e.getProjectiles().removeAll(toBeRemoved);
        }
    }


    /**
     * Checks for collisions between player's projectiles and enemies, and between projectiles of enemies and the player.
     * Deletes projectiles that hit.
     * Updates enemy and player health accordingly.
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
                        LOGGER.info("Enemy killed on X: " + String.format("%.2f", e.getPosX()) + ", Y: " + String.format("%.2f", e.getPosY()) + ". " + e.getDrop() + " dropped.");
                    }
                    projectilesToRemove.add(p);
                }
            }
        }
        // remove those that hit an enemy
        player.getProjectiles().removeAll(projectilesToRemove);
        enemies.removeAll(enemiesToRemove);
        projectilesToRemove = new ArrayList<>();
        for (Enemy e : enemies) {
            for (Projectile p : e.getProjectiles()) {
                if (abs(p.getPosY()-player.getPosY()) < 0.5 && abs(p.getPosX()-player.getPosX()) < 0.5) {
                    LOGGER.info("Player hit on X: " + String.format("%.2f", player.getPosX()) + ", Y: " + String.format("%.2f", player.getPosY()) + ". " + e.getAttackDamage() + " damage done.");
                    player.setHp(player.getHp()-e.getAttackDamage());
                    if (player.getHp() <= 0) {
                        LOGGER.info("Player killed on X: " + String.format("%.2f", player.getPosX()) + ", Y: " + String.format("%.2f", player.getPosY()) + ". Game over!");
                        player.kill();
                    }
                    projectilesToRemove.add(p);
                }
            }
            // remove those that hit the player
            e.getProjectiles().removeAll(projectilesToRemove);
        }
    }

    /**
     * Checks if the player is picking up any items on the board.
     * If the player picks up an item, it is added to their inventory and further actions are taken based on the item type.
     */
    public void checkItemPickUp() {
        ArrayList<Item> toBeRemoved = new ArrayList<>();
        for (Item item : items) {
            if (abs(item.getPosX()-player.getPosX()) < 0.5 && abs(item.getPosY()-player.getPosY()) < 0.5) {
                if (!(item instanceof Key) || !(player.hasKey())) {
                    LOGGER.info(item.getClass().getSimpleName() + " picked up.");
                    player.addToInventory(item);
                    checkUpgrade();
                    toBeRemoved.add(item);
                }
            }
        }
        for (Item item : toBeRemoved) {
            items.remove(item);
        }
    }

    /**
     * Checks if the player's inventory contains any coins with a value of 100 or more.
     * If such coins are found, the player is eligible for an upgrade and the upgrade process is triggered.
     * Player is given choice
     */
    public void checkUpgrade() {
        for (Item item : player.getInventory()) {
            if (item instanceof Coin) {
                if (((Coin) item).getValue() >= 100) {
                    LOGGER.info("Player has " + player.getCoinValue() + " coins! TIme for upgrade.");
                    controller.upgrade();
                }
            }
        }
    }

    /**
     * Kills the specified enemy and determines the item to drop based on its drop type.
     * The item is added to the list of items in the game.
     *
     * @param enemy The enemy to be killed.
     */
    public void killEnemy(Enemy enemy) {
        Item item;
        enemy.setDead(true);
        switch (enemy.getDrop()) {
            case "coin" -> {
                item = new Coin(enemy.getPosX(), enemy.getPosY());
                items.add(item);
            }
            case "potion" -> {
                item = new HealingPotion(enemy.getPosX(), enemy.getPosY());
                items.add(item);
            }
            case "key" -> {
                item = new Key(enemy.getPosX(), enemy.getPosY());
                items.add(item);
            }
        }
    }

    /**
     * Check if all enemies are dead, so the level can be endedx.
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

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }
}