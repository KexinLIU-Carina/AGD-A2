package com.mygdx.game.GameObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;



public class ScoreBar {
    private BitmapFont font;
    public int score;


    public ScoreBar(){
        score = 0;


        font = new BitmapFont();

        font.getData().setScale(5);

    }



    public void draw(Batch batch){
       font.draw(batch, "Score\n" + score, Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 100) ;

    }
}
