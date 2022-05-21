package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
    private Enemy groundEnemy;
    private AirEnemy airEnemy;

    // Map
    private TiledMap level1;
    private TiledMapRenderer foregroundTiledMapRenderer;
    private TiledMapRenderer backgroundTiledMapRenderer;

    int[] backgroundMapLayers = {0, 1, 2};
    int[] foregroundMapLayers = {3};
    float backgroundLayerSpeed = 0.2f;
    float foregroundLayerSpeed = 1.5f;

    // Camera
    private FitViewport foregroundViewport;
    private FitViewport backgroundViewport;

    private Rectangle collisionRectangle;


    //UI Buttons
    private TextButton restartButton;
    private TextButton quitButton;
    private Table table;
    private Skin skin;


    private static GameScreen INSTANCE = null;


    private GameScreen() {}


    public static GameScreen getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new GameScreen();
        }
        return INSTANCE;
    }


    @Override
    public void show() {
        create();
    }


    public void create() {

        helper = new GameHelper();

        // Player
        player = new Player();

        // Enemies
//        groundEnemy = new GroundEnemy();
//        airEnemy = new AirEnemy();

        // SpriteBatches
        uiBatch = new SpriteBatch();

        // Map
        level1 = new TmxMapLoader().load("Levels/background/SpacePlanet.tmx");
        foregroundTiledMapRenderer = new OrthogonalTiledMapRenderer(level1, 0.7f);
        backgroundTiledMapRenderer = new OrthogonalTiledMapRenderer(level1, 0.55f);

        // Map Collision Layer
        collisionRectangle = new Rectangle();
        MapObject collisionObject = level1.getLayers().get("Collision").getObjects().get("GroundCollision");
        if(collisionObject instanceof RectangleMapObject) {
            RectangleMapObject rmo = (RectangleMapObject) collisionObject;
            collisionRectangle = rmo.getRectangle();
        }

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

        stage.addActor(player);
//        stage.addActor(groundEnemy);
//        stage.addActor(airEnemy);
        Gdx.input.setInputProcessor(stage);


        // End Game Buttons
        skin = new Skin(Gdx.files.internal("GUI/uiskin.json"));
        restartButton = new TextButton("Restart", skin, "default");
        quitButton = new TextButton("Quit", skin, "default");
        table = new Table();
        table.add(restartButton).height(75).width(150).pad(20);
        table.row();
        table.add(quitButton).height(75).width(150).pad(20).space(20);
        table.setPosition((graphicsWidth - table.getWidth()) / 2, (graphicsHeight - table.getHeight()) / 2);

        newGame();
    }


    private void newGame() {
        gameState = GameState.PLAYING;

        player.reset();
//        groundEnemy.reset();
//        airEnemy.reset();
    }


    // This method sets the player to be killed and the game restarted.
    // ---COMMENT OUT THIS METHOD FOR GOD MODE ----
    public void killPlayer() {
        player.setPlayerState(Player.PlayerState.DYING);
        gameState = GameState.RESTART;
    }


    private void update() {

        boolean checkTouch = Gdx.input.isTouched();
        int touchX = Gdx.input.getX();
        int touchY = Gdx.input.getY();

        switch (gameState) {
            case PLAYING:

                if (player.getNumberOfLives() <= 0) {
                    gameState = GameState.GAMEOVER;
                }

                // -- LAYER SCROLLING ----

                // Set the layers to scroll at different speeds.
                backgroundViewport.getCamera().translate(backgroundLayerSpeed, 0, 0);
                foregroundViewport.getCamera().translate(foregroundLayerSpeed, 0, 0);

                // Resets the layers to the start position once they have finished scrolling.
                if (backgroundViewport.getCamera().position.x > 660) {
                    backgroundViewport.getCamera().position.x = 400;
                }
                if (foregroundViewport.getCamera().position.x > 750) {
                    foregroundViewport.getCamera().position.x = 400;
                }


                //-- RESTRICT PLAYER MOVEMENT ---------

                // Prevent player from going off screen to the left
                if (player.getSprite().getX() < 0) {
                    player.getSprite().setX(0);
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
//                        player.getCurrentFrame().flip(true, false);

                        player.getPositionAmount().x = GameScreen.getInstance().getHelper().setMovement(-player.getCURRENT_MOVEMENT_SPEED());
                        player.getSprite().translate(player.getPositionAmount().x, player.getPositionAmount().y);
                    }
                    // Move Right
                    if ((touchX > (graphicsWidth / 2) && (touchY > (graphicsHeight / 2)))) {
                        player.setDirection(Player.Direction.RIGHT);
                        player.setPlayerState(Player.PlayerState.RUNNING);

                        player.getPositionAmount().x = GameScreen.getInstance().getHelper().setMovement(player.getCURRENT_MOVEMENT_SPEED());
                        player.getSprite().translate(player.getPositionAmount().x, player.getPositionAmount().y);
                    }
                    // Shoot
                    if (touchY < (graphicsHeight / 2)) {
//                        if (player.getPlayerMissile().getProjectileState() == Projectile.ProjectileState.RESET) {
                            player.setPlayerState(Player.PlayerState.ATTACKING);
//                            player.getPlayerMissile().setProjectileState(Projectile.ProjectileState.FIRING);
//                        }
                    }
                }
                if(!checkTouch) {
                    if(player.getPlayerState() == Player.PlayerState.RUNNING) {
                        player.setPlayerState(Player.PlayerState.IDLE);
                    }
                }
//

            case RESTART:
                //Poll for input
                restartButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        // Remove the restart and quit buttons from the stage and restart
                        table.remove();
                        newGame();
                    }
                });

                quitButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Gdx.app.exit();
                    }
                });
                break;

            case GAMEOVER:
                MyGdxGame.startScreen.setStartScreen();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        update();
        stage.act();

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
