package com.mygdx.game;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;



public class Character extends Actor {


    // ---- CHARACTER STATS -------------------------
    private boolean isAlive = true;

    private int Max_Health = 100;
    private int health = Max_Health;
    private int CURRENT_MOVEMENT_SPEED;
    private int damage = 20;

    private int walkingSpeed = 100;
    private int runningSpeed = 200;
    private int jumpingSpeed = 100;
    private int fallingSpeed = 100;


    // ---- SPRITES -------------------------
    private Sprite sprite;
    private Vector2 startPosition;
    // The amount that a sprite will be translated by to reach its new position
    private Vector2 positionAmount;



    // ---- ANIMATION -------------------------
    private float stateTime = 0;
    private TextureRegion currentFrame;



    public Character() {

        sprite = new Sprite();
        currentFrame = new TextureRegion();
        startPosition = new Vector2();
        positionAmount = new Vector2();

    }




    @Override
    public void draw(Batch batch, float alpha) {

        batch.draw(currentFrame, sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }




    public boolean setAnimationFrame(Animation<TextureRegion> animationFrame) {

        if (animationFrame.isAnimationFinished(getStateTime())) {
            setStateTime(0);
            return true;
        }
        else {
            currentFrame = animationFrame.getKeyFrame(getStateTime(), false);
            return false;
        }
    }



    // ---------- GETTERS AND SETTERS -------------------------------------
    public boolean getIsAlive() { return isAlive; }

    public void setIsAlive(boolean alive) { isAlive = alive; }

    public int getHealth() { return health; }

    public void setHealth(int health) { this.health = health; }

    public int getMax_Health() { return Max_Health; }

    public int getCURRENT_MOVEMENT_SPEED() { return CURRENT_MOVEMENT_SPEED; }

    public void setCURRENT_MOVEMENT_SPEED(int CURRENT_MOVEMENT_SPEED) { this.CURRENT_MOVEMENT_SPEED = CURRENT_MOVEMENT_SPEED; }

    public int getWalkingSpeed() { return walkingSpeed; }

    public void setWalkingSpeed(int walkingSpeed) { this.walkingSpeed = walkingSpeed; }

    public int getRunningSpeed() { return runningSpeed; }

    public void setRunningSpeed(int runningSpeed) { this.runningSpeed = runningSpeed; }

    public int getJumpingSpeed() { return jumpingSpeed; }

    public void setJumpingSpeed(int jumpingSpeed) { this.jumpingSpeed = jumpingSpeed; }

    public int getFallingSpeed() { return fallingSpeed; }

    public void setFallingSpeed(int fallingSpeed) { this.fallingSpeed = fallingSpeed; }

    public int getDamage() { return damage; }

    public void setDamage(int damage) { this.damage = damage; }

    public Sprite getSprite() { return sprite; }

    public Vector2 getStartPosition() { return startPosition; }

    public Vector2 getPositionAmount() { return positionAmount; }

    public float getStateTime() { return stateTime; }

    public void setStateTime(float stateTime) { this.stateTime = stateTime; }

    public TextureRegion getCurrentFrame() { return currentFrame; }

    public void setCurrentFrame(TextureRegion currentFrame) { this.currentFrame = currentFrame; }
}
