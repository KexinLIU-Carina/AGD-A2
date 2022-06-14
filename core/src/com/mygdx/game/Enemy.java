package com.mygdx.game;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/*
The parent class for all enemies. Inherits from the Character super class.
 */
public class Enemy extends Character {


    // ---- STATES -------------------------
    public enum EnemyState { IDLE, MOVING, JUMPING, ATTACKING, HURT, DYING, DEAD }
    public enum MovingState { WALKING, RUNNING }
    public enum AttackState { MELEE, PROJECTILE }

    private EnemyState enemyState = EnemyState.MOVING;
    private MovingState movingState = MovingState.WALKING;
    private AttackState attackState = AttackState.MELEE;

    // Stats
    private String name;

    private int walkingSpeed = 100;
    private int runningSpeed = 200;
    private int jumpingSpeed = 200;
    private int fallingSpeed = 200;

    private boolean hasRunningState;
    private boolean hasProjectile;



    public Enemy() {

        // Default states as a backup. Individual enemies should override these.
        super.setDirection(Direction.LEFT);

    }


    // Checks to see if the enemy is still alive after getting damaged. If still alive it enters the hurt state
    // otherwise it enters the dying state
    public void healthCheck(int damage) {

        if((getHealth() - damage) > 0) {
            enemyState = EnemyState.HURT;
            super.setHealth(getHealth() - damage);
        }
        else {
            enemyState = EnemyState.DYING;
            super.setHealth(0);
        }
    }

    // Resets the enemy after it is killed.
    public void reset() {
        super.setIsAlive(true);
        super.setHealth(getMax_Health());
        enemyState = EnemyState.MOVING;
        super.getSprite().setPosition(getStartPosition().x, getStartPosition().y);
    }


    // Sets the conditions to enact if an enemy has been killed by the player.
    public void killEnemy() {
        enemyState = EnemyState.DEAD;
        super.setIsAlive(false);
    }

    // A default set of states for every enemy that sets movement and applies animations
    public void switchStates(Animation<TextureRegion> idleAnimation, Animation<TextureRegion> walkingAnimation,
                             Animation<TextureRegion> hurtAnimation, Animation<TextureRegion> dyingAnimation) {

        switch (enemyState) {

            case IDLE:
                super.setCURRENT_MOVEMENT_SPEED(0);
                super.moveCharacter();
                super.loopingAnimation(idleAnimation);
                break;

            case MOVING:
                if(movingState == MovingState.WALKING) {
                    super.setCURRENT_MOVEMENT_SPEED(walkingSpeed);
                    super.moveCharacter();
                    super.loopingAnimation(walkingAnimation);
                }
                break;

            case HURT:
                super.setCURRENT_MOVEMENT_SPEED(0);
                if (super.nonLoopingAnimation(hurtAnimation)) {
                    enemyState = EnemyState.MOVING;
                }
                break;

            case DYING:
                super.setCURRENT_MOVEMENT_SPEED(0);
                if (super.nonLoopingAnimation(dyingAnimation)) {
                    killEnemy();
                }
                break;
        }
    }




    // For enemies with melee attacks, this method is only triggered when the enemy is in an attacking state.
    public void checkDamage() {
        // If the enemy has overlapped the players bounding box while attacking, it has attacked the player.
        if(getSprite().getBoundingRectangle().overlaps(GameScreen.getInstance().getPlayer().getSprite().getBoundingRectangle())) {
            if (GameScreen.getInstance().getPlayer().getIsAlive()) {
                GameScreen.getInstance().getPlayer().healthCheck(getDamage());
            }
        }
    }

    /*
    A set of default AI states that all enemies will share.
    More specific AI states can be added to in individual enemies by overriding completely or with a call to super
     */
    public void setAIStates(Player player) {

        if (player.getIsAlive()) {

            if (hasRunningState && distanceFromPlayer(player) < 900) {
                movingState = MovingState.RUNNING;
            } else {
                movingState = MovingState.WALKING;
            }

            // If the enemy is close enough to melee attack.
            if (distanceFromPlayer(player) < 200) {
                if (attackState == AttackState.MELEE) {
                    enemyState = EnemyState.ATTACKING;
                }
            }
            // The enemy is meant to turn around if the player goes past it.
            // ** Bugs ** At the moment with the camera movement this is prevented from happening
            if (player.getCenteredSpritePosition().x > getCenteredSpritePosition().x && distanceFromPlayer(player) < 1000) {
                setDirection(Direction.RIGHT);
            } else {
                setDirection(Direction.LEFT);
            }

            // If the enemy goes off screen it is reset.
            if (super.getCenteredSpritePosition().x < -100) {
                reset();
            }
        }
    }



    // ---------- GETTERS AND SETTERS -------------------------------------
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public EnemyState getEnemyState() { return enemyState; }

    public void setEnemyState(EnemyState enemyState) { this.enemyState = enemyState; }

    public MovingState getMovingState() { return movingState; }

    public void setMovingState(MovingState movingState) { this.movingState = movingState; }

    public AttackState getAttackState() { return attackState; }

    public void setAttackState(AttackState attackState) { this.attackState = attackState; }

    public int getWalkingSpeed() { return walkingSpeed; }

    public void setWalkingSpeed(int walkingSpeed) { this.walkingSpeed = walkingSpeed; }

    public int getRunningSpeed() { return runningSpeed; }

    public void setRunningSpeed(int runningSpeed) { this.runningSpeed = runningSpeed; }

    public int getJumpingSpeed() { return jumpingSpeed; }

    public void setJumpingSpeed(int jumpingSpeed) { this.jumpingSpeed = jumpingSpeed; }

    public int getFallingSpeed() { return fallingSpeed; }

    public void setFallingSpeed(int fallingSpeed) { this.fallingSpeed = fallingSpeed; }

    public boolean getHasRunningState() { return hasRunningState; }

    public void setHasRunningState(boolean hasRunningState) { this.hasRunningState = hasRunningState; }

    public boolean getHasProjectile() { return hasProjectile; }

    public void setHasProjectile(boolean hasProjectile) { this.hasProjectile = hasProjectile; }
}
