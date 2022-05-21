package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;



public class EnemyDragon extends Enemy implements CharacterInterface {

    private enum State { IDLE, WALKING, ATTACKING, HURT, DYING, DEAD }

    private State state = State.WALKING;
    private Projectile dragonProjectile;

    // Animations
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> walkingAnimation;
    private Animation<TextureRegion> attackingAnimation;
    private Animation<TextureRegion> hurtAnimation;
    private Animation<TextureRegion> dyingAnimation;

    private float stateTime;



    public EnemyDragon() {

        setName("Dragon");
        setType(EnemyType.GROUND);
        getStartPosition().set(Gdx.graphics.getWidth() + 100, 200);
        setHasProjectile(true);

        setWalkingSpeed(100);


        // Initialize Projectile
        dragonProjectile = new Projectile("Game Objects/Character Objects/DragonProjectile.png", "Audio/Sounds/shot.mp3", 105, 65, -350f, -50f);
        dragonProjectile.getProjectileSprite().setSize(40, 20);

        // Load all animation frames into animation objects using Game Helper.
        idleAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Idle.png", 3, 6, 18);
        walkingAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Walking.png", 3, 6, 18);
        attackingAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Attacking.png", 3, 6, 18);
        hurtAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Hurt.png", 3, 6, 18);
        dyingAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Dying.png", 3, 3, 9);

    }


    @Override
    public void healthCheck(int damage) {
        if((getHealth() - damage) > 0) {
            state = State.HURT;
            setHealth(getHealth() - damage);
        }
        else {
            state = State.DYING;
            setHealth(0);
        }
    }

    @Override
    public void reset() {
        setIsAlive(true);
        setHealth(getMax_Health());
        state = State.WALKING;
        getSprite().setPosition(getStartPosition().x, getStartPosition().y);
    }

    @Override
    public void act(float delta) {

        setStateTime(stateTime += delta);
        setState();
    }


    @Override
    public void setState() {

        switch (state) {
            case IDLE:
                setCURRENT_MOVEMENT_SPEED(0);
                setCurrentFrame(idleAnimation.getKeyFrame(getStateTime(), true));
                break;

            case WALKING:
                setCURRENT_MOVEMENT_SPEED(getWalkingSpeed());
                setCurrentFrame(walkingAnimation.getKeyFrame(getStateTime(), true));
                break;

            case ATTACKING:
                setCURRENT_MOVEMENT_SPEED(0);
                if(setAnimationFrame(attackingAnimation)) {
                    state = State.IDLE;
                }
                break;

            case HURT:
                setCURRENT_MOVEMENT_SPEED(0);
                if(setAnimationFrame(hurtAnimation)) {
                    state = State.IDLE;
                }
                break;

            case DYING:
                setCURRENT_MOVEMENT_SPEED(0);
                if (setAnimationFrame(dyingAnimation)) {
                    setIsAlive(false);
                    state = State.DEAD;
                }
                break;
        }
    }
}
