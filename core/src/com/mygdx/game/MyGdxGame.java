package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


/**
 * Team members
 *
 * Name: Kieran Hambledon
 * Email ID: hamkj008
 *
 * Name: Kexin Liu
 * Email ID: liuky042
 *
 * Name: Shiwei Liang
 * Email ID: liasy032
 */
public class MyGdxGame extends Game implements ApplicationListener {

	public static LevelFactory.LevelNum levelNum;

	public Batch batch;

	public static StartScreen startScreen;
	public static OptionsScreen optionsScreen;
	public static GameScreen gameScreen;
	public static RestartScreen restartScreen;


	@Override
	public void create () {
		batch = new SpriteBatch();

		startScreen = new StartScreen(this);
		optionsScreen = new OptionsScreen(this);
		restartScreen = new RestartScreen(this);

		gameScreen = GameScreen.getInstance();

		levelNum = LevelFactory.LevelNum.Level1;

		setScreen(startScreen);

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

