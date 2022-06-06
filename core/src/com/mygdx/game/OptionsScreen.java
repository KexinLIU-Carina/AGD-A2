package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

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

        musicToggleButton.getLabel().setFontScale(2f);
        backButton.getLabel().setFontScale(2f);


        Table root = new Table();
        root.setFillParent(true);
        root.setBackground(skin.getDrawable("dialogDim"));

        Table table = new Table();


        table.add(musicToggleButton).height(150).width(300).pad(20).space(100);
        table.row();
        table.row();
        table.add(backButton).height(100).width(200).pad(20);

        root.add(table);
        table.setPosition((Gdx.graphics.getWidth() - table.getWidth()) / 2, (Gdx.graphics.getHeight() - table.getHeight()) / 2);
        stage.addActor(root);

        musicToggleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(MyGdxGame.startScreen.getMusic().isPlaying()) {
                    state = false;
                    musicToggleButton.setText(setButtonText());
                    MyGdxGame.startScreen.getMusic().stop();
                }
                else {
                    state = true;
                    musicToggleButton.setText(setButtonText());
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
