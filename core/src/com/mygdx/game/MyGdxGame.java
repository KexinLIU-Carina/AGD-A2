package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;



public class MyGdxGame extends Game implements ApplicationListener {


	public static StartScreen startScreen;
	public static OptionsScreen optionsScreen;
	public static GameScreen gameScreen;


	@Override
	public void create () {

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

