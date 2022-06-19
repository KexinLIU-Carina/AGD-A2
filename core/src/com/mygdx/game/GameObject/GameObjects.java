package com.mygdx.game.GameObject;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.GameScreen;
import com.mygdx.game.LevelEnd;
import com.mygdx.game.Player;


/**
 * A collection of objects needed for each level.
 * Collecting them together allows them to be processed together.
 */
public class GameObjects extends Actor {

    private Vector2 positionAmount;

    private ChestCreator chest;
    private ScoreBar scoreBar;
    private PowerUp powerUp;
    private LevelEnd levelEnd;


    public GameObjects() {

        positionAmount = new Vector2();

        scoreBar = new ScoreBar();
        powerUp = new PowerUp("Game Objects/Diamond.png", "Audio/Sounds/PowerUp.mp3", "Audio/Sounds/PowerDown.mp3");
        chest = new ChestCreator();
        levelEnd = new LevelEnd();
    }



    @Override
    public void draw(Batch batch, float alpha) {
        scoreBar.draw(batch, alpha);
        chest.draw(batch);
        powerUp.draw(batch, alpha);
        levelEnd.draw(batch, alpha);
    }


    @Override
    public void act(float delta) {
        powerUp.act(delta);
        levelEnd.act(delta);
    }


    public void reset() {
        powerUp.reset();
        levelEnd.reset();
    }


    public void checkCollided(Player player) {

        chest.checkCollided(player.getSprite().getX(), player.getSprite().getY());
        ScoreBar.enemyKilledScore+= chest.getValue();
        powerUp.checkCollided(player);

    }


    /*
     * Moves the characters in the opposite direction to oppose the cameras movement,
     * giving the impression that they are not moving if they are static objects.
     */
    public void compensateCamera(float cameraPositionAmount) {
        powerUp.compensateCamera(cameraPositionAmount);
        levelEnd.compensateCamera(cameraPositionAmount);
    }



    public void update(boolean left, float CURRENT_MOVEMENT_SPEED){
        positionAmount.x = GameScreen.getInstance().getHelper().setMovement(CURRENT_MOVEMENT_SPEED);
        positionAmount.y = 0;

        if(left) {
            if (chest.getCurrentSprite() != null){
                chest.XCollide += positionAmount.x;
                chest.getCurrentSprite().translate(positionAmount.x, positionAmount.y);
            }
        }
        else {

            if (chest.getCurrentSprite() != null){
                chest.XCollide -= positionAmount.x;
                chest.getCurrentSprite().translate(-positionAmount.x, positionAmount.y);
            }
        }
    }

    // Creates the desired chest depending on the chest type provided.
    public void configureChest(Chest.ChestType chestType, int positionX, int positionY) {

        switch(chestType) {
            case Chest01:
                chest.createChest01(positionX, positionY);
                break;
            case Chest02:
                chest.createChest02(positionX, positionY);
                break;
            case Chest03:
                chest.createChest03(positionX, positionY);
                break;
            case Chest04:
                chest.createChest04(positionX, positionY);
                break;
        }
    }

    public int returnValue(){
        return chest.getValue();
    }


    public PowerUp getPowerUp() { return powerUp; }

    public Chest getChest() { return chest; }

    public LevelEnd getLevelEnd() { return levelEnd; }
}
