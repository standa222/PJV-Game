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

    public void shoot() {
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public ArrayList<Projectile> getProjectiles() {
        return projectiles;
    }
}
