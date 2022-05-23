package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;



public class EnemyYeti extends Enemy implements CharacterInterface {


    private Projectile yetiProjectile;


    // ---- ANIMATIONS -------------------------
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> walkingAnimation;
    private Animation<TextureRegion> runningAnimation;
    private Animation<TextureRegion> attackingAnimation;
    private Animation<TextureRegion> throwingAnimation;
    private Animation<TextureRegion> hurtAnimation;
    private Animation<TextureRegion> dyingAnimation;

    private float stateTime;



    public EnemyYeti() {

        // Set stats
        setName("Yeti");
        getSprite().setSize(100, 100);

        // Start offscreen right
        getStartPosition().set(Gdx.graphics.getWidth() + 100, 120);

        setWalkingSpeed(-100);
        setRunningSpeed(-100);

        setHasProjectile(true);


        // Initialize Projectile
        yetiProjectile = new Projectile("Game Objects/Cartoon Yeti_Snow Ball.png", "Audio/Sounds/shot.mp3");
        yetiProjectile.getProjectileSprite().setSize(40, 20);

        yetiProjectile.getProjectileStartPosition().x = getSprite().getX();
        yetiProjectile.getProjectileStartPosition().y = getSprite().getY();
        yetiProjectile.getOffset().set(105, 65);
        yetiProjectile.getProjectileSprite().setPosition(yetiProjectile.getProjectileStartWithOffset().x, yetiProjectile.getProjectileStartWithOffset().y);

        yetiProjectile.setMovementSpeedX(350f);
        yetiProjectile.setMovementSpeedY(-50f);


        // Load all animation frames into animation objects using Game Helper.
        idleAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Cartoon Yeti/Idle.png", 6, 3, 18);
        walkingAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Cartoon Yeti/Walking.png", 5, 4, 18);
        runningAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Cartoon Yeti/Running.png", 5, 3, 15);
        attackingAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Cartoon Yeti/Attacking.png", 4, 3, 12);
        throwingAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Cartoon Yeti/Throwing.png", 4, 3, 12);
        hurtAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Cartoon Yeti/Hurt.png", 4, 3, 12);
        dyingAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Cartoon Yeti/Dying.png", 4, 3, 12);

        // Set which states that the enemy has
        setHasRunningState(true);
        setHasThrowingState(true);
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
    public void draw(Batch batch, float alpha) {
        // Draws enemy sprite facing left
        if(!getCurrentFrame().isFlipX()) {
            getCurrentFrame().flip(true, false);
        }
        batch.draw(getCurrentFrame(), getSprite().getX(), getSprite().getY(), getSprite().getWidth(), getSprite().getHeight());

        // Draw the projectile if the enemy is attacking
        if(getEnemyState() == EnemyState.THROWING) {
            yetiProjectile.draw(batch, alpha);
        }
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

            case THROWING:
                setCURRENT_MOVEMENT_SPEED(0);
                if(setAnimationFrame(throwingAnimation)) {
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
