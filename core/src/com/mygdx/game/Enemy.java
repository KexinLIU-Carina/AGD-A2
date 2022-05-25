package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;



public class Enemy extends Character implements CharacterInterface {


    // ---- CHARACTER STATS -------------------------
    public enum EnemyState { IDLE, WALKING, RUNNING, ATTACKING, THROWING, HURT, DYING, DEAD }

    private EnemyState enemyState = EnemyState.WALKING;

    private String name;

    // Not all enemies have the same states, these booleans act as a check to make sure that an enemy has the state before it accesses it.
    private boolean hasRunningState;
    private boolean hasThrowingState;
    private boolean hasProjectile = false;


    // The default drawing method for Enemies. Enemies with projectiles override this method.
    @Override
    public void draw(Batch batch, float alpha) {
        // Draws enemy sprite facing left
        if (!getCurrentFrame().isFlipX()) {
            getCurrentFrame().flip(true, false);
        }

        batch.draw(getCurrentFrame(), getSprite().getX(), getSprite().getY(), getSprite().getWidth(), getSprite().getHeight());
    }

    // Checks to see if the enemy is still alive after getting damaged. If still alive it enters the hurt state
    // otherwise it enters the dying state
    public void healthCheck(int damage) {

        if((getHealth() - damage) > 0) {
            enemyState = EnemyState.HURT;
            setHealth(getHealth() - damage);
        }
        else {
            enemyState = EnemyState.DYING;
            setHealth(0);
        }
    }

    // Resets the enemy after it is killed.
    @Override
    public void reset() {
        setIsAlive(true);
        setHealth(getMax_Health());
        setEnemyState(EnemyState.WALKING);
        getSprite().setPosition(getStartPosition().x, getStartPosition().y);
    }

    public void switchStates() {}



    // ---------- GETTERS AND SETTERS -------------------------------------
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public EnemyState getEnemyState() { return enemyState; }

    public void setEnemyState(EnemyState enemyState) { this.enemyState = enemyState; }

    public boolean getHasRunningState() { return hasRunningState; }

    public void setHasRunningState(boolean hasRunningState) { this.hasRunningState = hasRunningState; }

    public boolean getHasThrowingState() { return hasThrowingState; }

    public void setHasThrowingState(boolean hasThrowingState) { this.hasThrowingState = hasThrowingState; }

    public boolean getHasProjectile() { return hasProjectile; }

    public void setHasProjectile(boolean hasProjectile) { this.hasProjectile = hasProjectile; }

}
