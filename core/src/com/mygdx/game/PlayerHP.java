package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class PlayerHP {
    private Sprite playerHPBackground;
    private Sprite playerHP;
    private Texture HPimage;


    public PlayerHP(){

        Texture HPBimage = new Texture("GUI/Cartoon Sci-Fi Game GUI/Misc/Cartoon Sci-Fi Game GUI_Progress Bar - Background.png");
        playerHPBackground = new Sprite( HPBimage);
        HPimage = new Texture("GUI/Cartoon Sci-Fi Game GUI/Misc/Cartoon Sci-Fi Game GUI_Progress Bar - Green.png");
        playerHP = new Sprite(HPimage);
        // TODO: need a better way to set the position
        playerHP.setPosition(150, Gdx.graphics.getHeight() - HPimage.getHeight()-40);
        playerHPBackground.setPosition(0,Gdx.graphics.getHeight() - HPBimage.getHeight());
    }



    public void modifyHP(int hp){
        playerHP.setSize(HPimage.getWidth() * hp/100, HPimage.getHeight());
    }

    public void draw(Batch batch){
        playerHPBackground.draw(batch);
        playerHP.draw(batch);
    }

    public void reset(){
        playerHP.setSize(HPimage.getWidth() , HPimage.getHeight());
    }




}
