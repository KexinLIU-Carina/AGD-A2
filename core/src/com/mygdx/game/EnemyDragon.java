package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;



public class EnemyDragon extends Enemy implements CharacterInterface {


    private Projectile dragonProjectile;


    // ---- ANIMATIONS -------------------------
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> walkingAnimation;
    private Animation<TextureRegion> attackingAnimation;
    private Animation<TextureRegion> hurtAnimation;
    private Animation<TextureRegion> dyingAnimation;

    private float stateTime;



    public EnemyDragon() {

        // Set stats
        setName("Dragon");
        getSprite().setSize(100, 100);
        getStartPosition().set(Gdx.graphics.getWidth() + 100, 120);
        setHasProjectile(true);

        setWalkingSpeed(-50);


        // Initialize Projectile
        dragonProjectile = new Projectile("Game Objects/DragonProjectile.png", "Audio/Sounds/shot.mp3");
        dragonProjectile.getProjectileSprite().setSize(40, 20);
        dragonProjectile.getProjectileStartPosition().x = getSprite().getX();
        dragonProjectile.getProjectileStartPosition().y = getSprite().getY();
        dragonProjectile.getOffset().set(105, 65);
        dragonProjectile.getProjectileSprite().setPosition(dragonProjectile.getProjectileStartWithOffset().x, dragonProjectile.getProjectileStartWithOffset().y);

        dragonProjectile.setMovementSpeedX(350f);
        dragonProjectile.setMovementSpeedY(-50f);


        // Load all animation frames into animation objects using Game Helper.
        idleAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Cartoon Dragon 01/Idle.png", 3, 6, 18);
        walkingAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Cartoon Dragon 01/Walking.png", 3, 6, 18);
        attackingAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Cartoon Dragon 01/Attacking.png", 3, 6, 18);
        hurtAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Cartoon Dragon 01/Hurt.png", 3, 6, 18);
        dyingAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Cartoon Dragon 01/Dying.png", 3, 3, 9);

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
    public void draw(Batch batch, float alpha) {
        // // Draws enemy sprite facing left
        if(!getCurrentFrame().isFlipX()) {
            getCurrentFrame().flip(true, false);
        }

        batch.draw(getCurrentFrame(), getSprite().getX(), getSprite().getY(), getSprite().getWidth(), getSprite().getHeight());

        // Draw the projectile if the enemy is attacking
        if(getEnemyState() == EnemyState.ATTACKING) {
            dragonProjectile.draw(batch, alpha);
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
