package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Controller extends Actor {

    // Buttons
    public Button leftButton;
    public Button rightButton;

    public Button jumpButton;
    public Button shootButton;

    public Controller(){
        // Normal
        Texture jumpButtonImage= new Texture("GUI/Cartoon Sci-Fi Game GUI/Button Pack/Normal/Buttons Pack - Normal_Button Normal - Up Arrow.png");
        Texture leftButtonImage = new Texture("GUI/Cartoon Sci-Fi Game GUI/Button Pack/Normal/Buttons Pack - Normal_Button Normal - Left Arrow.png");
        Texture rightButtonImage = new Texture("GUI/Cartoon Sci-Fi Game GUI/Button Pack/Normal/Buttons Pack - Normal_Button Normal - Right Arrow.png");
        Texture shootButtonImage = new Texture("GUI/Cartoon Sci-Fi Game GUI/Button Pack/Normal/Buttons Pack - Normal_Button Normal - Increase.png");

        // Pressed
        Texture pressShootButton = new Texture("GUI/Cartoon Sci-Fi Game GUI/Button Pack/Disabled/Buttons Pack - Disabled_Button Disabled - Increase.png");
        Texture pressLeftButton = new Texture("GUI/Cartoon Sci-Fi Game GUI/Button Pack/Disabled/Buttons Pack - Disabled_Button Disabled - Left Arrow.png");
        Texture pressRightButton = new Texture("GUI/Cartoon Sci-Fi Game GUI/Button Pack/Disabled/Buttons Pack - Disabled_Button Disabled - Right Arrow.png");
        Texture pressJumButton= new Texture("GUI/Cartoon Sci-Fi Game GUI/Button Pack/Disabled/Buttons Pack - Disabled_Button Disabled - Up Arrow.png");

        // position
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        float buttonSize = h * 0.1f;

        leftButton = new Button(0.0f, buttonSize, buttonSize, buttonSize, leftButtonImage, pressLeftButton);
        rightButton= new Button(buttonSize*2, buttonSize, buttonSize, buttonSize, rightButtonImage, pressRightButton);
        shootButton= new Button(w - buttonSize *3, buttonSize , buttonSize, buttonSize, shootButtonImage, pressShootButton);
        jumpButton = new Button(w-buttonSize, buttonSize, buttonSize, buttonSize, jumpButtonImage, pressJumButton );


    }

    @Override
    public void draw(Batch batch, float alpha) {

        leftButton.draw(batch);
        rightButton.draw(batch);
        shootButton.draw(batch);
        jumpButton.draw(batch);
    }

    public void update(boolean checkTouch,int touchX, int touchY){
        leftButton.update(checkTouch, touchX, touchY);
        rightButton.update(checkTouch, touchX, touchY);
        jumpButton.update(checkTouch, touchX, touchY);
        shootButton.update(checkTouch,touchX,touchY);
    }

    public void dispose(){
        leftButton.dispose();
        rightButton.dispose();
        jumpButton.dispose();
        shootButton.dispose();

    }


}
