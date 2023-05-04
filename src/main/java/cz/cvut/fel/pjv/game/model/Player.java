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
    private ArrayList<Projectile> projectiles;
    private ArrayList<Item> inventory;
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

    public void shoot() {
        shootingCounter++;
        if (shootingCounter > 120/attackSpeed) {
            Projectile p = new Projectile(posX, posY, this.getClass(), facing);
            projectiles.add(p);
            shootingCounter = 0;
        }
    }

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

    public boolean hasKey() {
        for (Item item : inventory) {
            if (item instanceof Key) {
                return true;
            }
        }
        return false;
    }

    public void upgradeAD() {
        attackDamage += 10;
    }

    public void upgradeAS() {
        attackSpeed += 0.25;
    }

    public void upgradeHP() {
        max_hp += 25;
        hp += 25;
    }

    public void upgradeMS() {
        maxMovementSpeed += 0.01;
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
}
