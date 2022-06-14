package com.mygdx.game.GameObject;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class Chest01 extends Chest{


    public Chest01(int x, int y){
        this.Xposition = x;
        this.Yposition = y;


        this.chestClosed =new Sprite( new Texture("Game Objects/Chests & Coins PNG/Chests/chest 01_closed.png"));
        this.chestLight = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 01_light.png"));
        this.chestCoins = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 01_coins.png"));
        this.chestOpen = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 01_open.png"));







        this.animations[0] = chestClosed;
        this.animations[1] = chestOpen;
        this.animations[2] = chestLight;
        this.animations[3] = chestCoins;

        // set size of the chest
        for (Sprite i : animations){
            i.setPosition(x,y);
            i.setSize(200, 200);
        }

        Xhelper = 100;
        Yhelper = 100;

    }
}
