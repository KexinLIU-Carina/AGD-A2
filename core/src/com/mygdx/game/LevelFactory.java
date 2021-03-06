package com.mygdx.game;


import com.mygdx.game.GameObject.Chest;
import com.mygdx.game.GameObject.GameObjects;
import com.mygdx.game.GameObject.ScoreBar;


/**
 * Contains the configuration logic needed to create an entire level, with map and game objects.
 * New levels can be easily added and configured here.
 * The correct current level is created automatically by calling createCurrentLevel which will overwrite the current level container.
 */
public class LevelFactory {

    public enum LevelNum { Level1, Level2 }

    private LevelCreator currentLevel;
    private GameObjects currentLevelObjects;


    public LevelFactory() {}



    public void createLevel1() {

        int[] background = { 0, 1, 2, 3, 4 };
        int[] foreground = { 5, 6, 7, 8, 9, 10, 11 };
        currentLevel = new LevelCreator();
        currentLevel.createLevel("Levels/Level1/Level1.tmx", foreground, background, 4);

        currentLevelObjects = new GameObjects();
        currentLevelObjects.getPowerUp().getStartPosition().set(1300, 800);
        currentLevelObjects.configureChest(Chest.ChestType.Chest01, 2500, 800);
        currentLevelObjects.getLevelEnd().setGoalType(LevelEnd.GoalType.BABY);

        ScoreBar.enemyKilledMax = 175;

    }

    public void createLevel2() {

        int[] background = { 0 };
        int[] foreground = { 1, 2, 3, 4, 5 };
        currentLevel = new LevelCreator();
        currentLevel.createLevel("Levels/Level1/level2.tmx", foreground, background, 1);

        currentLevelObjects = new GameObjects();
        currentLevelObjects.getPowerUp().getStartPosition().set(2400, 600);
        currentLevelObjects.configureChest(Chest.ChestType.Chest02, 500, 600);
        currentLevelObjects.getLevelEnd().setGoalType(LevelEnd.GoalType.PRINCESS);

        ScoreBar.enemyKilledMax = 300;
    }



    // Creates whatever level MyGdxGame calls the current level
    public void createCurrentLevel() {
        if(MyGdxGame.levelNum == LevelNum.Level1) {
            createLevel1();
        }
        if(MyGdxGame.levelNum == LevelNum.Level2) {
            createLevel2();
        }
    }

    public void dispose() {
        currentLevel.dispose();
    }


    // ---------- GETTERS AND SETTERS -------------------------------------

    public LevelCreator getCurrentLevel() { return currentLevel; }

    public GameObjects getGameObjects() { return currentLevelObjects; }
}
