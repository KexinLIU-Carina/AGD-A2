package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;



public class Projectile extends Actor {

    public enum ProjectileState { FIRING, EXPLODING, RESET }

    private ProjectileState projectileState = ProjectileState.RESET;

    private Texture texture;
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

        texture = new Texture(texturePath);
        projectileSprite = new Sprite(texture);
        projectileSprite.flip(true, false);
        projectileStartPosition = new Vector2();
        offset = new Vector2();
        projectileStartWithOffset = new Vector2();
        PROJECTILE_MOVEMENT = new Vector2();

        firingSound = Gdx.audio.newSound(Gdx.files.internal(firingSoundPath));

    }


    @Override
    public void draw(Batch batch, float alpha) {

        if(getProjectileState() == ProjectileState.FIRING) {
            batch.draw(projectileSprite.getTexture(), projectileSprite.getX(), projectileSprite.getY(),
                    projectileSprite.getWidth(), projectileSprite.getHeight());
        }
//        if(getProjectileState() == ProjectileState.EXPLODING) {
//            batch.draw(currentFrame, explodingPosition.x, explodingPosition.y, explodingSprite.getWidth(),explodingSprite.getHeight());
//        }
    }

    @Override
    public void act(float delta) {

        switchState();
    }


    public void switchState() {

        switch (projectileState) {
            case RESET:
                projectileSprite.setPosition(getProjectileStartWithOffset().x, getProjectileStartWithOffset().y);
                playFiringSound = true;
//                playExplodingSound = true;
                break;

            case FIRING:
                PROJECTILE_MOVEMENT.x = GameScreen.getInstance().getHelper().setMovement(movementSpeedX);
                PROJECTILE_MOVEMENT.y = GameScreen.getInstance().getHelper().setMovement(movementSpeedY);
                projectileSprite.translate(PROJECTILE_MOVEMENT.x, PROJECTILE_MOVEMENT.y);
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
}
