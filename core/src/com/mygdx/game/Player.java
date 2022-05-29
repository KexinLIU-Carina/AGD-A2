package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;



public class Player extends Character implements CharacterInterface {


    // ---- PLAYER STATS -------------------------
    public enum PlayerState { IDLE, RUNNING, JUMPING, FALLING, ATTACKING, HURT, DYING, DEAD }
    public enum Direction { LEFT, RIGHT }

    private PlayerState playerState = PlayerState.IDLE;
    private int numberOfLives = 3;
    private Direction direction = Direction.RIGHT;

    private boolean powerUp = false;
    private int numberOfCoins = 0;
    private int numberOfTreasures = 0;
    private int score = 0;

    private Projectile playerProjectile;
    private Projectile handgunProjectile;
    private Projectile rifleProjectile;



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

    // The stateTime used for looping animations
    float stateTime = 0;


    // Player HP
    private Sprite playerHPBackground;
    private  Sprite playerHP;
    private Texture HPimage;



    public Player() {

        // Player HP
        Texture HPBimage = new Texture("GUI/Cartoon Sci-Fi Game GUI/Misc/Cartoon Sci-Fi Game GUI_Progress Bar - Background.png");
        playerHPBackground = new Sprite( HPBimage);
        HPimage = new Texture("GUI/Cartoon Sci-Fi Game GUI/Misc/Cartoon Sci-Fi Game GUI_Progress Bar - Green.png");
        playerHP = new Sprite(HPimage);
        // TODO: need a better way to set the position
        playerHP.setPosition(150,Gdx.graphics.getHeight() - HPimage.getHeight()-40);
        playerHPBackground.setPosition(0,Gdx.graphics.getHeight() - HPBimage.getHeight());

        // Initialize size and start position
        getSprite().setSize(100, 100);
        getStartPosition().set(200, 120);

        // Set movement speeds
        setRunningSpeed(200);
        setJumpingSpeed(100);
        setFallingSpeed(100);


        // Initialize Projectile
        playerProjectile = new Projectile("Game Characters/Player/PlayerProjectile.png", "Audio/Sounds/shot.mp3");
        playerProjectile.getProjectileSprite().setSize(15, 5);
        playerProjectile.getProjectileSprite().flip(true, false);

        // Initialize the starting position of the projectile to the players start position. The offset amount is added to the start position.
        // This is the final position that the projectile is set to so that it emits from the correct spot on the player.
        // The start position is updated in player.act() so that when the player moves the projectile keeps up.
        // The offset is added to the start position in the projectiles reset state, keeping the correct emission position.
        playerProjectile.getProjectileStartPosition().x = getStartPosition().x;
        playerProjectile.getProjectileStartPosition().y = getStartPosition().y;
        playerProjectile.getOffset().set(100, 43);

//        playerProjectile.setMovementSpeedY(-30f);
//        playerProjectile.setMovementSpeedX(350f);


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


    @Override
    public void reset() {
        // Player is alive again
        setIsAlive(true);
        // Health back to full health
        setHealth(getMax_Health());
        // Back to start position and idle
        getSprite().setPosition(getStartPosition().x, getStartPosition().y);
        playerState = PlayerState.IDLE;
    }


    // Checks to see if the player is still alive after getting damaged. If still alive it enters the hurt state
    // otherwise it enters the dying state
    public void healthCheck(int damage) {
        if((getHealth() - damage) > 0) {
            playerState = PlayerState.HURT;
            setHealth(getHealth() - damage);

            // TODO: need to test this code
            playerHP.setSize(HPimage.getWidth() * getHealth()/100, HPimage.getHeight());
        }
        else {
            playerState = PlayerState.DYING;

            playerHP.setSize(HPimage.getWidth() * getHealth()/100, HPimage.getHeight());
            setHealth(0);
        }
    }

    @Override
    public void draw(Batch batch, float alpha) {

        // Player HP
        playerHPBackground.draw(batch);
        playerHP.draw(batch);



        // Flips the sprite according to the correct direction. Reverses the offset and speed accordingly.
        // Only flips when drawing is needed to conserve processing power
        if(getDirection() == Direction.LEFT) {
            playerProjectile.getOffset().set(-10, 43);
            if(playerProjectile.getProjectileState() == Projectile.ProjectileState.RESET) {
                playerProjectile.setMovementSpeedX(-350f);
            }
            if (!getCurrentFrame().isFlipX()) {
                getCurrentFrame().flip(true, false);
                // CAN'T GET THIS TO WORK!! METHOD DOESN'T APPEAR TO DO WHAT I EXPECT.
                // SEEMS LIKE MAYBE IT'S FLIPPING THE SPRITE BUT NOT THE UNDERLYING TEXTURE. CAN'T FLIP TEXTURE DIRECTLY.
                playerProjectile.getProjectileSprite().flip(false, true);
            }
        }
        if(getDirection() == Direction.RIGHT) {
            playerProjectile.getOffset().set(100, 43);
            if(playerProjectile.getProjectileState() == Projectile.ProjectileState.RESET) {
                playerProjectile.setMovementSpeedX(350f);
            }
            if(getCurrentFrame().isFlipX()) {
                getCurrentFrame().flip(true, false);
            }
        }
        batch.draw(getCurrentFrame(), getSprite().getX(), getSprite().getY(), getSprite().getWidth(), getSprite().getHeight());
        playerProjectile.draw(batch, alpha);
    }

    @Override
    public void act(float delta) {

        stateTime += delta;

        // Updates the projectile to emit from wherever the player is.
        playerProjectile.getProjectileStartPosition().x = getSprite().getX();
        playerProjectile.getProjectileStartPosition().y = getSprite().getY();

        playerProjectile.act(delta);

        switchStates();
    }


    @Override
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

            setDamage(100);
        }
        else {
            idleAnimation = idleHandgunAnimation;
            runningAnimation = runningHandgunAnimation;
            jumpingStartAnimation = jumpingStartHandgunAnimation;
            jumpingEndAnimation = jumpingEndHandgunAnimation;
            attackingAnimation = attackingHandgunAnimation;
            hurtAnimation = hurtHandgunAnimation;
            dyingAnimation = dyingHandgunAnimation;

            setDamage(20);
        }


        // Controls the animations that are performed in different states as well as applies any additional conditions to the states.
        switch (playerState) {
            case IDLE:
                setCURRENT_MOVEMENT_SPEED(0);
                setCurrentFrame(idleAnimation.getKeyFrame(stateTime, true));
                break;

            case RUNNING:
                setCURRENT_MOVEMENT_SPEED(getRunningSpeed());
                setCurrentFrame(runningAnimation.getKeyFrame(stateTime, true));
                break;

            case JUMPING:
                setCURRENT_MOVEMENT_SPEED(getJumpingSpeed());
                if(setAnimationFrame(jumpingStartAnimation)) {
                    playerState = PlayerState.FALLING;
                }
                break;

            case FALLING:
                setCURRENT_MOVEMENT_SPEED(getFallingSpeed());
                setAnimationFrame(jumpingEndAnimation);
                break;

            case ATTACKING:
                setCURRENT_MOVEMENT_SPEED(0);
                playerProjectile.setProjectileState(Projectile.ProjectileState.FIRING);
                if(setAnimationFrame(attackingAnimation)) {
                    playerState = PlayerState.IDLE;
                }
                break;

            case HURT:
                setCURRENT_MOVEMENT_SPEED(0);
                if(setAnimationFrame(hurtAnimation)) {
                    playerState = PlayerState.IDLE;
                }
                break;

            case DYING:
                setCURRENT_MOVEMENT_SPEED(0);
                if (setAnimationFrame(dyingAnimation)) {
                    setIsAlive(false);
                    playerState = PlayerState.DEAD;
                    numberOfLives -= 1;
                }
                break;
        }
    }


    // ---------- GETTERS AND SETTERS -------------------------------------
    public PlayerState getPlayerState() { return playerState; }

    public void setPlayerState(PlayerState playerState) { this.playerState = playerState; }

    public Direction getDirection() { return direction; }

    public void setDirection(Direction direction) { this.direction = direction; }

    public int getNumberOfLives() { return numberOfLives; }

    public void setNumberOfLives(int numberOfLives) { this.numberOfLives = numberOfLives; }

    public boolean getPowerUp() { return powerUp; }

    public void setPowerUp(boolean powerUp) { this.powerUp = powerUp; }

    public Projectile getPlayerProjectile() { return playerProjectile; }

    public void setPlayerProjectile(Projectile playerProjectile) { this.playerProjectile = playerProjectile; }
}
