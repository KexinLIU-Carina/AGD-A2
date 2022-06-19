package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


/**
 * Yeti has a projectile attack and a melee attack
 */
public class EnemyYeti extends Enemy {


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
        super.getStartPosition().x = Gdx.graphics.getWidth() + 100f;
        super.getSprite().setX(getStartPosition().x);

        super.setHasRunningState(true);

        setHasProjectile(true);
        super.setAttackState(AttackState.PROJECTILE);


        // ---- PROJECTILE -------------------------
        // Initialize Projectile
        yetiProjectile = new Projectile("Game Objects/Cartoon Yeti_Snow Ball.png", "Audio/Sounds/shot.mp3");
        yetiProjectile.getProjectileSprite().setSize(70f, 50f);

        yetiProjectile.setMovementSpeedX(400f);



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


    // Additional conditions needed for projectiles. This is handled by overriding the draw method inherited from Enemy super class,
    // calling the super draw method, and then providing the additional conditions.
    @Override
    public void draw(Batch batch, float alpha) {

        super.draw(batch, alpha);

        if(yetiProjectile.getProjectileState() == Projectile.ProjectileState.RESET) {
            // Set the projectile to be launched in the same direction the character is facing. Reverses the offset and speed accordingly.
            if (super.getDirection() == Direction.LEFT) {
                yetiProjectile.getOffset().set(0f, 150f);
                yetiProjectile.setDirection(Direction.LEFT);
            }
            if (super.getDirection() == Direction.RIGHT) {
                yetiProjectile.getOffset().set(200f, 150f);
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

        switchCustomStates();

        // Updates the projectile to emit from wherever the character is.
        yetiProjectile.getProjectileStartPosition().x = getSprite().getX();
        yetiProjectile.getProjectileStartPosition().y = getSprite().getY();
        yetiProjectile.act(delta);
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

            if (super.getAttackState() == AttackState.MELEE) {
                // If the animation has finished
                if (super.nonLoopingAnimation(meleeAnimation)) {
                    checkDamage();
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


    // Adds additional AI states specific to this enemy, primarily its Attack state
    @Override
    public void setAIStates(Player player) {

        super.setAIStates(player);

        // The yeti has a projectile attack and a melee attack. If it is far enough away from the player it uses the projectile attack.
        if (distanceFromPlayer(player) > 700 && distanceFromPlayer(player) < 1000) {
            setAttackState(AttackState.PROJECTILE);
            if(yetiProjectile.getProjectileState() == Projectile.ProjectileState.RESET) {
                setEnemyState(EnemyState.ATTACKING);
            }
        }
        else {
            // If it is close enough to the player, it uses the melee attack
            setAttackState(AttackState.MELEE);
        }

        if(yetiProjectile.getProjectileSprite().getBoundingRectangle().overlaps(GameScreen.getInstance().getPlayer().getSprite().getBoundingRectangle())) {
            if(yetiProjectile.getProjectileState() == Projectile.ProjectileState.FIRING) {
                if(GameScreen.getInstance().getPlayer().getIsAlive()) {
                    // If the projectile has overlapped the players bounding box, it has hit the player and does damage.
                    GameScreen.getInstance().getPlayer().healthCheck(getDamage());
                    yetiProjectile.setProjectileState(Projectile.ProjectileState.RESET);
                    yetiProjectile.switchState();
                }
            }
        }
    }

    @Override
    public Projectile getEnemyProjectile() {
        return yetiProjectile;
    }
}
