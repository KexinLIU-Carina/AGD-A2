package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Particle {




    private Texture spriteSheet;
    private TextureRegion[] explosionFrames;

    public Vector2 position;


    private Animation animation;


    public boolean show = false;
    private float statetime;

    private TextureRegion current;


    public Particle() {
        explosionFrames = new TextureRegion[16];

        position = new Vector2();

        init();
    }

    public void init() {
        spriteSheet = new Texture(Gdx.files.internal("explosion.png"));

        explosionFrames[0] = new TextureRegion(spriteSheet, 2, 2, 87, 87);
        explosionFrames[1] = new TextureRegion(spriteSheet, 94, 2, 87, 87);
        explosionFrames[2] = new TextureRegion(spriteSheet, 186, 2, 87, 87);
        explosionFrames[3] = new TextureRegion(spriteSheet, 278, 2, 87, 87);
        explosionFrames[4] = new TextureRegion(spriteSheet, 370, 2, 87, 87);
        explosionFrames[5] = new TextureRegion(spriteSheet, 2, 94, 87, 87);
        explosionFrames[6] = new TextureRegion(spriteSheet, 94, 94, 87, 87);
        explosionFrames[7] = new TextureRegion(spriteSheet, 186, 94, 87, 87);
        explosionFrames[8] = new TextureRegion(spriteSheet, 278, 94, 87, 87);
        explosionFrames[9] = new TextureRegion(spriteSheet, 370, 94, 87, 87);

        explosionFrames[10] = new TextureRegion(spriteSheet, 2, 306, 45, 45);
        explosionFrames[11] = new TextureRegion(spriteSheet, 52, 306, 45, 45);
        explosionFrames[12] = new TextureRegion(spriteSheet, 102, 306, 45, 45);
        explosionFrames[13] = new TextureRegion(spriteSheet, 152, 306, 45, 45);
        explosionFrames[14] = new TextureRegion(spriteSheet, 202, 306, 45, 45);
        explosionFrames[15] = new TextureRegion(spriteSheet, 252, 306, 45, 45);


        animation = new Animation(0.33f, explosionFrames);



    }



    public void spawn() {


        statetime = 0;

                show = true;

    }




    public void render(Batch batch) {
        if (show) {
            statetime += Gdx.graphics.getDeltaTime();
            current = (TextureRegion) animation.getKeyFrame(statetime, false);
            batch.draw(current, position.x, position.y);
            if (animation.getKeyFrameIndex(statetime) == 15){
                show = false;
            }

        }
    }
    public void dispose() {
        spriteSheet.dispose();
    }
}
