package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class RestartScreen implements Screen {
    Texture texture;
    Texture texture1;
    Texture texture2;
    TextureRegion textureRegion;
    TextureRegion textureRegion1;
    Image back;
    Image begin;
    Image exit;
    private Stage stage;

    private MyGdxGame game;

    public RestartScreen(MyGdxGame game) {
        this.game = game;
    }


    private void init() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        texture=new Texture(Gdx.files.internal("player/defeated.jpg"));
        texture1=new Texture(Gdx.files.internal("player/restart.jpg"));
        texture2=new Texture(Gdx.files.internal("player/exit.jpg"));

        textureRegion=new TextureRegion(texture1,0,80,1024,400);
        textureRegion1=new TextureRegion(texture2,0,40,600,250);
        back=new Image(texture);
        back.setSize(1920,1080);

        begin=new Image(textureRegion);
        exit=new Image(textureRegion1);

        begin.setSize(300,100);
        begin.setPosition(Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/4);
        exit.setSize(300,100);
        exit.setPosition(Gdx.graphics.getWidth()/3*2,Gdx.graphics.getHeight()/4);

        stage.addActor(back);
        stage.addActor(begin);
        stage.addActor(exit);

        begin.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(GameScreen.getInstance());
                return true;
            }
        });
        exit.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                return true;
            }
        });

    }


    @Override
    public void show() {
        init();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
