package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;



public class EnemyFactory {



    // Enemy X and Y positions
    private final float X = Gdx.graphics.getWidth() + 100;
    private final float Y = 120;


    private Enemy[] enemyArray;


    private Enemy enemy1;
    private Enemy enemy2;
    private Enemy enemy3;
    private Enemy enemy4;
    private Enemy enemy5;




    public EnemyFactory() {
        enemyArray = new Enemy[5];


        enemy1 = new EnemyDragon(X,Y);
        enemy2 = new EnemyYeti(X,Y);
        enemy3 = new EnemyDevilGuy(X,Y);
        enemy4 = new EnemyRobot(X, Y);
        enemy5 = new EnemyWolf(X,Y);

        enemyArray[0] = enemy1;
        enemyArray[1] = enemy2;
        enemyArray[2] = enemy3;
        enemyArray[3] = enemy4;
        enemyArray[4] = enemy5;
    }




    public Enemy spawnRandomEnemy() {
        int randomIndex = MathUtils.random(0, 4);
        Gdx.app.log("Move", "Enemy: " + enemyArray[randomIndex].getName());
        return enemyArray[randomIndex];
    }

}
