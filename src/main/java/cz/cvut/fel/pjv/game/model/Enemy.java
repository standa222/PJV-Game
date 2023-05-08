package cz.cvut.fel.pjv.game.model;

import java.util.ArrayList;
import java.util.Random;

public class Enemy {
    private double posX;
    private double posY;
    private int hp;
    private int attackDamage;
    private double attackSpeed;
    private String drop;
    private ArrayList<Projectile> projectiles;
    private boolean isDead;
    private int updateCounter = 0;
    private int shootingCounter = 0;
    private double[] movementSpeed = {0, 0};
    private String direction = "down";

    public Enemy(int posX, int posY, int hp, int attackDamage, double attackSpeed, String drop) {
        this.posX = posX;
        this.posY = posY;
        this.hp = hp;
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.drop = drop;
        projectiles = new ArrayList<>();
        isDead = false;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public ArrayList<Projectile> getProjectiles() {
        return projectiles;
    }

    public void update() {
        updateCounter++;
        if (updateCounter > 120) {
            updateMovement();
            updateCounter = 0;
        }
    }

    public void updateMovement() {
        Random random = new Random();
        int rand = random.nextInt(7);
        switch (rand) {
            case 1:
                setMovementSpeed(0, -0.035);
                break;
            case 2:
                setMovementSpeed(0, 0.035);
                break;
            case 3:
                setMovementSpeed(0.035, 0);
                break;
            case 4:
                setMovementSpeed(-0.035, 0);
                break;
            default:
                setMovementSpeed(0, 0);
        }
    }

    public void shoot(String projectileDirection) {
        shootingCounter++;
        if (shootingCounter > 120 / attackSpeed) {
            Projectile p = new Projectile(posX, posY, this.getClass(), projectileDirection);
            projectiles.add(p);
            shootingCounter = 0;
        }
    }


    public String getDirection() {
        return direction;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getHp() {
        return hp;
    }

    public double[] getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(double speedX, double speedY) {
        movementSpeed = new double[]{speedX, speedY};
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public String getDrop() {
        return drop;
    }

    public int getAttackDamage() {
        return attackDamage;
    }
}
