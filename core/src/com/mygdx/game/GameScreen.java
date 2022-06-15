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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.GameObject.GameObjects;
import com.mygdx.game.GameObject.ScoreBar;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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

    // Music
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

    private LevelCreator[] level;

    private Rectangle collisionRectangle;
    private ShapeRenderer shapeRenderer;

    private GameObjects gameObjects;
    private ScoreBar scoreBar;

    private boolean prohibitLeft = false;
    private boolean prohibitRight = false;
    private boolean start = false;

    private int startingPoint = 200;
    private int mapPosition = startingPoint;


    Label label;
    private int f = 0;
    private int f1 = 0;
    private Skin skin;
    private Image gold;
    private Image gold1;
    private Image gold2;
    private Image gold4;


    // Controller
    private Controller controller;



    private static GameScreen INSTANCE = null;

    // --- Singleton ---------------
    private GameScreen() {
    }

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
        skin = new Skin(Gdx.files.internal("GUI/uiskin.json"));
        label = new Label("Score:" + f + "\nGold value:" + f1, skin);
        label.setFontScale(3f);
        label.setPosition(0, Gdx.graphics.getHeight() - label.getHeight() - 100);

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
        level = new LevelCreator[1];
        level[0] = new LevelCreator();

        int[] background = { 0, 1, 2, 3, 4 };
        int[] foreground = { 5, 6, 7, 8, 9, 10, 11 };
        level[0].createLevel("Levels/Level1/Level1.tmx", foreground, background, 4);
        // TODO
        int[] level2background={0};
        int[] level2foreground ={1,2,3,4,5};
//        level[0].createLevel("Levels/Level1/level2.tmx", level2foreground, level2background, 1 );



        gameObjects = new GameObjects();

        // Player
        player = new Player();

        // Enemy
        enemyFactory = new EnemyFactory();
        // Have to spawn an enemy at the start so that newEnemy() has something to remove from the stage
        randomEnemy = enemyFactory.spawnRandomEnemy();

        // Level End
        levelEnd = new LevelEnd();


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
        stage.addActor(levelEnd);
        stage.addActor(gameObjects);
        stage.addActor(controller);
        stage.addActor(label);
        // stage.addActor(gold);
        stage.addListener(new MyInputListener());


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
        controller.update(checkTouch, touchX, touchY);


        gameObjects.checkCollided(player.getSprite().getX(), player.getSprite().getY());
        randomEnemy.setAIStates(player);
        levelEnd.setAIStates(player);

        level[0].checkMapPlatformCollision(player);


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


                // -------------------- TOUCH CONTROLS --------------------------------------------------------------
                /*
                 Divides the screen into quadrants. Tap bottom left or bottom right to move left or right. Tap top left to jump, top right to shoot

                 */
                if (checkTouch) {
                    // Move Left - Touch Bottom Left quadrant to move
//                    if ((touchX < (graphicsWidth / 2) && (touchY > (graphicsHeight / 2)))) {
//                        // Make sure the player is on the ground - can't run if jumping
//                        movePlayerLeft();
//                    }
                    if (controller.leftButton.isDown){
                        controller.leftButton.isDown = true;
                        movePlayerLeft();
                    }
                    // Move Right - Touch Bottom Right quadrant to move
//                    if ((touchX > (graphicsWidth / 2) && (touchY > (graphicsHeight / 2)))) {
//                        // Make sure the player is on the ground - can't run if jumping
//                        movePlayerRight();
//                    }

                    if (controller.rightButton.isDown){
                        controller.rightButton.isDown = true;
                        movePlayerRight();
                    }
                    // Jump - Touch Top Left quadrant to jump
//                    if (touchY < (graphicsHeight / 2) && touchX < (graphicsWidth / 2)) {
//                        player.setPlayerState(Player.PlayerState.JUMPING);
//                    }
                    if (controller.jumpButton.isDown){
                        controller.jumpButton.isDown = true;
                        player.setPlayerState(Player.PlayerState.JUMPING);
                    }

                    // Shoot - Touch Top Right quadrant to shoot
//                    if (touchY < (graphicsHeight / 2) && touchX > (graphicsWidth / 2)) {
//                        // Make sure the player is on the ground - can't shoot if jumping
//                        if(player.getIsGrounded()) {
//
//                            if (player.getPlayerProjectile().getProjectileState() == Projectile.ProjectileState.RESET) {
//                                player.setPlayerState(Player.PlayerState.ATTACKING);
//                            }
//                        }
//                    }

                    if (controller.shootButton.isDown){
                        controller.shootButton.isDown = true;
                        if(player.getIsGrounded()) {

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


                // --------------- KEYBOARD CONTROLS ----------------------------------------------
                if (s1.equals("1")) {
                    movePlayerLeft();

                } else if (s1.equals("2")) {
                    movePlayerRight();
                }


                // ------- ENEMY ------------------------------------------------------------
                // randomEnemy.setAIStates(player);

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
                if (randomEnemy.getEnemyState() == Enemy.EnemyState.DEAD) {

                    f = f + 25;
                    f1 = f1 + 1;

                    label.setText("grade:" + f + "\nGold value:" + f1);

                    if (shut){
                        if (f >= 75 && f1 >= 1){
                            MyGdxGame.startScreen.setVictoryScreen1();
                            player.setPlayerState(Player.PlayerState.IDLE);
                            player.reset();
                            f = 0;
                            f1 = 0;
                            shut = false;
                        }
                    }
                    else {
                        if (f >=100 && f1 >= 8){
                            MyGdxGame.startScreen.setVictoryScreen2();
                            player.setPlayerState(Player.PlayerState.IDLE);
                            player.reset();
                            f = 0;
                            f1 = 0;
                            shut = true;
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
        level[0].renderMap(player);

        // Render the bounding boxes. ** Very useful for debugging **
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(player.getSprite().getX(), player.getSprite().getY(), player.getSprite().getWidth(), player.getSprite().getHeight());
        shapeRenderer.rect(player.getPlayerProjectile().getProjectileSprite().getX(), player.getPlayerProjectile().getProjectileSprite().getY(), player.getPlayerProjectile().getProjectileSprite().getWidth(), player.getPlayerProjectile().getProjectileSprite().getHeight());
        shapeRenderer.rect(randomEnemy.getSprite().getX(), randomEnemy.getSprite().getY(), randomEnemy.getSprite().getWidth(), randomEnemy.getSprite().getHeight());
        shapeRenderer.rect(levelEnd.getSprite().getX(), levelEnd.getSprite().getY(), levelEnd.getSprite().getWidth(), levelEnd.getSprite().getHeight());

        for(int i = 0; i < level[0].getCollisionSprites().length; i++) {
            shapeRenderer.rect(level[0].getCollisionSprites()[i].getX(), level[0].getCollisionSprites()[i].getY(), level[0].getCollisionSprites()[i].getWidth(), level[0].getCollisionSprites()[i].getHeight());
        }

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.end();

        // Render the stage actors
        stage.draw();


    }

    public void movePlayerLeft() {
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
                level[0].moveCamera(player);
                level[0].collisionCompensateCamera(player.getPositionAmount().x);
                randomEnemy.compensateCamera(player.getPositionAmount().x);
                levelEnd.compensateCamera(player.getPositionAmount().x);

                player.getPlayerProjectile().compensateCamera(player.getPositionAmount().x);
                if(randomEnemy.getHasProjectile()) {
                    randomEnemy.getEnemyProjectile().compensateCamera(player.getPositionAmount().x);
                }


                gameObjects.update(true, player.getCURRENT_MOVEMENT_SPEED());


            }
        }
    }

    public void movePlayerRight() {
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
            level[0].moveCamera(player);
            level[0].collisionCompensateCamera(-player.getPositionAmount().x);
            randomEnemy.compensateCamera(-player.getPositionAmount().x);
            levelEnd.compensateCamera(-player.getPositionAmount().x);

            player.getPlayerProjectile().compensateCamera(-player.getPositionAmount().x);
            // Guard to make sure this isn't null
            if(randomEnemy.getHasProjectile()) {
                randomEnemy.getEnemyProjectile().compensateCamera(-player.getPositionAmount().x);
            }

            gameObjects.update(false, player.getCURRENT_MOVEMENT_SPEED());

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

        for (LevelCreator levelCreator : level) {
            levelCreator.dispose();
        }
        stage.dispose();
        uiBatch.dispose();
    }

    public GameHelper getHelper() {
        return helper;
    }

    public Player getPlayer() {
        return player;
    }

    public LevelCreator getLevel() {
        return level[0];
    }
}
