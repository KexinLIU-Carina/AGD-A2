package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;



public class EnemyFactory {

    private Enemy[] enemyArray;


    private Enemy enemy1;
    private Enemy enemy2;
    private Enemy enemy3;
    private Enemy enemy4;
    private Enemy enemy5;




    public EnemyFactory() {
        enemyArray = new Enemy[5];


        enemy1 = new EnemyDragon();
        enemy2 = new EnemyYeti();
        enemy3 = new EnemyDevilGuy();
        enemy4 = new EnemyRobot();
        enemy5 = new EnemyWolf();

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
