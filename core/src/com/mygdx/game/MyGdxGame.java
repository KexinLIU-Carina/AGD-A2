package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class MyGdxGame extends Game implements ApplicationListener {


	public Batch batch;

	public static StartScreen startScreen;
	public static OptionsScreen optionsScreen;
	public static GameScreen gameScreen;


	@Override
	public void create () {
		batch = new SpriteBatch();

		startScreen = new StartScreen(this);
		optionsScreen = new OptionsScreen(this);
		gameScreen = GameScreen.getInstance();

		setScreen(startScreen);

	}


	public void setStartScreen() {

	}


	@Override
	public void render () {
		super.render();
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void dispose () {
		super.dispose();
	}
}

