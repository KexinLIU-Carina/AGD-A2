package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
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
    private Vector2 projectileMovement;
    private Sound firingSound;
    private boolean playFiringSound = true;


    public Projectile(String texturePath, String firingSoundPath, float offsetX, float offsetY, float movementSpeedX, float movementSpeedY) {

        texture = new Texture(texturePath);
        projectileSprite = new Sprite(texture);
        projectileStartPosition = new Vector2();
        offset = new Vector2();
        projectileStartWithOffset = new Vector2();
        projectileMovement = new Vector2();

        firingSound = Gdx.audio.newSound(Gdx.files.internal(firingSoundPath));

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

    public Vector2 getProjectileMovement() { return projectileMovement; }

    public void setProjectileMovement(Vector2 projectileMovement) { this.projectileMovement = projectileMovement; }

    public Vector2 getOffset() { return offset; }
}
