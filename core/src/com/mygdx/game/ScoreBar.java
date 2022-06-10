package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class ScoreBar {
    private Label bar;
    private Label label;
    private int score;


    public ScoreBar(){
        score = 0;

        label = new Label("Score", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        bar=new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        bar.setPosition(Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight()-100);
        label.setPosition(Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight()-100);

    }



    public void draw(Batch batch){
        label.draw(batch, 0);
        bar.draw(batch, 0);

    }
}
