package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class GameHelper {

    float deltaTime;

    public GameHelper() {}



/*
This method takes in a spritesheet, as well as how many rows, columns and total frames it has, splits it into TextureRegions,
and then loads those regions into an animation. The returned animation contains all the frames.
 */
    public Animation<TextureRegion> processAnimation(String texturePath, int sheetCols, int sheetRows, int maxFrames) {
        Gdx.app.log("MyDebug: ", "processPlayerAnimation()");

        Texture texture = new Texture(texturePath);
        TextureRegion[][] temp = TextureRegion.split(texture, texture.getWidth() / sheetCols, texture.getHeight() / sheetRows);
        TextureRegion[] animationFrames = new TextureRegion[maxFrames];

        int index = 0;
        for (int i = 0; i < sheetRows; i++) {
            for (int j = 0; j < sheetCols; j++) {
                if(index < maxFrames) {
                    animationFrames[index++] = temp[i][j];
                }
            }
        }
        return new Animation<>(0.033f, animationFrames);
    }

    /*
    This method simplifies speed calculations.
    It takes a desired speed float, multiplies it by delta time and returns the result.
    */
    public float setMovement(float movementSpeed) {

        deltaTime = Gdx.graphics.getDeltaTime();

        return movementSpeed * deltaTime;
    }
}
