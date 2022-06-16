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
        chest = new Chest01(2000,300);
        positionAmount = new Vector2();

    }



    @Override
    public void draw(Batch batch, float alpha) {

        scoreBar.draw(batch, alpha);
        chest.draw(batch);
    }



    public void checkCollided(float x, float y) {

               chest.checkCollided(x, y) ;
               ScoreBar.goldAmount += chest.getValue();

    }




    public void update(boolean left, float CURRENT_MOVEMENT_SPEED){
        positionAmount.x = GameScreen.getInstance().getHelper().setMovement(CURRENT_MOVEMENT_SPEED);
        positionAmount.y = 0;

        if(left) {
            if (chest.getCurrentSprite() != null){
                chest.XCollide += positionAmount.x;
            chest.getCurrentSprite().translate(positionAmount.x, positionAmount.y);}
        }
        else {

            if (chest.getCurrentSprite() != null){
                chest.XCollide -= positionAmount.x;
            chest.getCurrentSprite().translate(-positionAmount.x, positionAmount.y);
                }
        }



    }

    public int returnValue(){
        return chest.getValue();
    }

}
