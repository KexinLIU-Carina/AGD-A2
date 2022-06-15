package com.mygdx.game.GameObject;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Character;
import com.mygdx.game.GameScreen;


public class GameObjects extends Actor {

    private Chest chest;

    private ScoreBar scoreBar;

    private Vector2 positionAmount;



    public GameObjects() {
        scoreBar = new ScoreBar();
        chest = new Chest01(1000,300);
        positionAmount = new Vector2();

    }



    @Override
    public void draw(Batch batch, float alpha) {

        scoreBar.draw(batch);
        chest.draw(batch);
    }



    public void checkCollided(float x, float y) {
       chest.checkCollided(x, y) ;

       scoreBar.score += chest.getValue();
    }




    public void update(boolean left, float CURRENT_MOVEMENT_SPEED){
        positionAmount.x = GameScreen.getInstance().getHelper().setMovement(CURRENT_MOVEMENT_SPEED);
        positionAmount.y = 0;

        if(left) {
            chest.getCurrentSprite().translate(positionAmount.x, positionAmount.y);
        }
        else {
            chest.getCurrentSprite().translate(-positionAmount.x, positionAmount.y);
        }
    }

}
