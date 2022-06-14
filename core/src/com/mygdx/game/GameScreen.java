package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.GameObject.GameObjects;
import com.mygdx.game.GameObject.ScoreBar;


/*
The screen that contains the actual game. It is a singleton in case other classes need to access its elements.
 */
public class GameScreen implements Screen {


    // Game
    private enum GameState { PLAYING, RESTART, GAMEOVER }
    private GameState gameState = GameState.PLAYING;
    private GameHelper helper;

    private int graphicsWidth;
    private int graphicsHeight;

    // Render
    private Stage stage;
    private Batch uiBatch;

    // Player
    private Player player;

    // Enemies
    private EnemyFactory enemyFactory;
    private Enemy randomEnemy;

    // LevelEnd
    private LevelEnd levelEnd;

    private LevelCreator level1;


    private Rectangle collisionRectangle;
    private ShapeRenderer shapeRenderer;


    private GameObjects gameObjects;
    private ScoreBar scoreBar;


    private boolean prohibitLeft = false;
    private boolean prohibitRight = false;
    private boolean start = false;

    private int startingPoint = 200;
    private int mapPosition = startingPoint;



    private static GameScreen INSTANCE = null;


    //  ---  Singleton --------------------
    private GameScreen() {}


    public static GameScreen getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new GameScreen();
        }
        return INSTANCE;
    }
    // ------------------------------------


    @Override
    public void show() {
        create();
    }


    public void create() {

        helper = new GameHelper();

        // SpriteBatches
        uiBatch = new SpriteBatch();

        gameObjects = new GameObjects();


        // Player
        player = new Player();

        // Enemy
        enemyFactory = new EnemyFactory();
        // Have to spawn an enemy at the start so that newEnemy() has something to remove from the stage
        randomEnemy = enemyFactory.spawnRandomEnemy();

        // Level End
        levelEnd = new LevelEnd();


        // Level Maps
        level1 = new LevelCreator();

        int[] background = {0, 1, 2, 3, 4};
        int[] foreground = {5, 6, 7, 8, 9, 10};
        level1.createLevel("Levels/Level1/Level1.tmx", foreground, background);



        // Shape renderer to draw bounding boxes.
        shapeRenderer = new ShapeRenderer();

        // Convenient to set up getWidth() and getHeight() here so the are easier to use.
        graphicsWidth = Gdx.graphics.getWidth();
        graphicsHeight = Gdx.graphics.getHeight();



        // ----- STAGE ----------
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        stage.addActor(player);
        stage.addActor(randomEnemy);
        stage.addActor(levelEnd);
        stage.addActor(gameObjects);



        // --- START NEW GAME ---------
        newGame();
    }


    private void newGame() {
        gameState = GameState.PLAYING;

        player.reset();
        newEnemy();
        levelEnd.reset();
    }


    // If the player is killed the game is restarted.
    public void playerDied() {
        gameState = GameState.RESTART;
    }


    public void newEnemy() {
        randomEnemy.remove();
        randomEnemy = enemyFactory.spawnRandomEnemy();
        randomEnemy.reset();
        stage.addActor(randomEnemy);
    }


    private void update(float delta) {

        boolean checkTouch = Gdx.input.isTouched();
        int touchX = Gdx.input.getX();
        int touchY = Gdx.input.getY();

        gameObjects.checkCollided(player.getSprite().getX(), player.getSprite().getY());


        randomEnemy.setAIStates(player);
        levelEnd.setAIStates(player);


        switch (gameState) {
            case PLAYING:

                // If all lives are lost it is game over
                if (player.getNumberOfLives() <= 0) {
                    gameState = GameState.GAMEOVER;
                }

                //----------------- RESTRICT PLAYER MOVEMENT ---------------------------------------------------------------------------------------------
                // Prevent player from going off screen to the left, or Prevent player from going too far to the right

                // ** BUGS - To fix - Player can jump outside the screen **
                if(mapPosition <= startingPoint) {
                    start = true;
                }
                else {
                    start = false;
                }

                if (player.getSprite().getX() < 200) {
                    prohibitLeft = true;
                }
                else {
                    prohibitLeft = false;
                }

                if(player.getSprite().getX() > (graphicsWidth - 600)) {
                    prohibitRight = true;
                }
                else {
                    prohibitRight = false;
                }


                // -------------------- CONTROLS --------------------------------------------------------------
                /*
                 Divides the screen into quadrants. Tap bottom left or bottom right to move left or right. Tap top left to jump, top right to shoot
                 */
                if (checkTouch) {
                    // Move Left - Touch Bottom Left quadrant to move
                    if ((touchX < (graphicsWidth / 2) && (touchY > (graphicsHeight / 2)))) {
                        // Make sure the player is on the ground - can't run if jumping
                        if(player.getIsGrounded()) {
                            // Set the player to running and move the player to the new position.
                            player.setDirection(Player.Direction.LEFT);
                            player.setPlayerState(Player.PlayerState.RUNNING);

                            if(!start) {
                                if (!prohibitLeft) {
                                    // Move the player
                                    player.moveCharacter();
                                }
                                mapPosition -= player.getPositionAmount().x;

                                // Move the camera with the player, and compensate for the movement on other objects
                                level1.moveCamera(player);
                                randomEnemy.compensateCamera(player.getPositionAmount().x);
                                levelEnd.compensateCamera(player.getPositionAmount().x);

                                gameObjects.leftUpdate(player.getPositionAmount().x);
                            }
                        }
                    }
                    // Move Right - Touch Bottom Right quadrant to move
                    if ((touchX > (graphicsWidth / 2) && (touchY > (graphicsHeight / 2)))) {
                        // Make sure the player is on the ground - can't run if jumping
                        if(player.getIsGrounded()) {
                            // Set the player to running and move the player to the new position.
                            player.setDirection(Player.Direction.RIGHT);
                            player.setPlayerState(Player.PlayerState.RUNNING);


                            if (!prohibitRight) {
                                // Move the player
                                player.moveCharacter();
                            }
                            // Move the camera with the player
                            mapPosition += player.getPositionAmount().x;

                            // Move the camera with the player, and compensate for the movement on other objects
                            level1.moveCamera(player);
                            randomEnemy.compensateCamera(-player.getPositionAmount().x);
                            levelEnd.compensateCamera(-player.getPositionAmount().x);

                            gameObjects.rightUpdate(player.getPositionAmount().x);
                        }
                    }
                    // Jump - Touch Top Left quadrant to jump
                    if (touchY < (graphicsHeight / 2) && touchX < (graphicsWidth / 2)) {
                        player.setPlayerState(Player.PlayerState.JUMPING);
                    }

                    // Shoot - Touch Top Right quadrant to shoot
                    if (touchY < (graphicsHeight / 2) && touchX > (graphicsWidth / 2)) {
                        // Make sure the player is on the ground - can't shoot if jumping
                        if(player.getIsGrounded()) {
                            if (player.getPlayerProjectile().getProjectileState() == Projectile.ProjectileState.RESET) {
                                player.setPlayerState(Player.PlayerState.ATTACKING);
                            }
                        }
                    }
                }
                // If the screen is no longer being touched while the character is running, the running immediately stops and is idle.
                // Other animation states have to play out their animations before stopping.
                if(!checkTouch) {
                    if(player.getPlayerState() == Player.PlayerState.RUNNING) {
                        player.setPlayerState(Player.PlayerState.IDLE);
                    }
                }


                // ------- ENEMY ------------------------------------------------------------

                // If the player projectile hits the enemies bounding box and the player is attacking, the player has attacked the enemy.
                if (player.getPlayerProjectile().getProjectileSprite().getBoundingRectangle().overlaps(randomEnemy.getSprite().getBoundingRectangle())) {
                    if (player.getPlayerProjectile().getProjectileState() == Projectile.ProjectileState.FIRING) {
                        if (randomEnemy.getIsAlive()) {
                            player.getPlayerProjectile().setProjectileState(Projectile.ProjectileState.RESET);
                            randomEnemy.healthCheck(player.getDamage());
                        }
                    }
                }

                // If the enemy has died, remove from the stage and respawn a new enemy.
                if(!randomEnemy.getIsAlive()) {
                    newEnemy();
                }

                if(!player.getIsAlive()) {
                    playerDied();
                }

                break;
                //-------------------------------------------------------------------------------------



            case RESTART:
                newGame();
                break;

            case GAMEOVER:
                MyGdxGame.startScreen.getMusic().stop();
                MyGdxGame.startScreen.setStartScreen();
                break;
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        update(delta);
        stage.act();


        level1.renderMap(player);



        // Render the bounding boxes. ** Very useful for debugging **
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(player.getSprite().getX(), player.getSprite().getY(), player.getSprite().getWidth(), player.getSprite().getHeight());
        shapeRenderer.rect(player.getPlayerProjectile().getProjectileSprite().getX(), player.getPlayerProjectile().getProjectileSprite().getY(), player.getPlayerProjectile().getProjectileSprite().getWidth(), player.getPlayerProjectile().getProjectileSprite().getHeight());
        shapeRenderer.rect(randomEnemy.getSprite().getX(), randomEnemy.getSprite().getY(), randomEnemy.getSprite().getWidth(), randomEnemy.getSprite().getHeight());
        shapeRenderer.rect(levelEnd.getSprite().getX(), levelEnd.getSprite().getY(), levelEnd.getSprite().getWidth(), levelEnd.getSprite().getHeight());
        shapeRenderer.end();


        // Render the stage actors
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

        level1.dispose();
        stage.dispose();
        uiBatch.dispose();
    }

    public GameHelper getHelper() { return helper; }

    public Player getPlayer() { return player; }
}
