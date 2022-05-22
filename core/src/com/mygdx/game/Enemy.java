package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;



public class Enemy extends Character implements CharacterInterface {


    // ---- CHARACTER STATS -------------------------
    public enum EnemyState { IDLE, WALKING, RUNNING, ATTACKING, THROWING, HURT, DYING, DEAD }

    private EnemyState enemyState;

    private String name;

    // Not all enemies have the same states, this boolean acts as a check to make sure that an enemy has the state before it accesses it.
    private boolean[] hasState;
    private boolean hasProjectile = false;


    public Enemy() {
        hasState = new boolean[3];
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

    public void reset() {}

    public void switchStates() {}



    // ---------- GETTERS AND SETTERS -------------------------------------
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public EnemyState getEnemyState() { return enemyState; }

    public void setEnemyState(EnemyState enemyState) { this.enemyState = enemyState; }

    public boolean[] getHasState() { return hasState; }

    public boolean getHasProjectile() { return hasProjectile; }

    public void setHasProjectile(boolean hasProjectile) { this.hasProjectile = hasProjectile; }
}
