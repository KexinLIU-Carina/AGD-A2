package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;



public class EnemyWolf extends Enemy implements CharacterInterface {


    // ---- ANIMATIONS -------------------------
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> walkingAnimation;
    private Animation<TextureRegion> runningAnimation;
    private Animation<TextureRegion> attackingAnimation;
    private Animation<TextureRegion> hurtAnimation;
    private Animation<TextureRegion> dyingAnimation;

    // The stateTime used for looping animations
    private float stateTime;



    public EnemyWolf() {

        // Set stats
        setName("Wolf");
        getSprite().setSize(100, 100);
        // Start offscreen right
        getStartPosition().set(Gdx.graphics.getWidth() + 100, 120);
        setHasProjectile(false);

        setWalkingSpeed(-100);
        setRunningSpeed(-100);


        // Load all animation frames into animation objects using Game Helper.
        idleAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Tiny Wolf 02/Idle.png", 4, 3, 12);
        walkingAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Tiny Wolf 02/Walking.png", 3, 6, 18);
        runningAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Tiny Wolf 02/Running.png", 5, 2, 10);
        attackingAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Tiny Wolf 02/Attacking.png", 4, 3, 12);
        hurtAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Tiny Wolf 02/Hurt.png", 4, 3, 12);
        dyingAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Tiny Wolf 02/Dying.png", 4, 3, 12);

        // Set which states that the enemy has
        setHasRunningState(true);
        setHasThrowingState(false);
    }


    @Override
    public void act(float delta) {

        stateTime += delta;
        switchStates();
    }


    // Controls the animations that are performed in different states as well as applies any additional conditions to the states.
    @Override
    public void switchStates() {

        switch (getEnemyState()) {
            case IDLE:
                setCURRENT_MOVEMENT_SPEED(0);
                setCurrentFrame(idleAnimation.getKeyFrame(stateTime, true));
                break;

            case WALKING:
                setCURRENT_MOVEMENT_SPEED(getWalkingSpeed());
                setCurrentFrame(walkingAnimation.getKeyFrame(stateTime, true));
                getPositionAmount().x = GameScreen.getInstance().getHelper().setMovement(getCURRENT_MOVEMENT_SPEED());
                getSprite().translate(getPositionAmount().x, getPositionAmount().y);
                break;

            case RUNNING:
                setCURRENT_MOVEMENT_SPEED(getRunningSpeed());
                setCurrentFrame(runningAnimation.getKeyFrame(stateTime, true));
                getPositionAmount().x = GameScreen.getInstance().getHelper().setMovement(getCURRENT_MOVEMENT_SPEED());
                getSprite().translate(getPositionAmount().x, getPositionAmount().y);
                break;

            case ATTACKING:
                setCURRENT_MOVEMENT_SPEED(0);
                if(setAnimationFrame(attackingAnimation)) {
                    setEnemyState(EnemyState.IDLE);
                }
                break;

            case HURT:
                setCURRENT_MOVEMENT_SPEED(0);
                if(setAnimationFrame(hurtAnimation)) {
                    setEnemyState(EnemyState.WALKING);
                }
                break;

            case DYING:
                setCURRENT_MOVEMENT_SPEED(0);
                if (setAnimationFrame(dyingAnimation)) {
                    setIsAlive(false);
                    setEnemyState(EnemyState.DEAD);
                }
                break;
        }
    }
}
