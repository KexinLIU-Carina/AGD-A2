package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;



public class EnemyRobot extends Enemy implements CharacterInterface {


    // ---- ANIMATIONS -------------------------
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> walkingAnimation;
    private Animation<TextureRegion> runningAnimation;
    private Animation<TextureRegion> attackingAnimation;
    private Animation<TextureRegion> hurtAnimation;
    private Animation<TextureRegion> dyingAnimation;

    private float stateTime;



    public EnemyRobot() {

        // Set stats
        setName("Robot");
        getSprite().setSize(100, 100);
        getStartPosition().set(Gdx.graphics.getWidth() + 100, 120);
        setHasProjectile(false);

        setWalkingSpeed(-100);
        setRunningSpeed(-100);


        // Load all animation frames into animation objects using Game Helper.
        idleAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Giant Robot/Idle.png", 6, 3, 18);
        walkingAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Giant Robot/Walking.png", 6, 4, 24);
        runningAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Giant Robot/Running.png", 3, 6, 18);
        attackingAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Giant Robot/Attacking.png", 4, 3, 12);
        hurtAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Giant Robot/Hurt.png", 3, 4, 12);
        dyingAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Giant Robot/Dying.png", 3, 4, 12);

        getHasState()[0] = true;
        getHasState()[1] = true;
        getHasState()[2] = false;
    }


    // Resets the enemy after it is killed.
    @Override
    public void reset() {
        setIsAlive(true);
        setHealth(getMax_Health());
        setEnemyState(EnemyState.WALKING);
        getSprite().setPosition(getStartPosition().x, getStartPosition().y);
        Gdx.app.log("Move", "reset: ");
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
