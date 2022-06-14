package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.GameObject.ScoreBar;


/*
The player class. Inherits from the Character super class.
 */
public class Player extends Character {


    // ---- PLAYER STATS -------------------------
    public enum PlayerState { IDLE, RUNNING, JUMPING, FALLING, ATTACKING, HURT, DYING, DEAD }

    private PlayerState playerState = PlayerState.IDLE;
    private int numberOfLives = 3;

    private boolean powerUp = false;
    private int numberOfCoins = 0;
    private int numberOfTreasures = 0;
    private int score = 0;

    // Set movement speeds
    private int runningSpeed = 400;
    private int jumpingSpeed = 550;
    private int fallingSpeed = 550;

    // Point where the state switches from jumping to falling
    private int terminal_Velocity = 520;

    // Guard that acts as a check to prevent other states from being enacted before the jump has finished.
    private boolean grounded = true;

    private Projectile playerProjectile;



    // ---- STATE ANIMATIONS -------------------------
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> runningAnimation;
    private Animation<TextureRegion> jumpingStartAnimation;
    private Animation<TextureRegion> jumpingLoopAnimation;
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

    private Animation<TextureRegion> jumpingLoopHandgunAnimation;
    private Animation<TextureRegion> jumpingLoopRifleAnimation;

    PlayerHP playerHP;

    public Player() {

        // Initialize start position
        getStartPosition().x = 200;
        setDirection(Direction.RIGHT);

        // Player Health Bar
        playerHP = new PlayerHP();



        // ---- PROJECTILE -------------------------
        // Initialize Projectile
        playerProjectile = new Projectile("Game Characters/Player/PlayerProjectile.png", "Audio/Sounds/shot.mp3");
        playerProjectile.getProjectileSprite().setSize(45, 25);

        playerProjectile.setMovementSpeedX(400f);
//        playerProjectile.setMovementSpeedY(-30f);


        // ---- ANIMATIONS -------------------------
        // Load all animation frames into animation objects using Game Helper.
        idleHandgunAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Idle - Handgun.png", 9, 2, 18);
        idleRifleAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Idle - Rifle.png", 6, 3, 18);
        runningHandgunAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Running - Handgun.png", 9, 2, 18);
        runningRifleAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Running - Rifle.png", 6, 3, 18);
        jumpingStartHandgunAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Jump Start - Handgun.png", 3, 2, 6);
        jumpingStartRifleAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Jump Start - Rifle.png", 3, 2, 6);
        jumpingLoopHandgunAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Jump Loop - Handgun.png", 3, 2, 6);
        jumpingLoopRifleAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/Player/Jump Loop - Rifle.png", 3, 2, 6);
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
        grounded = true;
        playerState = PlayerState.IDLE;

        playerHP.reset();
    }


    // Checks to see if the player is still alive after getting damaged. If still alive it enters the hurt state
    // otherwise it enters the dying state
    // *** COMMENT OUT THIS METHOD FOR GOD MODE ***
    public void healthCheck(int damage) {

        // The player can only get hurt or die when on the ground.
        if(grounded) {
            if ((super.getHealth() - damage) > 0) {
                playerState = PlayerState.HURT;
                super.setHealth(getHealth() - damage);
            } else {
                playerState = PlayerState.DYING;
                super.setHealth(0);
            }
            playerHP.modifyHP(getHealth());
        }
    }

    @Override
    public void draw(Batch batch, float alpha) {

        playerHP.draw(batch);
        super.draw(batch, alpha);

        /*
         Once a projectile has been reset this is polled to find out the direction the player is facing and apply that direction to the projectile.
         Once a projectile has been fired, the projectile direction has already been locked in, so it maintains the correct direction once it has been fired.
         Otherwise you see the projectile change direction mid flight if the player does.
         */
        if(playerProjectile.getProjectileState() == Projectile.ProjectileState.RESET) {

            //Offsets are added to the projectile start position (in projectile reset state) for the projectile to emit from the correct spot on the player.
            if (super.getDirection() == Direction.LEFT) {
                playerProjectile.getOffset().set(0, 100);
                playerProjectile.setDirection(Direction.LEFT);
            }
            if (super.getDirection() == Direction.RIGHT) {
                playerProjectile.getOffset().set(200, 100);
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

        // Updates the projectiles starting position so that when fired it will emit from wherever the character is.
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
            jumpingLoopAnimation = jumpingLoopRifleAnimation;
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
            jumpingLoopAnimation = jumpingLoopHandgunAnimation;
            jumpingEndAnimation = jumpingEndHandgunAnimation;
            attackingAnimation = attackingHandgunAnimation;
            hurtAnimation = hurtHandgunAnimation;
            dyingAnimation = dyingHandgunAnimation;

            super.setDamage(20);
        }


        // Controls the animations that are performed in different states as well as applies any additional conditions to the states.
        switch (playerState) {
            case IDLE:
                // Set the speed and animation for idle
                super.setCURRENT_MOVEMENT_SPEED(0);
                super.loopingAnimation(idleAnimation);
                break;

            case RUNNING:
                // Set the speed and animation for running
                super.setCURRENT_MOVEMENT_SPEED(runningSpeed);
                super.loopingAnimation(runningAnimation);
                break;

            case JUMPING:
                // Grounded is a guard so that the player cannot enter other states untill the jump has finished
                grounded = false;
                super.setCURRENT_MOVEMENT_SPEED(jumpingSpeed);
                if(!grounded) {
                    // Start jumping
                    jumpCharacter();
                    // Once the animation has finished and terminal velocity has been hit, the player can start falling
                    if (super.nonLoopingAnimation(jumpingStartAnimation)) {
                        if (getSprite().getY() > terminal_Velocity) {
                            playerState = PlayerState.FALLING;
                        }
                    }
                }
                break;

            case FALLING:
                super.setCURRENT_MOVEMENT_SPEED(fallingSpeed);
                super.nonLoopingAnimation(jumpingLoopAnimation);
                // Start falling
                fallCharacter();

                if (super.nonLoopingAnimation(jumpingEndAnimation)) {
                    // If the player has fallen back to ground level then stop falling
                    if (getSprite().getY() < getGroundLevel()) {
                        getSprite().setPosition(getSprite().getX(), getStartPosition().y);
                        grounded = true;
                        playerState = PlayerState.IDLE;
                    }
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
                    super.setIsAlive(false);
                    playerState = PlayerState.DEAD;
                    numberOfLives -= 1;
                }
                break;
        }
    }


    /**
     Takes the current movement speed and uses Game Helper to apply deltaTime giving the total speed.
     Can then apply this to find the new position for the sprite which is then translated to that position.
     Player has its own version of moveCharacter, which is overwritten so that other characters don't have the camera speed added to their movement
     */

    @Override
    public void moveCharacter() {

        getPositionAmount().x = GameScreen.getInstance().getHelper().setMovement(getCURRENT_MOVEMENT_SPEED());
        getPositionAmount().y = 0;

        if(getDirection() == Direction.LEFT) {
            getSprite().translate(-getPositionAmount().x, getPositionAmount().y);
        }
        else {
            getSprite().translate(getPositionAmount().x, getPositionAmount().y);
        }

    }

    // Same as moveCharacter but applied to jumping. Adds the jumping speed to the Y axis.
    public void jumpCharacter() {

        getPositionAmount().x = GameScreen.getInstance().getHelper().setMovement(getCURRENT_MOVEMENT_SPEED());
        getPositionAmount().y = GameScreen.getInstance().getHelper().setMovement(getCURRENT_MOVEMENT_SPEED());

        if(getDirection() == Direction.LEFT) {
            getSprite().translate(-getPositionAmount().x, getPositionAmount().y);
        }
        else {
            getSprite().translate(getPositionAmount().x, getPositionAmount().y);
        }
    }

    public void fallCharacter() {

        getPositionAmount().x = GameScreen.getInstance().getHelper().setMovement(getCURRENT_MOVEMENT_SPEED());
        getPositionAmount().y = GameScreen.getInstance().getHelper().setMovement(-getCURRENT_MOVEMENT_SPEED());

        if(getDirection() == Direction.LEFT) {
            getSprite().translate(-getPositionAmount().x, getPositionAmount().y);
        }
        else {
            getSprite().translate(getPositionAmount().x, getPositionAmount().y);
        }
    }


    // ---------- GETTERS AND SETTERS -------------------------------------
    public PlayerState getPlayerState() { return playerState; }

    public void setPlayerState(PlayerState playerState) { this.playerState = playerState; }

    public int getNumberOfLives() { return numberOfLives; }

    public boolean getPowerUp() { return powerUp; }

    public void setPowerUp(boolean powerUp) { this.powerUp = powerUp; }

    public Projectile getPlayerProjectile() { return playerProjectile; }

    public int getNumberOfCoins() { return numberOfCoins; }

    public void setNumberOfCoins(int numberOfCoins) { this.numberOfCoins = numberOfCoins; }

    public int getNumberOfTreasures() { return numberOfTreasures; }

    public void setNumberOfTreasures(int numberOfTreasures) { this.numberOfTreasures = numberOfTreasures; }

    public int getScore() { return score; }

    public void setScore(int score) { this.score = score; }

    public boolean getIsGrounded() { return grounded;}
}
