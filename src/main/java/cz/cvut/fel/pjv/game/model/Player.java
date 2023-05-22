package cz.cvut.fel.pjv.game.model;

import java.util.ArrayList;

public class Player {
    private double posX;
    private double posY;
    private int max_hp;
    private int hp;
    private int attackDamage;
    private double attackSpeed;
    private double maxMovementSpeed;
    private double[] movementSpeed;
    private String facing;
    private final ArrayList<Projectile> projectiles;
    private final ArrayList<Item> inventory;
    private boolean isDead;

    private int shootingCounter = 0;

    public Player(double posX, double posY, int max_hp, int hp, int attackDamage, double attackSpeed, double maxMovementSpeed, String facing) {
        this.posX = posX;
        this.posY = posY;
        this.max_hp = max_hp;
        this.hp = hp;
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.maxMovementSpeed = maxMovementSpeed;
        movementSpeed = new double[]{0,0};
        this.facing = facing;
        projectiles = new ArrayList<>();
        isDead = false;
        inventory = new ArrayList<>();
    }

    /**
     * Increments the counter and checks if it has reached the threshold.
     * If the counter exceeds the threshold, it creates a new projectile and resets the counter.
     */
    public void shoot() {
        shootingCounter++;
        if (shootingCounter >= 120/attackSpeed) {
            Projectile p = new Projectile(posX, posY, "player", facing);
            projectiles.add(p);
            shootingCounter = 0;
        }
    }

    /**
     * Heals the entity by the specified heal value.
     *
     * @param healValue The amount of healing to be applied.
     */
    public void heal(int healValue) {
        hp += healValue;
        if (hp > max_hp) {
            hp = max_hp;
        }
    }

    public int getCoinValue() {
        for (Item item : inventory) {
            if (item instanceof Coin) {
                return ((Coin) item).getValue();
            }
        }
        return 0;
    }

    public Coin getCoin() {
        for (Item item : inventory) {
            if (item instanceof Coin) {
                return (Coin) item;
            }
        }
        return null;
    }

    /**
     * Adds the specified item to the inventory.
     * Instantly heal the player if it is a healing potion.
     * Increase coins value in player inventory if it is a coin and player already has some.
     * Add item to inventory otherwise (if its coins and player has none, or it is a key)
     *
     * @param item The item to be added to the inventory.
     */
    public void addToInventory(Item item) {
        if (item instanceof HealingPotion) {
            heal(((HealingPotion) item).getHealValue());
            return;
        }
        if (item instanceof Coin) {
            for (Item itemInInventory : inventory) {
                if (itemInInventory instanceof Coin) {
                    ((Coin) itemInInventory).setValue(((Coin) itemInInventory).getValue()+((Coin) item).getValue());
                    return;
                }
            }
        }
        inventory.add(item);
    }

    /**
     * Checks if the player has a key in their inventory.
     *
     * @return {@code true} if the player has a key, {@code false} otherwise.
     */
    public boolean hasKey() {
        for (Item item : inventory) {
            if (item instanceof Key) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes a key from the player's inventory.
     * If the player has a key in their inventory, it is removed.
     * If the player does not have a key, nothing happens.
     */
    public void removeKey() {
        Item key = null;
        for (Item item : inventory) {
            if (item instanceof Key) {
                key = item;
                break;
            }
        }
        if (key != null) {
            inventory.remove(key);
        }
    }

    public void kill() {
        isDead = true;
    }

    /**
     * Upgrades the player's attack damage.
     * Increases the attack damage by 10.
     * Decreases the player's coin value by 100.
     */
    public void upgradeAD() {
        attackDamage += 10;
        getCoin().setValue(getCoinValue()-100);
    }

    /**
     * Upgrades the player's attack speed.
     * Increases the attack damage by 0.25.
     * Decreases the player's coin value by 100.
     */
    public void upgradeAS() {
        attackSpeed += 0.5;
        getCoin().setValue(getCoinValue()-100);
    }

    /**
     * Upgrades the player's maximum HP.
     * Increases the attack damage by 50.
     * Decreases the player's coin value by 100.
     */
    public void upgradeHP() {
        max_hp += 50;
        hp += 50;
        getCoin().setValue(getCoinValue()-100);
    }

    /**
     * Upgrades the player's maximum movement speed.
     * Increases the attack damage by 0.01.
     * Decreases the player's coin value by 100.
     */
    public void upgradeMS() {
        maxMovementSpeed += 0.015;
        getCoin().setValue(getCoinValue()-100);
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public ArrayList<Projectile> getProjectiles() {
        return projectiles;
    }

    public String getFacing() {
        return facing;
    }

    public void setFacing(String facing) {
        this.facing = facing;
    }

    public double getMaxMovementSpeed() {
        return maxMovementSpeed;
    }

    public void setMaxMovementSpeed(double maxMovementSpeed) {
        this.maxMovementSpeed = maxMovementSpeed;
    }

    public double[] getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(double speedX, double speedY) {
        this.movementSpeed = new double[]{speedX, speedY};
    }

    public boolean isDead() {
        return isDead;
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setMax_hp(int max_hp) {
        this.max_hp = max_hp;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public void setAttackSpeed(double attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public int getMax_hp() {
        return max_hp;
    }

    public double getAttackSpeed() {
        return attackSpeed;
    }
}