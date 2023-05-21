package cz.cvut.fel.pjv.game.model;

import java.util.ArrayList;
import java.util.Random;

public class Enemy {
    private double posX;
    private double posY;
    private int hp;
    private final int attackDamage;
    private final double attackSpeed;
    private final String drop;
    private final ArrayList<Projectile> projectiles;
    private boolean isDead;
    private int updateCounter = 0;
    private int shootingCounter = 0;
    private double[] movementSpeed = {0, 0};

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

    /**
     * Updates the state of the enemy.
     * Increments the update counter and checks if it has reached the threshold.
     * If the counter exceeds the threshold, it calls the updateMovement method and resets the counter.
     */
    public void update() {
        updateCounter++;
        if (updateCounter > 120) {
            updateMovement();
            updateCounter = 0;
        }
    }

    /**
     * Updates the movement speed of the enemy.
     * Generates a random number between 0 and 6 and sets the movement speed based on the random value.
     * Possible movement directions are up, down, left, right, or no movement.
     */
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

    /**
     * Increments the counter and checks if it has reached the threshold.
     * If the counter exceeds the threshold, it creates a new projectile and resets the counter.
     *
     * @param projectileDirection The direction in which the projectile will be shot.
     */
    public void shoot(String projectileDirection) {
        shootingCounter++;
        if (shootingCounter > 120 / attackSpeed) {
            Projectile p = new Projectile(posX, posY, "enemy", projectileDirection);
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

    public ArrayList<Projectile> getProjectiles() {
        return projectiles;
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
