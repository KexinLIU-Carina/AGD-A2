package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;



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
    private Sprite sprite;
    private Vector2 startPosition;
    // The amount that a sprite will be translated by to reach its new position
    private Vector2 positionAmount;



    // ---- ANIMATION -------------------------
    private TextureRegion currentFrame;

    private float loopingStateTime;
    private float nonLoopingStateTime;

    private float deltaTime = Gdx.graphics.getDeltaTime();



    public Character() {

        sprite = new Sprite();
        currentFrame = new TextureRegion();
        startPosition = new Vector2();
        positionAmount = new Vector2();
    }


    // The default drawing method.
    @Override
    public void draw(Batch batch, float alpha) {

        // Flips the sprite according to the correct direction.
        if (direction == Direction.LEFT) {
            if(!currentFrame.isFlipX()) {
                currentFrame.flip(true, false);
            }
        }

        if (getDirection() == Direction.RIGHT) {
            if(currentFrame.isFlipX()) {
                currentFrame.flip(true, false);
            }
        }
        batch.draw(currentFrame, sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }


    public boolean nonLoopingAnimation(Animation<TextureRegion> animation) {

        nonLoopingStateTime += deltaTime;

        if (animation.isAnimationFinished(nonLoopingStateTime)) {
            nonLoopingStateTime = 0;
            return true;
        }
        else {
            setCurrentFrame(animation.getKeyFrame(nonLoopingStateTime, false));
            return false;
        }
    }

    public void loopingAnimation(Animation<TextureRegion> animation) {
        loopingStateTime += deltaTime;
        setCurrentFrame(animation.getKeyFrame(loopingStateTime, true));
    }


    public Vector2 getCenteredSpritePosition() {
        float x = getSprite().getX() + (getSprite().getWidth() / 2);
        float y = getSprite().getY() + (getSprite().getHeight() / 2);

        return new Vector2(x, y);
    }


    public void moveCharacter() {
        if(direction == Direction.LEFT) {
            positionAmount.x = GameScreen.getInstance().getHelper().setMovement(-CURRENT_MOVEMENT_SPEED);
        }
        else {
            positionAmount.x = GameScreen.getInstance().getHelper().setMovement(CURRENT_MOVEMENT_SPEED);
        }
        sprite.translate(positionAmount.x, positionAmount.y);
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

    public void setCurrentFrame(TextureRegion currentFrame) { this.currentFrame = currentFrame; }

}
