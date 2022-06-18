package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.GameObject.ScoreBar;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;



/*
The screen that contains the actual game. It is a singleton in case other classes need to access its elements.
 */
public class GameScreen implements Screen {

    // Game
    private enum GameState {
        PLAYING, RESTART, GAMEOVER
    }

    private GameState gameState = GameState.PLAYING;
    private GameHelper helper;

    private int graphicsWidth;
    private int graphicsHeight;

    private Boolean s = true;
    private Boolean shut = true;

    // Render
    private Stage stage;

    private SpriteBatch uiBatch;
    private ShapeRenderer shapeRenderer;

    // Music
    private Music music;
    private Image image;
    private Image image1;
    private Image image2;
    private Image image3;
    private Texture texture;

    // Player
    public Player player;
    private String s1 = "0";

    // Player movement restrictions
    private boolean start = false;

    // Enemies
    private EnemyFactory enemyFactory;
    private Enemy randomEnemy;





    // Levels
    private LevelFactory levelFactory;


    private float startingPoint = 200f;
    private float mapPosition = startingPoint;


    private Image gold;
    private Image gold1;
    private Image gold2;
    private Image gold4;

    // Controller
    private Controller controller;

    private static GameScreen INSTANCE = null;



    // --- Singleton ---------------
    private GameScreen() {}

    public static GameScreen getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GameScreen();
        }
        return INSTANCE;
    }
    // ----------------------------------


    @Override
    public void show() {
        create();
    }

    public void create() {

        // Music
        Skin skin = new Skin(Gdx.files.internal("GUI/uiskin.json"));

        music = Gdx.audio.newMusic(Gdx.files.internal("Audio/Music/back.mp3"));
        music.setLooping(true);
        music.setVolume(0.2f);
        texture = new Texture(Gdx.files.internal(
                "GUI/Cartoon Sci-Fi Game GUI/Button Pack/Normal/Buttons Pack - Normal_Button Normal - Music.png"));
        image = new Image(texture);

        texture = new Texture(Gdx.files.internal(
                "GUI/Cartoon Sci-Fi Game GUI/Button Pack/Disabled/Buttons Pack - Disabled_Button Disabled - Music.png"));
        image1 = new Image(texture);

        texture = new Texture(Gdx.files.internal(
                "GUI/Cartoon Sci-Fi Game GUI/Button Pack/Normal/Buttons Pack - Normal_Button Normal - Pause.png"));
        image2 = new Image(texture);

        texture = new Texture(Gdx.files.internal(
                "GUI/Cartoon Sci-Fi Game GUI/Button Pack/Disabled/Buttons Pack - Disabled_Button Disabled - Pause.png"));
        image3 = new Image(texture);

        // texture = new Texture(Gdx.files.internal("GUI/Casual Game GUI/Icons/Casual
        // Game GUI_Icon - Diamond.png"));
        // gold = new Image(texture);

        image1.setVisible(false);
        image3.setVisible(false);
        image.setSize(80, 50);
        image1.setSize(80, 50);
        image2.setSize(80, 50);
        image3.setSize(80, 50);
        // gold.setSize(100,100);
        // gold.setPosition(200,300);


        // Convenient to set up getWidth() and getHeight() here so the are easier to use.
        graphicsWidth = Gdx.graphics.getWidth();
        graphicsHeight = Gdx.graphics.getHeight();

        // Shape renderer to draw bounding boxes.
        shapeRenderer = new ShapeRenderer();

        // SpriteBatches
        uiBatch = new SpriteBatch();

        helper = new GameHelper();

        // Level Maps
        levelFactory = new LevelFactory();
        levelFactory.setCurrentLevel();

        // Player
        player = new Player();

        // Enemy
        enemyFactory = new EnemyFactory();
        // Have to spawn an enemy at the start so that newEnemy() has something to
        // remove from the stage
        randomEnemy = enemyFactory.spawnRandomEnemy();

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
                player.setPlayerState(Player.PlayerState.IDLE);
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
                player.setPlayerState(Player.PlayerState.JUMPING);
                randomEnemy.setEnemyState(Enemy.EnemyState.IDLE);
                return true;
            }
        });


        // Controller
        controller = new Controller();


        // ----- STAGE ----------
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        stage.addActor(image);
        stage.addActor(image1);
        stage.addActor(image2);
        stage.addActor(image3);

        stage.addActor(player);
        stage.addActor(randomEnemy);
        stage.addActor(levelFactory.getGameObjects());
        stage.addActor(controller);
        // stage.addActor(gold);
        stage.addListener(new MyInputListener());



        // --- START NEW GAME ---------
        newGame();
    }

    private void newGame() {
        gameState = GameState.PLAYING;
        player.reset();
        newEnemy();
        levelFactory.getGameObjects().reset();
        mapPosition = startingPoint;
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
        controller.update(checkTouch, touchX, touchY);

        randomEnemy.setAIStates(player);
        levelFactory.getGameObjects().getLevelEnd().setAIStates(player);


        switch (gameState) {
            case PLAYING:

                // If all lives are lost it is game over
                if (player.getNumberOfLives() <= 0) {
                    gameState = GameState.GAMEOVER;
                }

                levelFactory.getGameObjects().checkCollided(player);
                levelFactory.getCurrentLevel().checkMapPlatformCollision(player);
                checkVictoryConditions();


                // ----------------- RESTRICT PLAYER MOVEMENT
                // ---------------------------------------------------------------------------------------------
                // Prevent player from going off screen to the left, or Prevent player from
                // going too far to the right

                // Map position remembers the players starting point on the map
                if (mapPosition <= startingPoint) {
                    mapPosition = startingPoint;
                    start = true;
                }
                else {
                    start = false;
                }

                // -- Screen bounds --
                // Restrict left
                if (player.getSprite().getX() < 200) {
                    player.getSprite().setX(200);
                }

                // Restrict right
                if (player.getSprite().getX() > (graphicsWidth - 600)) {
                    player.getSprite().setX(graphicsWidth - 600);
                }

                // -------------------- TOUCH CONTROLS
                // --------------------------------------------------------------
                /*
                 * Divides the screen into quadrants. Tap bottom left or bottom right to move
                 * left or right. Tap top left to jump, top right to shoot
                 * 
                 */
                if (checkTouch) {
                    // Move Left
                    if (controller.leftButton.isDown) {
                        movePlayerLeft();
                    }
                    // Move Right
                    if (controller.rightButton.isDown) {
                        movePlayerRight();
                    }
                    // Jump
                    if (controller.jumpButton.isDown) {
                        player.setPlayerState(Player.PlayerState.JUMPING);
                    }

                    // Shoot
                    if (controller.shootButton.isDown) {
                        if (player.getIsGrounded()) {

                            if (player.getPlayerProjectile().getProjectileState() == Projectile.ProjectileState.RESET) {
                                player.setPlayerState(Player.PlayerState.ATTACKING);
                            }
                        }
                    }
                }
                // If the screen is no longer being touched while the character is running, the
                // running immediately stops and is idle.
                // Other animation states have to play out their animations before stopping.
                if (!checkTouch) {
                    if (player.getPlayerState() == Player.PlayerState.RUNNING) {
                        player.setPlayerState(Player.PlayerState.IDLE);
                    }
                }

                // --------------- KEYBOARD CONTROLS
                // ----------------------------------------------
                if (s1.equals("1")) {
                    movePlayerLeft();

                } else if (s1.equals("2")) {
                    movePlayerRight();
                }

                // ------- ENEMY ------------------------------------------------------------
                 randomEnemy.setAIStates(player);

                // If the player projectile hits the enemies bounding box and the player is
                // attacking, the player has attacked the enemy.
                if (player.getPlayerProjectile().getProjectileSprite().getBoundingRectangle()
                        .overlaps(randomEnemy.getSprite().getBoundingRectangle())) {
                    if (player.getPlayerProjectile().getProjectileState() == Projectile.ProjectileState.FIRING) {
                        if (randomEnemy.getIsAlive()) {
                            player.getPlayerProjectile().setProjectileState(Projectile.ProjectileState.RESET);
                            randomEnemy.healthCheck(player.getDamage());
                        }
                    }
                }

                // If the enemy has died, remove from the stage and respawn a new enemy.
                if (randomEnemy.getEnemyState() == Enemy.EnemyState.DEAD) {

                    // Victory conditions
                    ScoreBar.enemyKilledScore+=  25;



                    randomEnemy.remove();
                    randomEnemy = enemyFactory.spawnRandomEnemy();
                    randomEnemy.reset();
                    stage.addActor(randomEnemy);
                }

                if (!player.getIsAlive()) {
                    playerDied();
                }

                break;

            // -------------------------------------------------------------------------------------

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

        stage.act(delta);
        levelFactory.getCurrentLevel().renderMap(player);


        // ----------------- ** Render the bounding boxes. ** Very useful for debugging ** ---------------------
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.rect(player.getSprite().getX(), player.getSprite().getY(), player.getSprite().getWidth(),
//                player.getSprite().getHeight());
//        shapeRenderer.rect(player.getPlayerProjectile().getProjectileSprite().getX(),
//                player.getPlayerProjectile().getProjectileSprite().getY(),
//                player.getPlayerProjectile().getProjectileSprite().getWidth(),
//                player.getPlayerProjectile().getProjectileSprite().getHeight());
//        shapeRenderer.rect(randomEnemy.getSprite().getX(), randomEnemy.getSprite().getY(),
//                randomEnemy.getSprite().getWidth(), randomEnemy.getSprite().getHeight());
//        shapeRenderer.rect(getLevel().getLevelEnd().getSprite().getX(), getLevel().getLevelEnd().getSprite().getY(), getLevel().getLevelEnd().getSprite().getWidth(),
//                getLevel().getLevelEnd().getSprite().getHeight());
//
//        for (int i = 0; i < level[MyGdxGame.levelNum].getCollisionSprites1().length; i++) {
//            shapeRenderer.rect(level[MyGdxGame.levelNum].getCollisionSprites1()[i].getX(),
//                    level[MyGdxGame.levelNum].getCollisionSprites1()[i].getY(),
//                    level[MyGdxGame.levelNum].getCollisionSprites1()[i].getWidth(),
//                    level[MyGdxGame.levelNum].getCollisionSprites1()[i].getHeight());
//            shapeRenderer.rect(level[MyGdxGame.levelNum].getCollisionSprites2()[i].getX(),
//                    level[MyGdxGame.levelNum].getCollisionSprites2()[i].getY(),
//                    level[MyGdxGame.levelNum].getCollisionSprites2()[i].getWidth(),
//                    level[MyGdxGame.levelNum].getCollisionSprites2()[i].getHeight());
//        }
//        shapeRenderer.rect(level[MyGdxGame.levelNum].getGroundRectangle().getX(), level[MyGdxGame.levelNum].getGroundRectangle().getY(), level[MyGdxGame.levelNum].getGroundRectangle().getWidth(),
//                level[MyGdxGame.levelNum].getGroundRectangle().getHeight());
//
//        shapeRenderer.setColor(Color.RED);
//        shapeRenderer.end();


        // Render the stage actors
        stage.draw();
    }



    public void movePlayerLeft() {
        if (player.getIsGrounded()) {
            // Set the player to running and move the player to the new position.
            player.setDirection(Player.Direction.LEFT);
            player.setPlayerState(Player.PlayerState.RUNNING);


            if (!start) {

                // Move the player
                player.moveCharacter();
                mapPosition -= player.getPositionAmount().x;


                // ------ CAMERA COMPENSATE -------------------------------
                // Move the camera with the player, and compensate for the movement on other objects
                levelFactory.getCurrentLevel().moveCamera(player);
                levelFactory.getCurrentLevel().collisionCompensateCamera(player.getPositionAmount().x);
                randomEnemy.compensateCamera(player.getPositionAmount().x);
                levelFactory.getGameObjects().compensateCamera(player.getPositionAmount().x);
                player.getPlayerProjectile().compensateCamera(player.getPositionAmount().x);

                if (randomEnemy.getHasProjectile()) {
                    randomEnemy.getEnemyProjectile().compensateCamera(player.getPositionAmount().x);
                }

                levelFactory.getGameObjects().update(true, player.getCURRENT_MOVEMENT_SPEED());

            }
        }
    }

    public void movePlayerRight() {
        if (player.getIsGrounded()) {
            // Set the player to running and move the player to the new position.
            player.setDirection(Player.Direction.RIGHT);
            player.setPlayerState(Player.PlayerState.RUNNING);

            // Move the player
            player.moveCharacter();
            mapPosition += player.getPositionAmount().x;


            // ------ CAMERA COMPENSATE -------------------------------
            // Move the camera with the player, and compensate for the movement on other objects

            levelFactory.getCurrentLevel().moveCamera(player);
            levelFactory.getCurrentLevel().collisionCompensateCamera(-player.getPositionAmount().x);
            randomEnemy.compensateCamera(-player.getPositionAmount().x);
            levelFactory.getGameObjects().compensateCamera(-player.getPositionAmount().x);
            player.getPlayerProjectile().compensateCamera(-player.getPositionAmount().x);

            // Guard to make sure this isn't null
            if (randomEnemy.getHasProjectile()) {
                randomEnemy.getEnemyProjectile().compensateCamera(-player.getPositionAmount().x);
            }

            levelFactory.getGameObjects().update(false, player.getCURRENT_MOVEMENT_SPEED());

        }
    }

    // The player must kill the correct number of enemies, collect enough treasure. Only after that can rescue the levelEnd.
    public void checkVictoryConditions() {

        if (shut) {
            // Defeated enemies, treasure
            // the score reaches 175 moving to next level
            if (ScoreBar.enemyKilledScore >= 175 ) {
                // Rescue the levelEnd
                if(levelFactory.getGameObjects().getLevelEnd().getIsEndReached()){
                    MyGdxGame.startScreen.setVictoryScreen1();
                    player.reset();
                    ScoreBar.enemyKilledScore = 0;
                    shut = false;
                }
            }
        }
        else {

            // the score reaches 200
            if (ScoreBar.enemyKilledScore >= 300 ) {
                if(levelFactory.getGameObjects().getLevelEnd().getIsEndReached()) {
                    MyGdxGame.startScreen.setVictoryScreen2();
                    player.reset();
                    ScoreBar.enemyKilledScore = 0;
                    shut = true;
                }
            }
        }
    }


    private class MyInputListener extends InputListener {

        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            switch (keycode) {
                case Input.Keys.P:
                    if (s) {
                        player.setPlayerState(Player.PlayerState.IDLE);
                        randomEnemy.setEnemyState(Enemy.EnemyState.IDLE);
                        s1 = "0";
                        Gdx.graphics.setContinuousRendering(false);
                        s = false;
                    } else {
                        player.setPlayerState(Player.PlayerState.JUMPING);
                        randomEnemy.setEnemyState(Enemy.EnemyState.IDLE);
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

        levelFactory.dispose();
        stage.dispose();
        uiBatch.dispose();
    }


    // ---------- GETTERS AND SETTERS -------------------------------------
    public GameHelper getHelper() { return helper; }

    public Player getPlayer() { return player; }

    public LevelFactory getLevelFactory() { return levelFactory; }
}
