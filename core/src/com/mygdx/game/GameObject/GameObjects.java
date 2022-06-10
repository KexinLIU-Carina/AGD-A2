package com.mygdx.game.GameObject;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class GameObjects extends Actor {
    private Chest chest;


    public GameObjects() {
        chest = new Chest01(1000,300);

    }



    @Override
    public void draw(Batch batch, float alpha) {


        chest.draw(batch);
    }



    public void checkCollided(float x, float y) {
       chest.checkCollided(x, y) ;
    }


    public void leftUpdate(float x){
        chest.update(chest.Xposition - x , chest.Yposition);

    }

    public void rightUpdate(float x){
        chest.update(chest.Xposition + x, chest.Yposition);
    }

}
