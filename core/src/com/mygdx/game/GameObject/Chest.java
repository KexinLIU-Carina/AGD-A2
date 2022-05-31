package com.mygdx.game.GameObject;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Map;

public abstract class Chest {


    // Four chest images
    protected Sprite chestClosed;
    protected Sprite chestCoins;
    protected Sprite chestLight;
    protected Sprite chestOpen;

    // Chest position
    protected int Xposition;
    protected int Yposition;

    // collided X Y helper
    protected int Xhelper;
    protected int Yhelper;

    // TODO: I just set this value will return something to player
    private int value;



    // Chest states
    protected enum ChestState{

        OPEN,
        CLOSE
    }

    protected ChestState state = ChestState.CLOSE;


    // TODO: need to rename
    private int i;
    private int j;


    // Chest animations
    protected Sprite[] animations = new Sprite[4];
    protected int count;



    public int checkCollided(float x, float y){

        // if the position within the scope
        if (Xposition + Xhelper >= x && x >= Xposition-Xhelper){

if (Yposition + Yhelper >= y && y >= Yposition-Yhelper){

   state = ChestState.OPEN;
   return value;
}
        }







        return 0;
    }

    public void draw(final Batch batch){
        if (state == ChestState.CLOSE){
            animations[0].draw(batch);

        }else{

            if (count < 4){
                if (j == 0){
                    j = i;
                }
              animations[count].draw(batch);
                if (j == i){

                    count++;
                    j= j + 30;
                }

            }
        }

        i++;


        // TODO: remove it
        if (i > 90){
            state = ChestState.OPEN;
        }


    }

    public void dispose(){

    }




}
