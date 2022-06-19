package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;


/**
 * The parent superclass for all characters.
 * Both Player and Enemies inherit from this class and it provides the basic template that both of them require.
 */
public class Character extends Actor {

    public enum Direction { LEFT, RIGHT }


    // ---- CHARACTER STATS -------------------------
    private boolean isAlive = true;
    private Direction direction;

    private int Max_Health = 100;
    private int health = Max_Health;
    private int CURRENT_MOVEMENT_SPEED;
    private int damage = 20;


    // ---- SPRITES -------------------------
    protected Sprite sprite;
    private Vector2 startPosition;
    // The amount that a sprite will be translated by to reach its new position
    private Vector2 positionAmount;


    // ---- ANIMATION -------------------------
    protected TextureRegion currentFrame;

    private float loopingStateTime;
    private float nonLoopingStateTime;

    private float deltaTime = Gdx.graphics.getDeltaTime();


    private float groundLevel;

    // particle
    protected Particle particles;

    public Character() {

        particles = new Particle();

        sprite = new Sprite();
        sprite.setSize(250, 250);
        currentFrame = new TextureRegion();
        startPosition = new Vector2();
        positionAmount = new Vector2();

        groundLevel = GameScreen.getInstance().getLevelFactory().getCurrentLevel().getGroundLevel();
        startPosition.y = groundLevel;

    }


    /*
    The default drawing method. It flips the sprites to face the correct direction.
    Child classes that may need to override this will still have a call to super to access this method.
     */
    @Override
    public void draw(Batch batch, float alpha) {



        // Flips the sprite according to the correct direction.
        if (direction == Direction.LEFT) {
            if(!currentFrame.isFlipX()) {
                currentFrame.flip(true, false);
            }
        }

        if (direction == Direction.RIGHT) {
            if(currentFrame.isFlipX()) {
                currentFrame.flip(true, false);
            }
        }
        batch.draw(currentFrame, sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        this.particles.render(batch);
    }


    /*
     Processes animations for both looping and non looping versions. Non looping resets the state time so that it only plays once.
     Applies separate state time to each version so that they don't interfere with each other when the non looping resets.
     */
    public boolean nonLoopingAnimation(Animation<TextureRegion> animation) {

        nonLoopingStateTime += deltaTime;

        if (animation.isAnimationFinished(nonLoopingStateTime)) {
            nonLoopingStateTime = 0;
            return true;
        }
        else {
            currentFrame = animation.getKeyFrame(nonLoopingStateTime, false);
            return false;
        }
    }

    public void loopingAnimation(Animation<TextureRegion> animation) {

        loopingStateTime += deltaTime;

        currentFrame = animation.getKeyFrame(loopingStateTime, true);
    }



    // Finds and returns the centre of the sprite for when this is needed.
    public Vector2 getCenteredSpritePosition() {
        float x = sprite.getX() + (sprite.getWidth() / 2);
        float y = sprite.getY() + (sprite.getHeight() / 2);

        return new Vector2(x, y);
    }

    // -----------------------------------------------------------

    // Finds out how far away the player is from the enemy sprite.
    public float distanceFromPlayer(Player player) {
        return getCenteredSpritePosition().dst(player.getCenteredSpritePosition());
    }

    // -----------------------------------------------------------


    /* A characters movement speed * delta time gives a position amount.
    * This is the amount of distance that the sprite has to travel to to reach the new position.
    * The sprites are then translated to the new position given by the position amount
    */
    public void moveCharacter() {

        positionAmount.x = GameScreen.getInstance().getHelper().setMovement(CURRENT_MOVEMENT_SPEED);
        positionAmount.y = 0;

        if(direction == Direction.LEFT) {
            sprite.translate(-positionAmount.x, positionAmount.y);
        }
        else {
            sprite.translate(positionAmount.x, positionAmount.y);
        }
    }

    /*
     * Moves the characters in the opposite direction to oppose the cameras movement,
     * giving the impression that they are not moving if they are static objects.
     * Characters that are moving also do not have the cameras movement added to their own.
     * All "compensate" methods do this.
     */
    public void compensateCamera(float cameraPositionAmount) {

        sprite.translate(cameraPositionAmount, positionAmount.y);

    }


    // ---------- GETTERS AND SETTERS -------------------------------------
    public boolean getIsAlive() { return isAlive; }

    public void setIsAlive(boolean alive) { isAlive = alive; }

    public Direction getDirection() { return direction; }

    public void setDirection(Direction direction) { this.direction = direction; }

    public int getHealth() { return health; }

    public void setHealth(int health) { this.health = health; }

    public int getMax_Health() { return Max_Health; }

    public int getCURRENT_MOVEMENT_SPEED() { return CURRENT_MOVEMENT_SPEED; }

    public void setCURRENT_MOVEMENT_SPEED(int CURRENT_MOVEMENT_SPEED) { this.CURRENT_MOVEMENT_SPEED = CURRENT_MOVEMENT_SPEED; }

    public int getDamage() { return damage; }

    public void setDamage(int damage) { this.damage = damage; }

    public Sprite getSprite() { return sprite; }

    public Vector2 getStartPosition() { return startPosition; }

    public Vector2 getPositionAmount() { return positionAmount; }

    public float getGroundLevel() { return groundLevel; }
}
