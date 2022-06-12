package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class OptionsScreen implements Screen {

    private MyGdxGame game;

    private Stage stage;

    private boolean state = true;

    public OptionsScreen(MyGdxGame game) {
        this.game = game;
    }


    public void create() {

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("GUI/uiskin.json"));

        final TextButton musicToggleButton = new TextButton(setButtonText(), skin, "default");
        final TextButton backButton = new TextButton("Back", skin, "default");

        musicToggleButton.getLabel().setFontScale(1.5f);
        backButton.getLabel().setFontScale(1.5f);


        Table root = new Table();
        root.setFillParent(true);
        root.setBackground(skin.getDrawable("dialogDim"));

        Table table = new Table();


        table.add(musicToggleButton).height(75).width(150).pad(20).space(50);
        table.row();
        table.row();
        table.add(backButton).height(50).width(100).pad(20);

        root.add(table);
        table.setPosition((Gdx.graphics.getWidth() - table.getWidth()) / 2, (Gdx.graphics.getHeight() - table.getHeight()) / 2);
        stage.addActor(root);

        musicToggleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(state) {
                    state = false;
                    musicToggleButton.setText(setButtonText());
                    MyGdxGame.startScreen.getMusic().stop();
                }
                else {
                    state = true;
                    musicToggleButton.setText(setButtonText());
                    MyGdxGame.startScreen.getMusic().play();
                }
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(MyGdxGame.startScreen);
            }
        });
    }

    // Change the text displayed on the button to indicate if music is on or off
    public String setButtonText() {
        if(state) {
            return "Music: ON";
        }
        else {
            return "Music: OFF";
        }
    }


    public boolean getState() {
        return state;
    }


    @Override
    public void show() {
        create();
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
