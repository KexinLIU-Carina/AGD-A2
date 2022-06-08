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
        System.out.println("X: "+ x +" Y: "+ y);
        chest.checkCollided(x, y) ;
        System.out.println(chest.state);
    }
}
