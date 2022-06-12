package com.mygdx.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;


/*
The screen that contains the actual game. It is a singleton in case other classes need to access its elements.
 */
public class GameScreen implements Screen {


    // Game
    private enum GameState {PLAYING, RESTART, GAMEOVER}

    private GameState gameState = GameState.PLAYING;
    private GameHelper helper;
    private int graphicsWidth;
    private int graphicsHeight;
    private Character character = new Character();
    private Projectile projectile = new Projectile();
    private Boolean s = true;
    private Boolean shut  = true;
    // Render
    private Stage stage;
    private SpriteBatch uiBatch;
    //Music
    private Music music;
    private Image image;
    private Image image1;
    private Image image2;
    private Image image3;
    Texture texture;
    // Player
    public Player player;
    private String s1 = "0";
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
    Label label;
    private int f = 0;
    private int f1 = 0;
    private Skin skin;
    private Image gold;
    private Image gold1;
    private Image gold2;
    private Image gold4;
    private static GameScreen INSTANCE = null;


    //  ---  Singleton ---------------
    private GameScreen() {
    }


    public static GameScreen getInstance() {
        if (INSTANCE == null) {
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
        // Music
        skin = new Skin(Gdx.files.internal("GUI/uiskin.json"));
        label = new Label("Score:" + f + "\nGold value:" + f1, skin);
        label.setFontScale(3f);
        label.setPosition(0, Gdx.graphics.getHeight() - label.getHeight() - 100);

        music = Gdx.audio.newMusic(Gdx.files.internal("Audio/Music/back.mp3"));
        music.setLooping(true);
        music.setVolume(0.2f);
        texture = new Texture(Gdx.files.internal("GUI/Cartoon Sci-Fi Game GUI/Button Pack/Normal/Buttons Pack - Normal_Button Normal - Music.png"));
        image = new Image(texture);

        texture = new Texture(Gdx.files.internal("GUI/Cartoon Sci-Fi Game GUI/Button Pack/Disabled/Buttons Pack - Disabled_Button Disabled - Music.png"));
        image1 = new Image(texture);

        texture = new Texture(Gdx.files.internal("GUI/Cartoon Sci-Fi Game GUI/Button Pack/Normal/Buttons Pack - Normal_Button Normal - Pause.png"));
        image2 = new Image(texture);

        texture = new Texture(Gdx.files.internal("GUI/Cartoon Sci-Fi Game GUI/Button Pack/Disabled/Buttons Pack - Disabled_Button Disabled - Pause.png"));
        image3 = new Image(texture);

      //  texture = new Texture(Gdx.files.internal("GUI/Casual Game GUI/Icons/Casual Game GUI_Icon - Diamond.png"));
       // gold = new Image(texture);

        image1.setVisible(false);
        image3.setVisible(false);
        image.setSize(80, 50);
        image1.setSize(80, 50);
        image2.setSize(80, 50);
        image3.setSize(80, 50);
       // gold.setSize(100,100);
       // gold.setPosition(200,300);
        helper = new GameHelper();

        // Player
        player = new Player();

        // Enemy
        enemyFactory = new EnemyFactory();
        randomEnemy = enemyFactory.spawnRandomEnemy();

        // Level End
        levelEnd = new LevelEnd();

        // SpriteBatches
        uiBatch = new SpriteBatch();

        // Map
        level1 = new TmxMapLoader().load("Levels/Level1/Level1.tmx");
        foregroundTiledMapRenderer = new OrthogonalTiledMapRenderer(level1, 0.5f);
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
        image.setPosition(graphicsWidth - 250, graphicsHeight - 100);
        image1.setPosition(graphicsWidth - 250, graphicsHeight - 100);
        image2.setPosition(graphicsWidth - 160, graphicsHeight - 100);
        image3.setPosition(graphicsWidth - 160, graphicsHeight - 100);
        image.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                image.setVisible(false);
                MyGdxGame.startScreen.getMusic().stop();
                image1.setVisible(true);
                return true;
            }
        });
        image1.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                image1.setVisible(false);
                MyGdxGame.startScreen.getMusic().play();
                image.setVisible(true);
                return true;
            }
        });
        image2.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                image2.setVisible(false);
                image3.setVisible(true);
                player.setPlayerState(Player.PlayerState.IDLE1);
                randomEnemy.setEnemyState(Enemy.EnemyState.IDLE);
                s1 = "0";
                return true;
            }
        });
        image3.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                image3.setVisible(false);
                image2.setVisible(true);
                player.setPlayerState(Player.PlayerState.JUMPING1);
                randomEnemy.setEnemyState(Enemy.EnemyState.IDLE1);
                return true;
            }
        });
        // ----- STAGE ----------
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        stage.addActor(image);
        stage.addActor(image1);
        stage.addActor(image2);
        stage.addActor(image3);
        stage.addActor(player);
        stage.addActor(randomEnemy);
        stage.addActor(levelEnd);
        stage.addActor(label);
      //  stage.addActor(gold);
        stage.addListener(new MyInputListener());

        // --- START NEW GAME ---------
        newGame();
    }


    private void newGame() {
        gameState = GameState.PLAYING;
        player.reset();
        randomEnemy.reset();
        levelEnd.reset();
    }


    // If the player is killed the game is restarted.
    public void playerDied() {
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
                        // Set the player to running and move the player to the new position.
                        if (player.getIsGrounded()) {
                            player.setDirection(Player.Direction.LEFT);
                            player.setPlayerState(Player.PlayerState.RUNNING);
                            player.moveCharacter();
                            // Move the camera with the player
                            foregroundViewport.getCamera().translate(player.getPositionAmount().x, 0, 0);
                        }
                    }
                    // Move Right - Touch Bottom Right quadrant to move
                    if ((touchX > (graphicsWidth / 2) && (touchY > (graphicsHeight / 2)))) {
                        // Set the player to running and move the player to the new position.
                        if (player.getIsGrounded()) {
                            player.setDirection(Player.Direction.RIGHT);
                            player.setPlayerState(Player.PlayerState.RUNNING);
                            player.moveCharacter();

                            // Move the camera with the player
                            foregroundViewport.getCamera().translate(player.getPositionAmount().x, 0, 0);
                        }
                    }
                    // Jump - Touch Top Left quadrant to jump
                    if (touchY < (graphicsHeight / 2) && touchX < (graphicsWidth / 2)) {
                        player.setPlayerState(Player.PlayerState.JUMPING);
                    }

                    // Shoot - Touch Top Right quadrant to shoot
                    if (touchY < (graphicsHeight / 2) && touchX > (graphicsWidth / 2) && touchX < (graphicsWidth / 4 * 3)) {
                        if (player.getIsGrounded()) {
                            if (player.getPlayerProjectile().getProjectileState() == Projectile.ProjectileState.RESET) {
                                player.setPlayerState(Player.PlayerState.ATTACKING);
                            }
                        }
                    }
                }
                // If the screen is no longer being touched while the character is running, the running immediately stops and is idle.
                // Other animation states have to play out their animations before stopping.
                if (!checkTouch) {
                    if (player.getPlayerState() == Player.PlayerState.RUNNING) {
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

                // If the enemy has dies, remove from the stage and respawn a new enemy.
                if (randomEnemy.getEnemyState() == Enemy.EnemyState.DEAD) {
                    f=f+25;
                    f1=f1+1;
                    label.setText("grade:" + f + "\nGold value:" + f1);
                    if (shut){
                        if (f>=75&&f1>=1){
                            MyGdxGame.startScreen.setStartScreen1();
                            player.setPlayerState(Player.PlayerState.IDLE);
                            player.reset();
                            f=0;
                            f1=0;
                            shut=false;
                        }
                    }else {
                        if (f>=100&&f1>=8){
                            MyGdxGame.startScreen.setStartScreen2();
                            player.setPlayerState(Player.PlayerState.IDLE);
                            player.reset();
                            f=0;
                            f1=0;
                            shut=true;
                        }
                    }
                    randomEnemy.remove();
                    randomEnemy = enemyFactory.spawnRandomEnemy();
                    randomEnemy.reset();
                    stage.addActor(randomEnemy);
                }

                if (!player.getIsAlive()) {
                    playerDied();
                }

                break;
            //-------------------------------------------------------------------------------------

            case RESTART:
                newGame();
                break;

            case GAMEOVER:

                MyGdxGame.startScreen.setStartScreen();
                break;
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        update(delta);
        if (s1.equals("1")) {
            if (player.getIsGrounded()) {
                player.setDirection(Player.Direction.LEFT);
                player.setPlayerState(Player.PlayerState.RUNNING);
                player.moveCharacter();
                // Move the camera with the player
                foregroundViewport.getCamera().translate(player.getPositionAmount().x, 0, 0);
            }

        } else if (s1.equals("2")) {
            if (player.getIsGrounded()) {
                player.setDirection(Player.Direction.RIGHT);
                player.setPlayerState(Player.PlayerState.RUNNING);
                player.moveCharacter();
                // Move the camera with the player
                foregroundViewport.getCamera().translate(player.getPositionAmount().x, 0, 0);
            }
        }
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
        shapeRenderer.rect(player.getPlayerProjectile().getProjectileSprite().getX(), player.getPlayerProjectile().getProjectileSprite().getY(), player.getPlayerProjectile().getProjectileSprite().getWidth(), player.getPlayerProjectile().getProjectileSprite().getHeight());
        shapeRenderer.rect(randomEnemy.getSprite().getX(), randomEnemy.getSprite().getY(), randomEnemy.getSprite().getWidth(), randomEnemy.getSprite().getHeight());
        shapeRenderer.rect(levelEnd.getSprite().getX(), levelEnd.getSprite().getY(), levelEnd.getSprite().getWidth(), levelEnd.getSprite().getHeight());
        shapeRenderer.end();
        // Render the stage actors
        stage.draw();


    }


    private class MyInputListener extends InputListener {


        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            switch (keycode) {
                case Input.Keys.P:
                    if (s) {
                        player.setPlayerState(Player.PlayerState.IDLE1);
                        randomEnemy.setEnemyState(Enemy.EnemyState.IDLE);
                        s1 = "0";
                        Gdx.graphics.setContinuousRendering(false);
                        s = false;
                    } else {
                        player.setPlayerState(Player.PlayerState.JUMPING1);
                        randomEnemy.setEnemyState(Enemy.EnemyState.IDLE1);
                        Gdx.graphics.setContinuousRendering(true);
                        Gdx.graphics.requestRendering();
                        s = true;
                    }
                    break;
                case Input.Keys.A:
                    s1 = "1";
                    break;
                case Input.Keys.D:
                    s1 = "2";
                    break;
                case Input.Keys.S:
                    s1 = "0";
                    break;
                case Input.Keys.K:
                    s1 = "0";
                    player.setPlayerState(Player.PlayerState.JUMPING);
                    break;
                case Input.Keys.J:
                    s1 = "0";
                    if (player.getIsGrounded()) {
                        if (player.getPlayerProjectile().getProjectileState() == Projectile.ProjectileState.RESET) {
                            player.setPlayerState(Player.PlayerState.ATTACKING);
                        }
                    }
                    break;
                case Input.Keys.Q:
                    if (player.getPowerUp()) {
                        player.setPowerUp(false);
                    } else {
                        player.setPowerUp(true);
                    }
                    break;

            }
            return false;
        }
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

    public GameHelper getHelper() {
        return helper;
    }
}
