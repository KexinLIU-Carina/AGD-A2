package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


/*
The player class. Inherits from the Character super class.
 */
public class Player extends Character {


    // ---- PLAYER STATS -------------------------
    public enum PlayerState { IDLE, RUNNING, JUMPING, FALLING, ATTACKING, HURT, DYING, DEAD }

    private PlayerState playerState = PlayerState.IDLE;
    private int numberOfLives = 3;
    private Direction direction = Direction.RIGHT;

    private boolean powerUp = false;
    private int numberOfCoins = 0;
    private int numberOfTreasures = 0;
    private int score = 0;

    // Set movement speeds
    private int runningSpeed = 200;
    private int jumpingSpeed = 100;
    private int fallingSpeed = 100;

    private Projectile playerProjectile;



    // ---- STATE ANIMATIONS -------------------------
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> runningAnimation;
    private Animation<TextureRegion> jumpingStartAnimation;
    private Animation<TextureRegion> jumpingEndAnimation;
    private Animation<TextureRegion> attackingAnimation;
    private Animation<TextureRegion> hurtAnimation;
    private Animation<TextureRegion> dyingAnimation;

    // ---- ALL ANIMATIONS -------------------------
    private Animation<TextureRegion> idleHandgunAnimation;
    private Animation<TextureRegion> idleRifleAnimation;
    private Animation<TextureRegion> runningHandgunAnimation;
    private Animation<TextureRegion> runningRifleAnimation;
    private Animation<TextureRegion> jumpingStartHandgunAnimation;
    private Animation<TextureRegion> jumpingStartRifleAnimation;
    private Animation<TextureRegion> jumpingEndHandgunAnimation;
    private Animation<TextureRegion> jumpingEndRifleAnimation;
    private Animation<TextureRegion> attackingHandgunAnimation;
    private Animation<TextureRegion> attackingRifleAnimation;
    private Animation<TextureRegion> hurtHandgunAnimation;
    private Animation<TextureRegion> hurtRifleAnimation;
    private Animation<TextureRegion> dyingHandgunAnimation;
    private Animation<TextureRegion> dyingRifleAnimation;




    public Player() {

        // Initialize size and start position
        getSprite().setSize(100, 100);
        getStartPosition().set(200, 120);


        // ---- PROJECTILE -------------------------
        // Initialize Projectile
        playerProjectile = new Projectile("Game Characters/Player/PlayerProjectile.png", "Audio/Sounds/shot.mp3");
        playerProjectile.getProjectileSprite().setSize(15, 5);

        Gdx.app.log("Main", "health " + getHealth());
        /*
        Projectiles have a starting position. This is updated in act() to equal the players start position so that it emits from the players position.
        The offset amount is added to the start position in the projectiles reset state. This maintains the projectile will emit from the correct
        spot on the player.
         */
        playerProjectile.getOffset().set(100, 43);
        playerProjectile.setMovementSpeedX(350f);
//        playerProjectile.setMovementSpeedY(-30f);


        // ---- ANIMATIONS -------------------------
        // Load all animation frames into animation objects using Game Helper.
        idleHandgunAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Idle - Handgun.png", 9, 2, 18);
        idleRifleAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Idle - Rifle.png", 6, 3, 18);
        runningHandgunAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Running - Handgun.png", 9, 2, 18);
        runningRifleAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Running - Rifle.png", 6, 3, 18);
        jumpingStartHandgunAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Jump Start - Handgun.png", 3, 2, 6);
        jumpingStartRifleAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Jump Start - Rifle.png", 3, 2, 6);
        jumpingEndHandgunAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Jump End - Handgun.png", 3, 2, 6);
        jumpingEndRifleAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Jump End - Rifle.png", 3, 2, 6);
        attackingHandgunAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Attacking - Handgun.png", 3, 3, 9);
        attackingRifleAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Attacking - Rifle.png", 4, 1, 4);
        hurtHandgunAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Hurt - Handgun.png", 4, 3, 12);
        hurtRifleAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Hurt - Rifle.png", 4, 3, 12);
        dyingHandgunAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Dying - Handgun.png", 3, 4, 12);
        dyingRifleAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Dying - Rifle.png", 4, 3, 12);

    }

    // Resets the player if it has lost a life.
    public void reset() {
        // Player is alive again
        super.setIsAlive(true);
        // Health back to full health
        super.setHealth(getMax_Health());
        // Back to start position and idle
        super.getSprite().setPosition(getStartPosition().x, getStartPosition().y);
        playerState = PlayerState.IDLE;
    }


    // Checks to see if the player is still alive after getting damaged. If still alive it enters the hurt state
    // otherwise it enters the dying state
    // *** COMMENT OUT THIS METHOD FOR GOD MODE ***
    public void healthCheck(int damage) {
        if((super.getHealth() - damage) > 0) {
            playerState = PlayerState.HURT;
            super.setHealth(getHealth() - damage);
        }
        else {
            playerState = PlayerState.DYING;
            super.setHealth(0);
        }
    }

    @Override
    public void draw(Batch batch, float alpha) {

        super.draw(batch, alpha);

        /*
         Once a projectile has been reset this is polled to find out the direction the player is facing and apply that direction to the projectile.
         Once a projectile has been fired, the projectile direction has already been locked in, so it maintains the correct direction once it has been fired.
         Otherwise you see the projectile change direction mid flight if the player does.
         */
        if(playerProjectile.getProjectileState() == Projectile.ProjectileState.RESET) {
            // Set the projectile to be launched in the same direction the character is facing. Reverses the offset accordingly.
            if (super.getDirection() == Direction.LEFT) {
                playerProjectile.getOffset().set(-10, 43);
                playerProjectile.setDirection(Direction.LEFT);
            }
            if (super.getDirection() == Direction.RIGHT) {
                playerProjectile.getOffset().set(100, 43);
                playerProjectile.setDirection(Direction.RIGHT);
            }
        }
        // Draw the projectile if it has been fired.
        if(playerProjectile.getProjectileState() == Projectile.ProjectileState.FIRING) {
            playerProjectile.draw(batch, alpha);
        }
    }

    @Override
    public void act(float delta) {

        // Updates the projectile to emit from wherever the character is.
        playerProjectile.getProjectileStartPosition().x = getSprite().getX();
        playerProjectile.getProjectileStartPosition().y = getSprite().getY();
        playerProjectile.act(delta);

        switchStates();
    }


    // A state machine. Applies the correct animations, movement and other conditions to the various player states.
    public void switchStates() {

        // Normal player animations have a handgun equipped, but if a power up is enabled, all the animations are set to the more powerful rifle weapon.
        if (powerUp) {
            idleAnimation = idleRifleAnimation;
            runningAnimation = runningRifleAnimation;
            jumpingStartAnimation = jumpingStartRifleAnimation;
            jumpingEndAnimation = jumpingEndRifleAnimation;
            attackingAnimation = attackingRifleAnimation;
            hurtAnimation = hurtRifleAnimation;
            dyingAnimation = dyingRifleAnimation;

            // The rifle does more damage
            super.setDamage(100);
        }
        else {
            idleAnimation = idleHandgunAnimation;
            runningAnimation = runningHandgunAnimation;
            jumpingStartAnimation = jumpingStartHandgunAnimation;
            jumpingEndAnimation = jumpingEndHandgunAnimation;
            attackingAnimation = attackingHandgunAnimation;
            hurtAnimation = hurtHandgunAnimation;
            dyingAnimation = dyingHandgunAnimation;

            super.setDamage(20);
        }


        // Controls the animations that are performed in different states as well as applies any additional conditions to the states.
        switch (playerState) {
            case IDLE:
                // If idle the speed is zero
                super.setCURRENT_MOVEMENT_SPEED(0);
                super.loopingAnimation(idleAnimation);
                break;

            case RUNNING:
                super.setCURRENT_MOVEMENT_SPEED(runningSpeed);
                super.loopingAnimation(runningAnimation);
                break;

            case JUMPING:
                super.setCURRENT_MOVEMENT_SPEED(jumpingSpeed);
                if (super.nonLoopingAnimation(jumpingStartAnimation)) {
                    playerState = PlayerState.FALLING;
                }
                break;

            case FALLING:
                super.setCURRENT_MOVEMENT_SPEED(fallingSpeed);
                if (super.nonLoopingAnimation(jumpingEndAnimation)) {
                    playerState = PlayerState.IDLE;
                }
                break;

            case ATTACKING:
                super.setCURRENT_MOVEMENT_SPEED(0);
                playerProjectile.setProjectileState(Projectile.ProjectileState.FIRING);
                if (super.nonLoopingAnimation(attackingAnimation)) {
                    playerState = PlayerState.IDLE;
                }
                break;

            case HURT:
                super.setCURRENT_MOVEMENT_SPEED(0);
                if (super.nonLoopingAnimation(hurtAnimation)) {
                    playerState = PlayerState.IDLE;
                }
                break;

            case DYING:
                super.setCURRENT_MOVEMENT_SPEED(0);
                if (super.nonLoopingAnimation(dyingAnimation)) {
                    Gdx.app.log("Main", "player dead " + getHealth());
                    super.setIsAlive(false);
                    playerState = PlayerState.DEAD;
                    numberOfLives -= 1;
                }
                break;
        }
    }


    // ---------- GETTERS AND SETTERS -------------------------------------
    public PlayerState getPlayerState() { return playerState; }

    public void setPlayerState(PlayerState playerState) { this.playerState = playerState; }

    public int getNumberOfLives() { return numberOfLives; }

    public void setNumberOfLives(int numberOfLives) { this.numberOfLives = numberOfLives; }

    public boolean getPowerUp() { return powerUp; }

    public void setPowerUp(boolean powerUp) { this.powerUp = powerUp; }

    public Projectile getPlayerProjectile() { return playerProjectile; }
}
