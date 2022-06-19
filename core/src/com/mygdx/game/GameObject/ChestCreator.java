package com.mygdx.game.GameObject;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Can create four different types of chests with animations, specified by the chest type.
 */
public class ChestCreator extends Chest {


    public ChestCreator() {}


    public void createChest01(int x, int y) {
        this.Xposition = x;
        this.Yposition = y;
        this.XCollide = x;


        this.chestClosed = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 01_closed.png"));
        this.chestLight = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 01_light.png"));
        this.chestCoins = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 01_coins.png"));
        this.chestOpen = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 01_open.png"));

        this.chestCollectedSound = Gdx.audio.newSound(Gdx.files.internal("Audio/Sounds/Chest.mp3"));

        setAnimations(x, y);
        this.value = 100;
    }

    public void createChest02(int x, int y) {
        this.Xposition = x;
        this.Yposition = y;
        this.XCollide = x;


        this.chestClosed =new Sprite( new Texture("Game Objects/Chests & Coins PNG/Chests/chest 02_closed.png"));
        this.chestLight = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 02_light.png"));
        this.chestCoins = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 02_coins.png"));
        this.chestOpen = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 02_open.png"));

        this.chestCollectedSound = Gdx.audio.newSound(Gdx.files.internal("Audio/Sounds/Chest.mp3"));

        this.value = 200;
        setAnimations(x, y);
    }

    public void createChest03(int x, int y) {
        this.Xposition = x;
        this.Yposition = y;
        this.XCollide = x;

        this.value = 300;

        this.chestClosed =new Sprite( new Texture("Game Objects/Chests & Coins PNG/Chests/chest 03_closed.png"));
        this.chestLight = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 03_light.png"));
        this.chestCoins = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 03_coins.png"));
        this.chestOpen = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 03_open.png"));

        this.chestCollectedSound = Gdx.audio.newSound(Gdx.files.internal("Audio/Sounds/Chest.mp3"));

        setAnimations(x, y);

    }

    public void createChest04(int x, int y) {
        this.Xposition = x;
        this.Yposition = y;
        this.XCollide = x;

        this.value = 400;

        this.chestClosed =new Sprite( new Texture("Game Objects/Chests & Coins PNG/Chests/chest 04_closed.png"));
        this.chestLight = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 04_light.png"));
        this.chestCoins = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 04_coins.png"));
        this.chestOpen = new Sprite(new Texture("Game Objects/Chests & Coins PNG/Chests/chest 04_open.png"));

        this.chestCollectedSound = Gdx.audio.newSound(Gdx.files.internal("Audio/Sounds/Chest.mp3"));

        setAnimations(x, y);

    }


    public void setAnimations(int x, int y) {
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


    public void dispose() {
        chestCollectedSound.dispose();
    }

}
