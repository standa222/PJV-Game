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
    }

    public void shoot() {
        shootingCounter++;
        if (shootingCounter > 120/attackSpeed) {
            Projectile p = new Projectile(posX, posY, this.getClass(), facing);
            projectiles.add(p);
            shootingCounter = 0;
        }
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

    public void heal(int healValue) {
        hp += healValue;
    }
}
