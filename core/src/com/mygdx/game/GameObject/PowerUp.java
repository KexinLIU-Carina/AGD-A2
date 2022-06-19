package com.mygdx.game.GameObject;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Player;


/**
 * A collectible Power Up that gives the player a more powerful rifle weapon for a short duration of 20 seconds,
 * and will then deactivate returning to the default weapon. The power up sill respawn and can be collected again.
 */
public class PowerUp extends Actor {

    public enum PowerUpState { ACTIVE, INACTIVE }

    private PowerUpState powerUpState = PowerUpState.INACTIVE;


    private Sprite powerUpSprite;
    private Vector2 startPosition;

    // Sounds
    private Sound powerUpSound;
    private boolean playPowerUpSound = false;

    private Sound powerDownSound;
    private boolean playPowerDownSound = false;


    private float timePeriod = 0;
    // Power Up will last for 20 secs
    private int powerUpTimeDuration = 20;



    public PowerUp(String filePath, String powerUpSoundPath, String powerDownSoundPath) {

        powerUpSprite = new Sprite(new Texture(filePath));
        startPosition = new Vector2();

        powerUpSound = Gdx.audio.newSound(Gdx.files.internal(powerUpSoundPath));
        powerDownSound = Gdx.audio.newSound(Gdx.files.internal(powerDownSoundPath));
    }


    @Override
    public void draw(Batch batch, float alpha) {

        if(powerUpState == PowerUpState.INACTIVE) {
            batch.draw(powerUpSprite.getTexture(), powerUpSprite.getX(), powerUpSprite.getY(),
                    powerUpSprite.getWidth(), powerUpSprite.getHeight());
        }
    }

    @Override
    public void act(float delta) {

        // Countdown for Power Up duration
        timePeriod += delta;
        if(timePeriod > 1) {
            timePeriod = 0;
            powerUpTimeDuration -= 1;

            if(powerUpTimeDuration == 0) {
                powerUpState = PowerUpState.INACTIVE;
                powerUpTimeDuration = 20;
            }
        }

        switchState();
    }

    // Sets whether the power up is activated or not.
    public void switchState() {

        switch (powerUpState) {

            case ACTIVE:
                playPowerDownSound = true;
                playPowerUpSound();
                break;

            case INACTIVE:
                playPowerUpSound = true;
                playPowerDownSound();
                break;
        }
    }

    public void reset() {
        powerUpSprite.setPosition(startPosition.x, startPosition.y);

    }

    // Check for collisions
    public void checkCollided(Player player) {
        if(player.getSprite().getBoundingRectangle().overlaps(powerUpSprite.getBoundingRectangle())) {
            powerUpState = PowerUpState.ACTIVE;
            player.setPowerUp(true);
        }

        if(powerUpState == PowerUpState.INACTIVE) {
            player.setPowerUp(false);
        }
    }

    public void playPowerUpSound() {
        if(playPowerUpSound) {
            powerUpSound.play();
            playPowerUpSound = false;
        }
    }

    public void playPowerDownSound() {
        if(playPowerDownSound) {
            powerDownSound.play();
            playPowerDownSound = false;
        }
    }

    // Moves the power up in the opposite direction to the players camera movement, giving the appearance of being a static object.
    public void compensateCamera(float cameraPositionAmount) {

           powerUpSprite.translate(cameraPositionAmount, 0);
    }


    public void dispose() {
        powerUpSound.dispose();
        powerDownSound.dispose();
    }

    public Vector2 getStartPosition() { return startPosition; }
}
