package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;



public class StartScreen implements Screen {


    private Stage stage;

    private MyGdxGame game;

    private Music music;




    public StartScreen(MyGdxGame game) { this.game = game; }



    public void create() {

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Music
        music = Gdx.audio.newMusic(Gdx.files.internal("Audio/Music/back.mp3"));
        music.setLooping(true);
        music.setVolume(0.5f);
        // Sets the music to play depending on the selection in the options menu
        if(MyGdxGame.optionsScreen.getState()) {
            if (!music.isPlaying()) {
                music.play();
            }
        }

        // Buttons
        Skin skin = new Skin(Gdx.files.internal("GUI/uiskin.json"));

        Label gameTitle = new Label("Some Adventures", skin);
        final TextButton playButton = new TextButton("Play", skin, "default");
        final TextButton optionsButton = new TextButton("Options", skin, "default");
        final TextButton exitButton = new TextButton("Exit", skin, "default");

        playButton.setColor(Color.GREEN);
        optionsButton.setColor(Color.BLUE);
        exitButton.setColor(Color.RED);

        gameTitle.setFontScale(3f);
        playButton.getLabel().setFontScale(2f);
        optionsButton.getLabel().setFontScale(2f);
        exitButton.getLabel().setFontScale(2f);

        // Root table
        Table root = new Table();
        root.setFillParent(true);
        root.setBackground(skin.getDrawable("dialogDim"));

        // Buttons Table
        Table table = new Table();

        // Add the elements to the table
        gameTitle.setAlignment(Align.center);
        table.add(gameTitle).width(400).height(200).expandX();
        table.row();
        table.add(playButton).height(150).width(300).pad(10).space(20);
        table.row();
        table.add(optionsButton).height(150).width(300).pad(10).space(20);
        table.row();
        table.add(exitButton).height(150).width(300).pad(20);

        // Add the table to the root and the root to the stage.
        root.add(table);
        // Set the table to the middle of the screen
        table.setPosition((Gdx.graphics.getWidth() - table.getWidth()) / 2, (Gdx.graphics.getHeight() - table.getHeight()) / 2);
        stage.addActor(root);


        // -- BUTTON LISTENERS -------

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(GameScreen.getInstance());
            }
        });

        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(MyGdxGame.optionsScreen);
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

    }

    public Music getMusic() {
        return music;
    }

    public void setStartScreen() {
        game.setScreen(MyGdxGame.startScreen);
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
        music.dispose();
        stage.dispose();
    }


}
