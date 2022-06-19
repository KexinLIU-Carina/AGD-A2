package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


/**
 * Devil guy has a melee attack
 */
public class EnemyDevilGuy extends Enemy {


    // ---- ANIMATIONS -------------------------
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> walkingAnimation;
    private Animation<TextureRegion> runningAnimation;
    private Animation<TextureRegion> attackingAnimation;
    private Animation<TextureRegion> hurtAnimation;
    private Animation<TextureRegion> dyingAnimation;



    public EnemyDevilGuy() {

        super.setName("DevilGuy");

        // Start offscreen right
        super.getStartPosition().x = Gdx.graphics.getWidth() + 100;
        super.getSprite().setX(getStartPosition().x);

        super.setAttackState(AttackState.MELEE);
        super.setHasRunningState(true);



        // ---- ANIMATIONS -------------------------
        idleAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Devil Masked Guy/Idle.png", 3, 6, 18);
        walkingAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Devil Masked Guy/Walking.png", 4, 6, 24);
        runningAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Devil Masked Guy/Running.png", 4, 3, 12);
        attackingAnimation = GameScreen.getInstance().getHelper().processAnimation( "Game Characters/Enemies/Devil Masked Guy/Attacking.png", 3, 4, 12);
        hurtAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Devil Masked Guy/Hurt.png", 4, 3, 12);
        dyingAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Devil Masked Guy/Dying.png", 3, 4, 12);

    }


    @Override
    public void act(float delta) {

        switchCustomStates();
    }


    /*
     First handles default states with a call to super.switchStates in Enemy class.
     Then custom states are specified. Not all enemies have these states, they are specific to the enemy.
     */
    public void switchCustomStates() {

        // Switch states in Enemy class has a set of default behaviours for standard animations.
        super.switchStates(idleAnimation, walkingAnimation, hurtAnimation, dyingAnimation);


        // --- Custom states ------

        if(super.getEnemyState() == EnemyState.MOVING) {
            if (super.getMovingState() == MovingState.RUNNING) {
                super.setCURRENT_MOVEMENT_SPEED(getRunningSpeed());
                super.moveCharacter();
                super.loopingAnimation(runningAnimation);
            }
        }

        if(super.getEnemyState() == EnemyState.ATTACKING) {
            super.setCURRENT_MOVEMENT_SPEED(0);
            if (super.nonLoopingAnimation(attackingAnimation)) {
                checkDamage();
                setEnemyState(EnemyState.MOVING);
            }
        }
    }
}
