package com.mygdx.game.GameObject;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Character;
import com.mygdx.game.GameScreen;
import com.mygdx.game.MyGdxGame;


public class GameObjects extends Actor {

    private Chest[][] chests;

    private ScoreBar scoreBar;

    private Vector2 positionAmount;



    public GameObjects() {
        scoreBar = new ScoreBar();


        // Chests [levels][type]
        chests = new Chest[2][2];
        chests[0][0] = new Chest01(2000,300);
        chests[0][1] = new Chest02(2500, 750);
        chests[1][0] = new Chest03(1500, 300);
        chests[1][1] = new Chest04(2000,6500);
        positionAmount = new Vector2();

    }



    @Override
    public void draw(Batch batch, float alpha) {

        scoreBar.draw(batch, alpha);
        for (Chest chest: chests[MyGdxGame.levelNum]){
            chest.draw(batch);
        }
    }



    public void checkCollided(float x, float y) {

        for (Chest chest: chests[MyGdxGame.levelNum]){
            chest.checkCollided(x, y) ;


            ScoreBar.goldAmount += chest.getValue();
        }

    }




    public void update(boolean left, float CURRENT_MOVEMENT_SPEED) {
        positionAmount.x = GameScreen.getInstance().getHelper().setMovement(CURRENT_MOVEMENT_SPEED);
        positionAmount.y = 0;

        for (Chest chest : chests[MyGdxGame.levelNum]) {

            if (chest.getCurrentSprite() != null) {
                if (left) {
                    chest.XCollide += positionAmount.x;
                    chest.getCurrentSprite().translate(positionAmount.x, positionAmount.y);

                } else {

                    chest.XCollide -= positionAmount.x;
                    chest.getCurrentSprite().translate(-positionAmount.x, positionAmount.y);

                }
            }
        }



    }



}
