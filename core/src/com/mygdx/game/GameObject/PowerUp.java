package com.mygdx.game.GameObject;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Player;


public class PowerUp extends Actor {

    public enum PowerUpState { ACTIVE, INACTIVE }

    private PowerUpState powerUpState = PowerUpState.INACTIVE;


    private Sprite powerUpSprite;
    private Vector2 startPosition;

    // Sounds
    private Sound powerUpSound;
    private boolean playPowerUpSound = true;

    private Sound powerDownSound;
    private boolean playPowerDownSound = true;


    private float timePeriod = 0;
    // Power Up will last for 30 secs
    private int powerUpTimeDuration = 10;



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
                powerUpTimeDuration = 10;
            }
        }

        switchState();
    }

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


    public void compensateCamera(float cameraPositionAmount) {
        Gdx.app.log("Main", "power up compensate " + powerUpSprite.getX());
           powerUpSprite.translate(cameraPositionAmount, 0);
    }

    public Vector2 getStartPosition() { return startPosition; }
}
