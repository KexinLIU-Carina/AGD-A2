package com.mygdx.game.GameObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class ScoreBar {

    private Label label;
    public static int enemyKilledScore = 0;
    public static int enemyKilledMax = 0;


    public ScoreBar(){
        Skin skin = new Skin(Gdx.files.internal("GUI/uiskin.json"));
        label = new Label("Score:" + enemyKilledScore , skin);
        label.setFontScale(3f);
        label.setPosition(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight() - label.getHeight() - 100);



    }




    public void draw(Batch batch, float alpha){

        label.setText("grade:" + enemyKilledScore );
        label.draw(batch, alpha);
    }
}
