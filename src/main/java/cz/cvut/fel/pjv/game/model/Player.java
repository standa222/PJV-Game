package cz.cvut.fel.pjv.game.model;

import java.util.ArrayList;

public class Player {
    private double posX;
    private double posY;
    private int max_hp;
    private int hp;
    private int attackDamage;
    private double attackSpeed;
    private double critChance;
    private double movementSpeed;
    private String facing;
    private ArrayList<Projectile> projectiles;
    private ArrayList<Item> inventory;

    private int shootingCounter = 0;

    public Player(double posX, double posY, int max_hp, int hp, int attackDamage, double attackSpeed, double critChance, String facing) {
        this.posX = posX;
        this.posY = posY;
        this.max_hp = max_hp;
        this.hp = hp;
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.critChance = critChance;
        this.facing = facing;
        projectiles = new ArrayList<>();
    }

    public void moveUp() {
        facing = "up";
        if (posY <= 1) {
            posY = 1;
        } else {
            movementSpeed = 0.05;
        }
    }

    public void moveDown() {
        facing = "down";
        if (posY >= 16) {
            posY = 16;
        } else {
            movementSpeed = 0.05;
        }
    }

    public void moveRight() {
        facing = "right";
        if (posX >= 20) {
            posX = 20;
        } else {
            movementSpeed = 0.05;
        }
    }

    public void moveLeft() {
        facing = "left";
        if (posX <= 1) {
            posX = 1;
        } else {
            movementSpeed = 0.05;
        }
    }

    public void shoot() {
        shootingCounter++;
        if (shootingCounter > 120/attackSpeed) {
            Projectile p = new Projectile(posX, posY, this.getClass(), facing, 0.1);
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

    public ArrayList<Projectile> getProjectiles() {
        return projectiles;
    }

    public String getFacing() {
        return facing;
    }

    public double getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(double movementSpeed) {
        this.movementSpeed = movementSpeed;
    }
}
