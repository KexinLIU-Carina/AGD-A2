package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;


public class Enemy extends Character implements CharacterInterface {


    // ---- CHARACTER STATS -------------------------
    public enum EnemyState { IDLE, MOVING, JUMPING, ATTACKING, HURT, DYING, DEAD }
    public enum MovingState { WALKING, RUNNING }
    public enum AttackState { MELEE, PROJECTILE }

    private EnemyState enemyState = EnemyState.MOVING;
    private MovingState movingState = MovingState.WALKING;
    private AttackState attackState = AttackState.MELEE;

    private String name;

    private int walkingSpeed = 50;
    private int runningSpeed = 100;
    private int jumpingSpeed = 100;
    private int fallingSpeed = 100;

    private boolean hasRunningState;
    private boolean hasProjectile;

    private boolean projectile = true;


    public Enemy() {

        // Default
        super.setDirection(Direction.LEFT);
        super.getSprite().setSize(100, 100);
        super.getSprite().setPosition(getStartPosition().x, getStartPosition().y);

    }




    // The default drawing method for Enemies. Enemies with projectiles override this method.
    @Override
    public void draw(Batch batch, float alpha) {
        super.draw(batch, alpha);

    }


    // Checks to see if the enemy is still alive after getting damaged. If still alive it enters the hurt state
    // otherwise it enters the dying state
    @Override
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


    public void killEnemy() {
        super.setIsAlive(false);
        enemyState = EnemyState.DEAD;
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



    public float getAngle(Vector2 target) {
        float angle = (float) Math.toDegrees(Math.atan2(target.y - getCenteredSpritePosition().y, target.x - getCenteredSpritePosition().x));

        if(angle < 0) {
            angle += 360;
        }
        return angle;
    }


    public boolean canSeePlayer(Player player) {
        float angle = getAngle(player.getCenteredSpritePosition());
        return angle > 170 && angle < 190;
    }


    public float distanceFromPlayer(Player player) {
        return super.getCenteredSpritePosition().dst(player.getCenteredSpritePosition());
    }



    public void setAIStates(Player player) {

        if(canSeePlayer(player)) {

            if(hasRunningState) {
                movingState = MovingState.RUNNING;
            }
            else {
                movingState = MovingState.WALKING;
            }

            if(distanceFromPlayer(player) < 400 && distanceFromPlayer(player) > 390) {
                if (hasProjectile) {
                    if(projectile) {
                        enemyState = EnemyState.ATTACKING;
                        projectile = false;
                    }
                }
            }

            // If the enemy is close enough to melee attack.
            if(distanceFromPlayer(player) < 50) {
                if(attackState == AttackState.MELEE) {
                    enemyState = EnemyState.ATTACKING;
                }

                if(player.getCenteredSpritePosition().x > getCenteredSpritePosition().x) {
                    setDirection(Direction.RIGHT);
                }
                else {
                    setDirection(Direction.LEFT);
                }
            }
        }

        // If the enemy cant see the play it starts walking to the left
        if(!canSeePlayer(player)) {
            super.setDirection(Direction.LEFT);
            enemyState = EnemyState.IDLE;

            // If the enemy goes off screen it is reset.
            if(super.getCenteredSpritePosition().x < -100) {
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
