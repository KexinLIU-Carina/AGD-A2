package com.mygdx.game.GameObject;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;



public class Chest04 extends Chest {

    public Chest04(int x, int y){
        this.Xposition = x;
        this.Yposition = y;


        this.chestClosed =new Sprite( new Texture("Game Objects/Chests & Coins PNG/Chests/chest 04_closed.png"));
        this.chestLight = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 04_light.png"));
        this.chestCoins = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 04_coins.png"));
        this.chestOpen = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 04_open.png"));



        this.animations[0] = chestClosed;
        this.animations[1] = chestOpen;
        this.animations[2] = chestLight;
        this.animations[3] = chestCoins;

        // set size of the chest
        for (Sprite i : animations){
            i.setPosition(x,y);
            i.setSize(200, 200);
        }


    }
}
