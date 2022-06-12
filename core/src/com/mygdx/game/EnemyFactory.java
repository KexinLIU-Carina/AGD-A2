package com.mygdx.game;


import com.badlogic.gdx.math.MathUtils;


public class EnemyFactory {


    private String[] enemyArray = {
            "Dragon",
            "DevilGuy",
            "Yeti",
            "Robot",
            "Wolf"
    };


    public EnemyFactory() {}




    /*
    Selects a random enemy name from the enemyArray and then creates that enemy.
    This approach creates the enemy only when it is actually needed to save on processing, as opposed to creating and preloading all enemies at the start
     */
    public Enemy spawnRandomEnemy() {

        Enemy enemy = new Enemy();

        int randomIndex = MathUtils.random(0, enemyArray.length - 1);
        String selectedEnemy = enemyArray[randomIndex];

        switch(selectedEnemy) {
            case "Dragon":
                enemy = createEnemyDragon();
                break;
            case "DevilGuy":
                enemy = createEnemyDevilGuy();
                break;
            case "Wolf":
                enemy = createEnemyWolf();
                break;
            case "Yeti":
                enemy = createEnemyYeti();
                break;
            case "Robot":
                enemy = createEnemyRobot();
                break;
        }
        return enemy;
    }



    // --- CREATE ENEMIES ----------------------------
    public Enemy createEnemyDragon() {
        return new EnemyDragon();
    }

    public Enemy createEnemyDevilGuy() {
        return new EnemyDevilGuy();
    }

    public Enemy createEnemyYeti() {
        return new EnemyYeti();
    }

    public Enemy createEnemyRobot() {
        return new EnemyRobot();
    }

    public Enemy createEnemyWolf() {
        return new EnemyWolf();
    }








}
