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
    private Animation<TextureRegion> meleeAnimation;
    private Animation<TextureRegion> throwingAnimation;
    private Animation<TextureRegion> hurtAnimation;
    private Animation<TextureRegion> dyingAnimation;



    /*
    The Yeti has a running state and also has both a melee attack and a projectile attack.
     */
    public EnemyYeti() {

        super.setName("Yeti");

        // Start offscreen right
        super.getStartPosition().set(Gdx.graphics.getWidth() + 100, 120);
        super.getSprite().setPosition(getStartPosition().x, getStartPosition().y);

        super.setHasRunningState(true);
        super.setRunningSpeed(100);

        setHasProjectile(true);
        super.setAttackState(AttackState.PROJECTILE);


        // ---- PROJECTILE -------------------------
        // Initialize Projectile
        yetiProjectile = new Projectile("Game Objects/Cartoon Yeti_Snow Ball.png", "Audio/Sounds/shot.mp3");
        yetiProjectile.getProjectileSprite().setSize(40, 20);

        yetiProjectile.setMovementSpeedX(300f);
//        yetiProjectile.setMovementSpeedY(-50f);



        // ---- ANIMATIONS -------------------------
        // Load all animation frames into animation objects using Game Helper.
        idleAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Cartoon Yeti/Idle.png", 6, 3, 18);
        walkingAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Cartoon Yeti/Walking.png", 5, 4, 18);
        runningAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Cartoon Yeti/Running.png", 5, 3, 15);
        meleeAnimation = GameScreen.getInstance().getHelper().processAnimation( "Game Characters/Enemies/Cartoon Yeti/Attacking.png", 4, 3, 12);
        throwingAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Cartoon Yeti/Throwing.png", 4, 3, 12);
        hurtAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Cartoon Yeti/Hurt.png", 4, 3, 12);
        dyingAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Cartoon Yeti/Dying.png", 4, 3, 12);

    }


    // Has projectile so overrides draw method inherited from Enemy super class.
    @Override
    public void draw(Batch batch, float alpha) {

        super.draw(batch, alpha);

        if(yetiProjectile.getProjectileState() == Projectile.ProjectileState.RESET) {
            // Set the projectile to be launched in the same direction the character is facing. Reverses the offset and speed accordingly.
            if (super.getDirection() == Direction.LEFT) {
                yetiProjectile.getOffset().set(-10, 50);
                yetiProjectile.setDirection(Direction.LEFT);
            }
            if (super.getDirection() == Direction.RIGHT) {
                yetiProjectile.getOffset().set(100, 50);
                yetiProjectile.setDirection(Direction.RIGHT);
            }
        }
        // Draw the projectile if the enemy is attacking
        if(yetiProjectile.getProjectileState() == Projectile.ProjectileState.FIRING) {
            yetiProjectile.draw(batch, alpha);
        }
    }

    @Override
    public void act(float delta) {

        applyCustomStates();

        // Updates the projectile to emit from wherever the character is.
        yetiProjectile.getProjectileStartPosition().x = getSprite().getX();
        yetiProjectile.getProjectileStartPosition().y = getSprite().getY();
        yetiProjectile.act(delta);
    }


    /*
     Calls switchStates in Enemy class to handle default states then provides the ability to specify custom states.
     These states might be unique to the enemy or require more functionality than the default..
     */
    public void applyCustomStates() {

        // Switch states in Enemy class has a set of default behaviours for standard animations.
        super.switchStates(idleAnimation, walkingAnimation, hurtAnimation, dyingAnimation);

        if(super.getEnemyState() == EnemyState.MOVING) {
            if (super.getMovingState() == MovingState.RUNNING) {
                super.setCURRENT_MOVEMENT_SPEED(getRunningSpeed());
                super.moveCharacter();
                super.loopingAnimation(runningAnimation);
            }

            if(super.getEnemyState() == EnemyState.ATTACKING) {
                super.setCURRENT_MOVEMENT_SPEED(0);

                if (super.getAttackState() == AttackState.MELEE) {
                    // If the animation has finished
                    if (super.nonLoopingAnimation(meleeAnimation)) {
                        // Set the state to enter into after animation has played.
                        setEnemyState(EnemyState.MOVING);
                    }
                }
                if (super.getAttackState() == AttackState.PROJECTILE) {
                    // If the animation has finished
                    if (super.nonLoopingAnimation(throwingAnimation)) {
                        // Set the state to enter into after animation has played.
                        setEnemyState(EnemyState.MOVING);
                    }
                    yetiProjectile.setProjectileState(Projectile.ProjectileState.FIRING);
                }
            }
        }
    }


    @Override
    public void setAIStates(Player player) {

        super.setAIStates(player);

        if(distanceFromPlayer(player) < 50) {
            setAttackState(AttackState.MELEE);
        }

        if(yetiProjectile.getProjectileSprite().getBoundingRectangle().overlaps(player.getSprite().getBoundingRectangle())) {
            player.healthCheck(getDamage());
        }
    }
}
