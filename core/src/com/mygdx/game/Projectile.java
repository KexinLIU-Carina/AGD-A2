package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;


/*
A class for projectiles that need to be fired around by player and enemies. It manages the states, sounds and behaviour of projectiles.
Each instance of a projectile is owned by the character that fires it.
 */
public class Projectile extends Actor {

    public enum ProjectileState { FIRING, EXPLODING, RESET }

    private ProjectileState projectileState = ProjectileState.RESET;
    private Character.Direction direction;

    private TextureRegion textureRegion;
    private Sprite projectileSprite;
    private float movementSpeedX = 0;
    private float movementSpeedY = 0;
    private Vector2 projectileStartPosition;
    private Vector2 offset;
    private Vector2 projectileStartWithOffset;
    private Vector2 PROJECTILE_MOVEMENT;
    private Sound firingSound;
    private boolean playFiringSound = true;


    public Projectile(String texturePath, String firingSoundPath) {

        Texture texture = new Texture(texturePath);
        textureRegion = new TextureRegion();
        textureRegion.setRegion(texture);
        projectileSprite = new Sprite(textureRegion);
        projectileStartPosition = new Vector2();
        offset = new Vector2();
        projectileStartWithOffset = new Vector2();
        PROJECTILE_MOVEMENT = new Vector2();

        firingSound = Gdx.audio.newSound(Gdx.files.internal(firingSoundPath));

    }


    @Override
    public void draw(Batch batch, float alpha) {

        if (projectileState == ProjectileState.FIRING) {

            if (direction == Character.Direction.LEFT) {
                if (!textureRegion.isFlipX()) {
                    textureRegion.flip(true, false);
                }
            }
            if (direction == Character.Direction.RIGHT) {
                if (textureRegion.isFlipX()) {
                    textureRegion.flip(true,false);

                }
            }
            batch.draw(textureRegion, projectileSprite.getX(), projectileSprite.getY(),
                    projectileSprite.getWidth(),projectileSprite.getHeight());

        }
    }


    @Override
    public void act(float delta) {

        switchState();
        setProjectileBounds();
    }


    public void switchState() {

        switch (projectileState) {

            case RESET:
                projectileSprite.setPosition(getProjectileStartWithOffset().x, getProjectileStartWithOffset().y);
                playFiringSound = true;
//                playExplodingSound = true;
                break;

            case FIRING:
                moveProjectile();
                playFiringSound();
                break;
//            case EXPLODING:
//                currentFrame = currentExplodingFrame;
//                setExplodingSound();
//                break;
        }
    }

    public void playFiringSound() {
        if(playFiringSound) {
            firingSound.play();
            playFiringSound = false;
        }
    }

//    public void setExplodingSound() {
//        if(playExplodingSound) {
//            explodingSound.play();
//            playExplodingSound = false;
//        }
//    }

    public void setProjectileBounds() {
        if(getProjectileSprite().getX() > Gdx.graphics.getWidth()) {
            projectileState = ProjectileState.RESET;
            switchState();
        }
        if(getProjectileSprite().getX() < 0) {
            projectileState = ProjectileState.RESET;
            switchState();
        }
    }

    /*
     Takes the movement speeds and direction, uses Game Helper to apply deltaTime, then finds the new position for the sprite to move to
     and translates to the new position.
     */
    public void moveProjectile() {
        if(direction == Character.Direction.LEFT) {
            PROJECTILE_MOVEMENT.x = GameScreen.getInstance().getHelper().setMovement(-movementSpeedX);
        }
        else {
            PROJECTILE_MOVEMENT.x = GameScreen.getInstance().getHelper().setMovement(movementSpeedX);
        }
        PROJECTILE_MOVEMENT.y = GameScreen.getInstance().getHelper().setMovement(movementSpeedY);
        projectileSprite.translate(PROJECTILE_MOVEMENT.x, PROJECTILE_MOVEMENT.y);
    }



    // ------ GETTERS AND SETTERS --------------------------------

    public ProjectileState getProjectileState() { return projectileState; }

    public void setProjectileState(ProjectileState projectileState) { this.projectileState = projectileState; }

    public Sprite getProjectileSprite() { return projectileSprite; }

    public float getMovementSpeedX() { return movementSpeedX; }

    public void setMovementSpeedX(float movementSpeedX) { this.movementSpeedX = movementSpeedX; }

    public float getMovementSpeedY() { return movementSpeedY; }

    public void setMovementSpeedY(float movementSpeedY) { this.movementSpeedY = movementSpeedY; }

    public Vector2 getProjectileStartPosition() { return projectileStartPosition; }

    public Vector2 getProjectileStartWithOffset() {
        projectileStartWithOffset.x = projectileStartPosition.x + offset.x;
        projectileStartWithOffset.y = projectileStartPosition.y + offset.y;
        return projectileStartWithOffset;
    }

    public Vector2 getOffset() { return offset; }

    public Character.Direction getDirection() { return direction; }

    public void setDirection(Character.Direction direction) { this.direction = direction; }


}
