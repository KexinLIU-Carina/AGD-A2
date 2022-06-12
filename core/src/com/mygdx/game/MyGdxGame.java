package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;


public class MyGdxGame extends Game implements ApplicationListener {


	public static StartScreen startScreen;
	public static OptionsScreen optionsScreen;
	public static GameScreen gameScreen;
	public static RestartScreen restartScreen;
	public static VictoryScreen victoryScreen;
	public static VictoryScreen1 victoryScreen1;


	@Override
	public void create () {

		startScreen = new StartScreen(this);
		optionsScreen = new OptionsScreen(this);
		restartScreen = new RestartScreen(this);
		victoryScreen = new VictoryScreen(this);
		victoryScreen1 = new VictoryScreen1(this);
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
		Gdx.app.log("123","123");
		super.pause();
	}

	@Override
	public void resume() {
		Gdx.app.log("1234","1234");
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

