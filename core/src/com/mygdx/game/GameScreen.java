package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
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
    public Player player;

    // Enemies
    private EnemyFactory enemyFactory;
    private Enemy randomEnemy;

    // LevelEnd
    private LevelEnd levelEnd;

    // Map
    private TiledMap level1;
    private TiledMapRenderer foregroundTiledMapRenderer;
    private TiledMapRenderer backgroundTiledMapRenderer;

    int[] backgroundMapLayers = {0, 1, 2, 3, 4};
    int[] foregroundMapLayers = {5, 6, 7, 8, 9, 10};
    float backgroundLayerSpeed = 0.2f;
    float foregroundLayerSpeed = 1.5f;

    // Camera
    private FitViewport foregroundViewport;
    private FitViewport backgroundViewport;
    private FitViewport playerViewport;

    private Rectangle collisionRectangle;
    private ShapeRenderer shapeRenderer;


    private GameObjects gameObjects;
    private ScoreBar scoreBar;



    private static GameScreen INSTANCE = null;


    //  ---  Singleton ---------------
    private GameScreen() {}


    public static GameScreen getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new GameScreen();
        }
        return INSTANCE;
    }
    // ------------------------------


    @Override
    public void show() {
        create();
    }


    public void create() {
        gameObjects = new GameObjects();


        helper = new GameHelper();

        // Player
        player = new Player();

        // Enemy
        enemyFactory = new EnemyFactory();
        // Have to spawn an enemy at the start so that newEnemy() has something to remove from the stage
        randomEnemy = enemyFactory.spawnRandomEnemy();

        // Level End
        levelEnd = new LevelEnd();

        // SpriteBatches
        uiBatch = new SpriteBatch();

        // Map
        level1 = new TmxMapLoader().load("Levels/Level1/Level1.tmx");
        foregroundTiledMapRenderer = new OrthogonalTiledMapRenderer(level1, 1f);
        backgroundTiledMapRenderer = new OrthogonalTiledMapRenderer(level1, 1f);

        // Map Collision Layer
//        collisionRectangle = new Rectangle();
//        MapObject collisionObject = level1.getLayers().get("Collision").getObjects().get("GroundCollision");
//        if(collisionObject instanceof RectangleMapObject) {
//            RectangleMapObject rmo = (RectangleMapObject) collisionObject;
//            collisionRectangle = rmo.getRectangle();
//        }

        // Shape renderer to draw bounding boxes.
        shapeRenderer = new ShapeRenderer();

        // Convenient to set up getWidth() and getHeight() here so the are easier to use.
        graphicsWidth = Gdx.graphics.getWidth();
        graphicsHeight = Gdx.graphics.getHeight();

        // Cameras. Separate cameras setup for potential to implement parallax scrolling
        OrthographicCamera foregroundCamera = new OrthographicCamera();
        foregroundCamera.setToOrtho(false, graphicsWidth, graphicsHeight);
        foregroundViewport = new FitViewport(graphicsWidth, graphicsHeight, foregroundCamera);

        OrthographicCamera backgroundCamera = new OrthographicCamera();
        backgroundCamera.setToOrtho(false, graphicsWidth, graphicsHeight);
        backgroundViewport = new FitViewport(graphicsWidth, graphicsHeight, backgroundCamera);



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
//        randomEnemy = enemyFactory.spawnRandomEnemy();
        randomEnemy = enemyFactory.createEnemyDragon();
        randomEnemy.reset();
        stage.addActor(randomEnemy);
    }


    private void update(float delta) {

        boolean checkTouch = Gdx.input.isTouched();
        int touchX = Gdx.input.getX();
        int touchY = Gdx.input.getY();

        gameObjects.checkCollided(player.getSprite().getX(), player.getSprite().getY());



        switch (gameState) {
            case PLAYING:

                // If all lives are lost it is game over
                if (player.getNumberOfLives() <= 0) {
                    gameState = GameState.GAMEOVER;
                }

                //-- RESTRICT PLAYER MOVEMENT ---------
                // Prevent player from going off screen to the left
                if (player.getSprite().getX() < 100) {
                    player.getSprite().setX(100);
                }
                // Prevent player from going too far to the right
                if (player.getSprite().getX() > (graphicsWidth - 200)) {
                    player.getSprite().setX(graphicsWidth - 200);
                }


                // ----------- CONTROLS ----------
                /*
                 Divides the screen into quadrants. Tap bottom left or bottom right to move left or right. Tap top left to jump, top right to shoot
                 ** Jump yet to be implemented **
                 */
                if (checkTouch) {
                    // Move Left - Touch Bottom Left quadrant to move
                    if ((touchX < (graphicsWidth / 2) && (touchY > (graphicsHeight / 2)))) {
                        if(player.getIsGrounded()) {
                            // Set the player to running and move the player to the new position.
                            player.setDirection(Player.Direction.LEFT);
                            player.setPlayerState(Player.PlayerState.RUNNING);
                            player.moveCharacter();

                            // Move the camera with the player
                            foregroundViewport.getCamera().translate(player.getPositionAmount().x, 0, 0);

                            gameObjects.leftUpdate(player.getPositionAmount().x);
                        }
                    }
                    // Move Right - Touch Bottom Right quadrant to move
                    if ((touchX > (graphicsWidth / 2) && (touchY > (graphicsHeight / 2)))) {
                        if(player.getIsGrounded()) {
                            // Set the player to running and move the player to the new position.
                            player.setDirection(Player.Direction.RIGHT);
                            player.setPlayerState(Player.PlayerState.RUNNING);
                            player.moveCharacter();

                            // Move the camera with the player
                            foregroundViewport.getCamera().translate(player.getPositionAmount().x, 0, 0);

                            gameObjects.rightUpdate(player.getPositionAmount().x);
                        }
                    }
                    // Jump - Touch Top Left quadrant to jump
                    if (touchY < (graphicsHeight / 2) && touchX < (graphicsWidth / 2)) {
                        player.setPlayerState(Player.PlayerState.JUMPING);
                    }

                    // Shoot - Touch Top Right quadrant to shoot
                    if (touchY < (graphicsHeight / 2) && touchX > (graphicsWidth / 2)) {
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
                randomEnemy.setAIStates(player);

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

        // Render the map
        backgroundViewport.update(graphicsWidth, graphicsHeight);
        backgroundTiledMapRenderer.setView((OrthographicCamera) backgroundViewport.getCamera());
        backgroundTiledMapRenderer.render(backgroundMapLayers);
        foregroundViewport.update(graphicsWidth, graphicsHeight);
        foregroundTiledMapRenderer.setView((OrthographicCamera) foregroundViewport.getCamera());
        foregroundTiledMapRenderer.render(foregroundMapLayers);


        // Render the bounding boxes. ** Very useful for debugging **
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(player.getSprite().getX(), player.getSprite().getY(), player.getSprite().getWidth(), player.getSprite().getHeight());
//        shapeRenderer.rect(player.getPlayerProjectile().getProjectileSprite().getX(), player.getPlayerProjectile().getProjectileSprite().getY(), player.getPlayerProjectile().getProjectileSprite().getWidth(), player.getPlayerProjectile().getProjectileSprite().getHeight());
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
}
