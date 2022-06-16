package com.mygdx.game.GameObject;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;



public abstract class Chest {

    public enum ChestType { Chest01, Chest02, Chest03, Chest04 }
    private ChestType chestType = ChestType.Chest01;

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


    protected int XCollide;

    // TODO: I just set this value will return something to player
    private int value = 100;

    private boolean hasReturn = false;


    // Chest states
    protected enum ChestState{

        OPEN,
        CLOSE
    }

    protected ChestState state = ChestState.CLOSE;


    // TODO: need to rename
    private int i = 0;
    private int j = 0;


    // Chest animations
    protected Sprite[] animations = new Sprite[4];
    protected int count;



    public int checkCollided(float x, float y){


        // if the player is on the position
        if (XCollide+ Xhelper >= x && x >= XCollide-Xhelper ){

            if (Yposition + Yhelper >= y && y >= Yposition-Yhelper){

                state = ChestState.OPEN;
                return value;
            }
        }
        return 0;
    }



    public void draw(Batch batch){
        if (state == ChestState.CLOSE){
            animations[0].draw(batch);

        }else{

            // if the chest is opened

            if (count < 4){
                if (j == 0){
                    j = i;
                }
              animations[count].draw(batch);

                // wait for 30 times to go next animation
                if (j == i){

                    count++;

                    // internal time
                    j= j + 30;
                }
            }
        }

        i++;


        // TODO: remove it
//        if (i > 90){
//            state = ChestState.OPEN;
//        }


    }

    public void dispose(){

    }

    public void update(float x, float y){
        for (Sprite sprite: animations){
            sprite.setPosition(x, y);
        }
    }

    public int getValue(){
        if (state == ChestState.OPEN && !hasReturn){
            hasReturn = true;
            return value;
        }
        return 0;
    }

    public Sprite getCurrentSprite(){
        if (count < animations.length){
            return animations[count];
        }
        return null;
    }

}
