package com.mygdx.game.GameObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

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
