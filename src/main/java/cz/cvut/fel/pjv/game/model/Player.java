package cz.cvut.fel.pjv.game.model;

import java.util.ArrayList;

public class Player {
    private int posX;
    private int posY;
    private int max_hp;
    private int hp;
    private int attackDamage;
    private double attackSpeed;
    private double critChance;
    private String facing;
    private ArrayList<Projectile> projectiles;

    public Player(int posX, int posY, int max_hp, int hp, int attackDamage, double attackSpeed, double critChance, String facing) {
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
            posY--;
        }
    }

    public void moveDown() {
        facing = "down";
        if (posY >= 16) {
            posY = 16;
        } else {
            posY++;
        }
    }

    public void moveRight() {
        facing = "right";
        if (posX >= 20) {
            posX = 20;
        } else {
            posX++;
        }
    }

    public void moveLeft() {
        facing = "left";
        if (posX <= 1) {
            posX = 1;
        } else {
            posX--;
        }
    }



    public void shoot() {
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public ArrayList<Projectile> getProjectiles() {
        return projectiles;
    }
}
