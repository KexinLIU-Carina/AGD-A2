package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;



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

        super.setAttackState(AttackState.PROJECTILE);
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
     Calls switchStates in Enemy class to handle default states then provides the ability to specify custom states.
     These states might be unique to the enemy or require more functionality than the default..
     */
    public void switchCustomStates() {

        // Switch states in Enemy class has a set of default behaviours for standard animations.
        super.switchStates(idleAnimation, walkingAnimation, hurtAnimation, dyingAnimation);

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
