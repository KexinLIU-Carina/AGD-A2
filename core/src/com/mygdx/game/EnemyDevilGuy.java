package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;



public class EnemyDevilGuy extends Enemy implements CharacterInterface {


    // ---- ANIMATIONS -------------------------
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> walkingAnimation;
    private Animation<TextureRegion> runningAnimation;
    private Animation<TextureRegion> attackingAnimation;
    private Animation<TextureRegion> hurtAnimation;
    private Animation<TextureRegion> dyingAnimation;

    private float stateTime;



    public EnemyDevilGuy() {

        // Set stats
        setName("DevilGuy");
        getSprite().setSize(100, 100);
        getStartPosition().set(Gdx.graphics.getWidth() + 100, 120);
        setHasProjectile(false);

        setWalkingSpeed(-50);
        setRunningSpeed(-100);


        // Load all animation frames into animation objects using Game Helper.
        idleAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Devil Masked Guy/Idle.png", 3, 6, 18);
        walkingAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Devil Masked Guy/Walking.png", 4, 6, 24);
        runningAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Devil Masked Guy/Running.png", 4, 3, 12);
        attackingAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Devil Masked Guy/Attacking.png", 3, 4, 12);
        hurtAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Devil Masked Guy/Hurt.png", 4, 3, 12);
        dyingAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Devil Masked Guy/Dying.png", 3, 4, 12);

        // Set which states that the enemy has
        setHasRunningState(true);
        setHasThrowingState(false);
    }


    // Resets the enemy after it is killed.
    @Override
    public void reset() {
        setIsAlive(true);
        setHealth(getMax_Health());
        setEnemyState(EnemyState.WALKING);
        getSprite().setPosition(getStartPosition().x, getStartPosition().y);
    }


    @Override
    public void act(float delta) {

        stateTime = getStateTime();
        setStateTime(stateTime += delta);
        switchStates();
    }


    // Controls the animations that are performed in different states as well as applies any additional conditions to the states.
    @Override
    public void switchStates() {

        switch (getEnemyState()) {
            case IDLE:
                setCURRENT_MOVEMENT_SPEED(0);
                setCurrentFrame(idleAnimation.getKeyFrame(getStateTime(), true));
                break;

            case WALKING:
                setCURRENT_MOVEMENT_SPEED(getWalkingSpeed());
                setCurrentFrame(walkingAnimation.getKeyFrame(getStateTime(), true));
                getPositionAmount().x = GameScreen.getInstance().getHelper().setMovement(getCURRENT_MOVEMENT_SPEED());
                getSprite().translate(getPositionAmount().x, getPositionAmount().y);
                break;

            case RUNNING:
                setCURRENT_MOVEMENT_SPEED(getRunningSpeed());
                setCurrentFrame(runningAnimation.getKeyFrame(getStateTime(), true));
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
                    setEnemyState(EnemyState.IDLE);
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