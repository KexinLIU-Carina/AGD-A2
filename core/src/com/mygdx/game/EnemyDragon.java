package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


/**
 * Dragon has a projectile attack
 */
public class EnemyDragon extends Enemy {


    private Projectile dragonProjectile;


    // ---- ANIMATIONS -------------------------
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> walkingAnimation;
    private Animation<TextureRegion> attackingAnimation;
    private Animation<TextureRegion> hurtAnimation;
    private Animation<TextureRegion> dyingAnimation;



    public EnemyDragon() {
        super.setName("Dragon");

        // Start offscreen right
        super.getStartPosition().x = Gdx.graphics.getWidth() + 100f;
        super.getSprite().setX(getStartPosition().x);

        super.setHasProjectile(true);
        super.setAttackState(AttackState.PROJECTILE);


        // ---- PROJECTILE -------------------------
        // Initialize Projectile
        dragonProjectile = new Projectile("Game Objects/DragonProjectile.png", "Audio/Sounds/shot.mp3");
        dragonProjectile.getProjectileSprite().setSize(70f, 50f);

        dragonProjectile.setMovementSpeedX(400f);



        // ---- ANIMATIONS -------------------------
        idleAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Cartoon Dragon 01/Idle.png", 3, 6, 18);
        walkingAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Cartoon Dragon 01/Walking.png", 3, 6, 9);
        attackingAnimation = GameScreen.getInstance().getHelper().processAnimation( "Game Characters/Enemies/Cartoon Dragon 01/Attacking.png", 3, 6, 18);
        hurtAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Cartoon Dragon 01/Hurt.png", 3, 6, 18);
        dyingAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Enemies/Cartoon Dragon 01/Dying.png", 3, 3, 9);

    }


    // Has projectile so overrides draw method inherited from Enemy super class.
    @Override
    public void draw(Batch batch, float alpha) {

        super.draw(batch, alpha);

        if(dragonProjectile.getProjectileState() == Projectile.ProjectileState.RESET) {
            // Set the projectile to be launched in the same direction the character is facing. Reverses the offset and speed accordingly.
            if (super.getDirection() == Direction.LEFT) {
                dragonProjectile.getOffset().set(0, 100);
                dragonProjectile.setDirection(Direction.LEFT);
            }
            if (super.getDirection() == Direction.RIGHT) {
                dragonProjectile.getOffset().set(200, 100);
                dragonProjectile.setDirection(Direction.RIGHT);
            }
        }

        // Draw the projectile if the enemy is attacking
        if(dragonProjectile.getProjectileState() == Projectile.ProjectileState.FIRING) {
            dragonProjectile.draw(batch, alpha);
        }
    }


    @Override
    public void act(float delta) {

        switchCustomStates();

        // Updates the projectile to emit from wherever the character is.
        dragonProjectile.getProjectileStartPosition().x = getSprite().getX();
        dragonProjectile.getProjectileStartPosition().y = getSprite().getY();
        dragonProjectile.act(delta);
    }


    /*
     First handles default states with a call to super.switchStates in Enemy class.
     Then custom states are specified. Not all enemies have these states, they are specific to the enemy.
     */
    public void switchCustomStates() {

        // Switch states in Enemy class has a set of default behaviours for standard animations.
        super.switchStates(idleAnimation, walkingAnimation, hurtAnimation, dyingAnimation);


        // --- Custom states ------

        if(super.getEnemyState() == EnemyState.ATTACKING) {
            super.setCURRENT_MOVEMENT_SPEED(0);
            if (super.nonLoopingAnimation(attackingAnimation)) {
                setEnemyState(EnemyState.MOVING);
            }
            dragonProjectile.setProjectileState(Projectile.ProjectileState.FIRING);
        }
    }


    @Override
    public void setAIStates(Player player) {

        super.setAIStates(player);

        // The dragon fires at the enemy wherever it is
        if (distanceFromPlayer(player) < 1000 && distanceFromPlayer(player) > 500) {
            if (dragonProjectile.getProjectileState() == Projectile.ProjectileState.RESET) {
                setEnemyState(EnemyState.ATTACKING);
            }
        }

        if(dragonProjectile.getProjectileSprite().getBoundingRectangle().overlaps(GameScreen.getInstance().getPlayer().getSprite().getBoundingRectangle())) {
            if(dragonProjectile.getProjectileState() == Projectile.ProjectileState.FIRING) {
                if(GameScreen.getInstance().getPlayer().getIsAlive()) {
                    // If the projectile hits the player it does damage
                    GameScreen.getInstance().getPlayer().healthCheck(getDamage());
                    dragonProjectile.setProjectileState(Projectile.ProjectileState.RESET);
                    dragonProjectile.switchState();

                }
            }
        }
    }

    @Override
    public Projectile getEnemyProjectile() {
        return dragonProjectile;
    }

}
