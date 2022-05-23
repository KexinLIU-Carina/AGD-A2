package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;


public class ParticleSystem {

    public static final int MAX_PARTICLES = 128;
    public static final float EXPLOSION_LIFETIME = 0.5f;
    public static final float SMOKE_LIFETIME = 3.0f;

    public enum Type { NONE, EXPLOSION, SMOKE }

    private Texture spriteSheet;
    private TextureRegion[] explosionFrames;
    private TextureRegion[] smokeFrames;

    public Type[] type;
    public Vector2[] position;
    public Vector2[] velocity;
    public float[] lifetime;



    public ParticleSystem() {
        explosionFrames = new TextureRegion[10];
        smokeFrames = new TextureRegion[6];

        type = new Type[MAX_PARTICLES];
        position = new Vector2[MAX_PARTICLES];
        velocity = new Vector2[MAX_PARTICLES];
        lifetime = new float[MAX_PARTICLES];

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

        smokeFrames[0] = new TextureRegion(spriteSheet, 2, 306, 45, 45);
        smokeFrames[1] = new TextureRegion(spriteSheet, 52, 306, 45, 45);
        smokeFrames[2] = new TextureRegion(spriteSheet, 102, 306, 45, 45);
        smokeFrames[3] = new TextureRegion(spriteSheet, 152, 306, 45, 45);
        smokeFrames[4] = new TextureRegion(spriteSheet, 202, 306, 45, 45);
        smokeFrames[5] = new TextureRegion(spriteSheet, 252, 306, 45, 45);


        for(int index = 0; index < MAX_PARTICLES; index++) {
            type[index] = Type.NONE;
            position[index] = new Vector2();
            velocity[index] = new Vector2();
            lifetime[index] = 0.0f;
        }
    }



    public int spawn(Type type) {
        if(type == Type.NONE) {
            return -1;
        }

        int foundIndex = -1;
        for (int index = 0; index < MAX_PARTICLES; index ++) {
            if(this.type[index] == Type.NONE) {
                foundIndex = index;
                break;
            }
        }

        if(foundIndex == -1) {
            return -1;
        }

        this.type[foundIndex] = type;
        position[foundIndex].set(0, 0);
        velocity[foundIndex].set(0, 0);

        switch(type){
            case EXPLOSION:
                lifetime[foundIndex] = EXPLOSION_LIFETIME;
                break;
            case SMOKE:
                lifetime[foundIndex] = SMOKE_LIFETIME;
                velocity[foundIndex].x = MathUtils.random(-20f, 20f);
                velocity[foundIndex].y = MathUtils.random(-20f, 20f);
                break;
        }
        return foundIndex;
    }


    public void update(float deltaTime) {
        for(int index = 0; index < type.length; index++) {
            if(type[index] != Type.NONE) {
                lifetime[index] -= deltaTime;
                if(lifetime[index] <= 0) {
                    type[index] = Type.NONE;
                }
                else {
                    position[index].mulAdd(velocity[index], deltaTime);
                }
            }
        }
    }

    public void render(SpriteBatch batch) {

        for(int index = 0; index < MAX_PARTICLES; index++) {
            float frame = 0;

            if (type[index] == Type.EXPLOSION) {
                frame = 10 - (lifetime[index] / EXPLOSION_LIFETIME) * 10;
                if (frame < 0) {
                    frame = 0;
                }
                if (frame > 9) {
                    frame = 9;
                }
                batch.draw(explosionFrames[(int) frame],
                        position[index].x - explosionFrames[(int)frame].getRegionWidth() / 2,
                        position[index].y - explosionFrames[(int)frame].getRegionHeight() / 2);
            }
            if (type[index] == Type.SMOKE) {
                frame = 6 - (lifetime[index] / SMOKE_LIFETIME) * 6;
                if (frame < 0) {
                    frame = 0;
                }
                if (frame > 5) {
                    frame = 5;
                }
                batch.draw(smokeFrames[(int) frame],
                        position[index].x - smokeFrames[(int)frame].getRegionWidth() / 2,
                        position[index].y - smokeFrames[(int)frame].getRegionHeight() / 2);
            }
        }
    }

    public void dispose() {
        spriteSheet.dispose();
    }
}
