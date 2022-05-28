package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;



public class GameScreen implements Screen {


    // Game
    private enum GameState { PLAYING, RESTART, GAMEOVER }
    private GameState gameState = GameState.PLAYING;
    private GameHelper helper;
    private int graphicsWidth;
    private int graphicsHeight;

    // Render
    private Stage stage;
    private SpriteBatch uiBatch;

    // Player
    public Player player;

    // Enemies
    private EnemyFactory enemyFactory;
    private Enemy randomEnemy;

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

    private Rectangle collisionRectangle;



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

        helper = new GameHelper();

        // Player
        player = new Player();

        // Enemy
        enemyFactory = new EnemyFactory();
        randomEnemy = enemyFactory.spawnRandomEnemy();
//        randomEnemy = enemyFactory.createEnemyYeti();

        // SpriteBatches
        uiBatch = new SpriteBatch();

        // Map
        level1 = new TmxMapLoader().load("Levels/Level1/Level1.tmx");
        foregroundTiledMapRenderer = new OrthogonalTiledMapRenderer(level1, 0.5f);
        backgroundTiledMapRenderer = new OrthogonalTiledMapRenderer(level1, 0.7f);

        // Map Collision Layer
//        collisionRectangle = new Rectangle();
//        MapObject collisionObject = level1.getLayers().get("Collision").getObjects().get("GroundCollision");
//        if(collisionObject instanceof RectangleMapObject) {
//            RectangleMapObject rmo = (RectangleMapObject) collisionObject;
//            collisionRectangle = rmo.getRectangle();
//        }

        // Camera
        graphicsWidth = Gdx.graphics.getWidth();
        graphicsHeight = Gdx.graphics.getHeight();

        OrthographicCamera foregroundCamera = new OrthographicCamera();
        foregroundCamera.setToOrtho(false, graphicsWidth, graphicsHeight);
        foregroundViewport = new FitViewport(graphicsWidth, graphicsHeight, foregroundCamera);

        OrthographicCamera backgroundCamera = new OrthographicCamera();
        backgroundCamera.setToOrtho(false, graphicsWidth, graphicsHeight);
        backgroundViewport = new FitViewport(graphicsWidth, graphicsHeight, backgroundCamera);

        // Stage
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        stage.addActor(player);
        stage.addActor(randomEnemy);

        newGame();
    }


    private void newGame() {
        gameState = GameState.PLAYING;

        player.reset();
        randomEnemy.reset();
    }


    // This method sets the player to be killed and the game restarted.
    // ---COMMENT OUT THIS METHOD FOR GOD MODE ----
    public void killPlayer() {
        player.setPlayerState(Player.PlayerState.DYING);
        gameState = GameState.RESTART;
    }


    private void update(float delta) {

        boolean checkTouch = Gdx.input.isTouched();
        int touchX = Gdx.input.getX();
        int touchY = Gdx.input.getY();

        switch (gameState) {
            case PLAYING:

                // If all lives are lost it is game over
                if (player.getNumberOfLives() <= 0) {
                    gameState = GameState.GAMEOVER;
                }

                //-- RESTRICT PLAYER MOVEMENT ---------
                // Prevent player from going off screen to the left
                if (player.getSprite().getX() < 200) {
                    player.getSprite().setX(200);
                }
                // Prevent player from going too far to the right
                if (player.getSprite().getX() > (graphicsWidth / 2)) {
                    player.getSprite().setX(graphicsWidth / 2);
                }


                // -- CONTROLS --
                if (checkTouch) {
                    // Move Left
                    if ((touchX < (graphicsWidth / 2) && (touchY > (graphicsHeight / 2)))) {
                        player.setDirection(Player.Direction.LEFT);
                        player.setPlayerState(Player.PlayerState.RUNNING);
                        player.moveCharacter();

                        foregroundViewport.getCamera().translate(player.getPositionAmount().x, 0, 0);
                    }
                    // Move Right
                    if ((touchX > (graphicsWidth / 2) && (touchY > (graphicsHeight / 2)))) {
                        player.setDirection(Player.Direction.RIGHT);
                        player.setPlayerState(Player.PlayerState.RUNNING);

                        player.getPositionAmount().x = GameScreen.getInstance().getHelper().setMovement(player.getCURRENT_MOVEMENT_SPEED());
                        player.getSprite().translate(player.getPositionAmount().x, player.getPositionAmount().y);

                        foregroundViewport.getCamera().translate(player.getPositionAmount().x, 0, 0);
                    }
                    // Shoot
                    if (touchY < (graphicsHeight / 2)) {
                        if(player.getPlayerProjectile().getProjectileState() == Projectile.ProjectileState.RESET) {
                            player.setPlayerState(Player.PlayerState.ATTACKING);
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

                // -- ENEMY -------

                randomEnemy.setAIStates(player);

                // If the missile hits the enemies bounding box
                if (player.getPlayerProjectile().getProjectileSprite().getBoundingRectangle().overlaps(randomEnemy.getSprite().getBoundingRectangle())) {
                    // The enemy may have just walked into the projectile bounding box located on the player, which means the player did not shoot anything.
                    // In this case it is the player who should die, not the enemy.
                    if(randomEnemy.getSprite().getBoundingRectangle().overlaps(player.getSprite().getBoundingRectangle())) {
                        if (randomEnemy.getIsAlive()) {
                            player.healthCheck(randomEnemy.getDamage());
                            killPlayer();
                        }
                    }
                    // The player has attacked the enemy. If it is alive it should die, otherwise it is already dead.
                    else {
                        if (randomEnemy.getIsAlive()) {
                            player.getPlayerProjectile().setProjectileState(Projectile.ProjectileState.RESET);
                            randomEnemy.healthCheck(player.getDamage());
                        }
                    }
                }
                if(randomEnemy.getEnemyState() == Enemy.EnemyState.DEAD) {

                    randomEnemy.remove();
                    randomEnemy = enemyFactory.spawnRandomEnemy();
                    randomEnemy.reset();
                    stage.addActor(randomEnemy);
                }

            case RESTART:
                //Poll for input
                break;

            case GAMEOVER:
                MyGdxGame.startScreen.setStartScreen();
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

    }

    public GameHelper getHelper() { return helper; }
}
