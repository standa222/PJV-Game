package cz.cvut.fel.pjv.game.model;

import java.util.ArrayList;

public class Enemy {
    private int posX;
    private int posY;
    private int hp;
    private int attackDamage;
    private double attackSpeed;
    private String drop;
    private ArrayList<Projectile> projectiles;

    public Enemy(int posX, int posY, int hp, int attackDamage, double attackSpeed, String drop) {
        this.posX = posX;
        this.posY = posY;
        this.hp = hp;
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.drop = drop;
        projectiles = new ArrayList<>();
    }
}
