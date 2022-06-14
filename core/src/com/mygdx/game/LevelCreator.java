package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;


/**
 * Creates levels. Loads a tmx map, then applies a tmx renderer, viewports cameras and layers.
 * Implements infinite scrolling, and parallax scrolling.
 */
public class LevelCreator {


    private int graphicsWidth;
    private int graphicsHeight;

    private TiledMap loadedMap;

    // Map Renderer
    private TiledMapRenderer foregroundTiledMapRenderer;
    private TiledMapRenderer backgroundTiledMapRenderer;

    // Viewports
    private FitViewport foregroundViewport1;
    private FitViewport foregroundViewport2;
    private FitViewport backgroundViewport1;
    private FitViewport backgroundViewport2;

    private int[] foregroundMapLayers;
    private int[] backgroundMapLayers;

    Rectangle groundRectangle;
    Rectangle[] collisionRectangle;
    Sprite[] collisionSprites;

//    private levelStart;
//    private LevelEnd levelEnd;

    private float groundLevel;


    public LevelCreator() {

        // Convenient to set up getWidth() and getHeight() here so the are easier to use.
        graphicsWidth = Gdx.graphics.getWidth();
        graphicsHeight = Gdx.graphics.getHeight();

    }

    public void createLevel(String filePath, int[] foregroundLayers, int[] backgroundLayers, int numberOfCollisionObjects) {

        loadedMap = new TmxMapLoader().load(filePath);

        foregroundTiledMapRenderer = new OrthogonalTiledMapRenderer(loadedMap);
        backgroundTiledMapRenderer = new OrthogonalTiledMapRenderer(loadedMap);
        backgroundMapLayers = backgroundLayers;
        foregroundMapLayers = foregroundLayers;


        /*
         * Map scrolling is first implemented with two viewports that will both draw the level. The first and second viewport will be drawn.
         * Once viewport 1 leaves the screen drawing stops, then the camera is teleported back behind viewport 2 while viewport 2 is being drawn.
         * Once viewport 2 leaves the screen it is teleported behind viewport 1 etc. If the player moves in the opposite direction it is the same but in reverse.
         */

        // --------- CAMERAS ------------------------------------

        // Foreground cameras. Two cameras used to execute map scrolling
        OrthographicCamera foregroundCamera1 = new OrthographicCamera();
        foregroundCamera1.setToOrtho(false, graphicsWidth, graphicsHeight);

        OrthographicCamera foregroundCamera2 = new OrthographicCamera();
        foregroundCamera2.setToOrtho(false, graphicsWidth, graphicsHeight);
        // Foreground Camera 2's starting position is behind viewport 1.
        foregroundCamera2.position.x = -graphicsWidth + (graphicsWidth / 2);

        // Background camera used for parallax scrolling
        OrthographicCamera backgroundCamera1 = new OrthographicCamera();
        backgroundCamera1.setToOrtho(false, graphicsWidth, graphicsHeight);

        OrthographicCamera backgroundCamera2 = new OrthographicCamera();
        backgroundCamera2.setToOrtho(false, graphicsWidth, graphicsHeight);
        // Background Camera 2's starting position is behind viewport 1.
        backgroundCamera2.position.x = -graphicsWidth + (graphicsWidth / 2);


        foregroundViewport1 = new FitViewport(graphicsWidth, graphicsHeight, foregroundCamera1);
        foregroundViewport2 = new FitViewport(graphicsWidth, graphicsHeight, foregroundCamera2);
        backgroundViewport1 = new FitViewport(graphicsWidth, graphicsHeight, backgroundCamera1);
        backgroundViewport2 = new FitViewport(graphicsWidth, graphicsHeight, backgroundCamera2);


        // ---------- Ground Collision ------------------------
        groundRectangle = new Rectangle();
        MapObject groundCollision = loadedMap.getLayers().get("GroundCollision").getObjects().get("GroundLevel");
        if(groundCollision instanceof RectangleMapObject) {
            RectangleMapObject rmo = (RectangleMapObject) groundCollision;
            groundRectangle = rmo.getRectangle();
        }

        groundLevel = groundRectangle.getY() + groundRectangle.getHeight();


        // ----  Platform Collision  --------------------------------
        collisionSprites = new Sprite[numberOfCollisionObjects];
        collisionRectangle = new Rectangle[numberOfCollisionObjects];
        MapLayer collisionLayer = loadedMap.getLayers().get("Collision");
        for(int i = 0; i < collisionLayer.getObjects().getCount(); i++) {
            if(collisionLayer.getObjects().get(i) instanceof RectangleMapObject) {
                RectangleMapObject rmo = (RectangleMapObject) collisionLayer.getObjects().get(i);
                collisionRectangle[i] = rmo.getRectangle();
                collisionSprites[i] = new Sprite();
                collisionSprites[i].setBounds(collisionRectangle[i].getX(), collisionRectangle[i].getY(), collisionRectangle[i].getWidth(), collisionRectangle[i].getHeight());
            }
        }
    }


    public void checkMapPlatformCollision(Player player) {

        for (Sprite collisionSprite : collisionSprites) {
            if (player.getSprite().getBoundingRectangle().overlaps(collisionSprite.getBoundingRectangle())) {
                if (player.getBounds()[0] >= collisionSprite.getY() + collisionSprite.getHeight()) {

                    player.getSprite().setY(collisionSprite.getY() + collisionSprite.getHeight());
                    player.setPlayerLevel(collisionSprite.getY() + collisionSprite.getHeight());
                    player.setIsGrounded(true);
                    player.setPlayerState(Player.PlayerState.IDLE);

                }
            }
//            else {
//                if(player.getPlayerLevel() > getGroundLevel()) {
//                    // If the player moves off the platform to the left or right
//                    if(player.getBounds()[0] < collisionSprites[i].getX() ||
//                            player.getBounds()[0] > collisionSprites[i].getX() + collisionSprites[i].getWidth()) {
//                        player.setPlayerState(Player.PlayerState.FALLING);
//                    }
//                }
//            }
        }
    }


    public void moveCamera(Player player) {

        if(player.getDirection() == Character.Direction.LEFT) {
            foregroundViewport1.getCamera().translate(-player.getPositionAmount().x, 0, 0);
            foregroundViewport2.getCamera().translate(-player.getPositionAmount().x, 0, 0);
            backgroundViewport1.getCamera().translate(-player.getPositionAmount().x / 8, 0, 0);
            backgroundViewport2.getCamera().translate(-player.getPositionAmount().x / 8, 0, 0);
        }
        else {
            foregroundViewport1.getCamera().translate(player.getPositionAmount().x, 0, 0);
            foregroundViewport2.getCamera().translate(player.getPositionAmount().x, 0, 0);
            backgroundViewport1.getCamera().translate(player.getPositionAmount().x / 8, 0, 0);
            backgroundViewport2.getCamera().translate(player.getPositionAmount().x / 8, 0, 0);
        }
    }

    public void collisionCompensateCamera(float cameraPositionAmount) {

        for (Sprite collisionSprite : collisionSprites) {
            collisionSprite.translate(cameraPositionAmount, 0);
        }
    }

    public void renderMap(Player player) {

        /*
         * Map scrolling is first implemented with two viewports that will both draw the level. The first and second viewport will be drawn.
         * Once viewport 1 leaves the screen drawing stops, then the camera is teleported back behind viewport 2 while viewport 2 is being drawn.
         * Once viewport 2 leaves the screen it is teleported behind viewport 1 etc. If the player moves in the opposite direction it is the same but in reverse.
         *
         * The same system is used separately for background layers and foreground layers, i.e there are two background viewports and two foreground viewports.
         */

        // Render the foreground
        if (player.getDirection() == Character.Direction.RIGHT) {

            // ---- BACKGROUND LAYERS ----------------------

            /* Draw the first map camera for one full length untill it is out of view.
             * Once out of view it is reset back to its original position to come into view again.
             */
            if (backgroundViewport1.getCamera().position.x > graphicsWidth + (graphicsWidth / 2)) {
                backgroundViewport1.getCamera().position.x = (-graphicsWidth / 2);
            }
            else {
                backgroundViewport1.update(graphicsWidth, graphicsHeight);
                backgroundTiledMapRenderer.setView((OrthographicCamera) backgroundViewport1.getCamera());
                backgroundTiledMapRenderer.render(backgroundMapLayers);
            }

            /* Draw the second map camera for one full length untill it is out of view.
             * Once out of view it is reset back to its original position to come into view again.
             */
            if (backgroundViewport2.getCamera().position.x > graphicsWidth + (graphicsWidth / 2)) {
                backgroundViewport2.getCamera().position.x = (-graphicsWidth / 2);
            } else {
                backgroundViewport2.update(graphicsWidth, graphicsHeight);
                backgroundTiledMapRenderer.setView((OrthographicCamera) backgroundViewport2.getCamera());
                backgroundTiledMapRenderer.render(backgroundMapLayers);
            }


            // ---- FOREGROUND LAYERS -----

            /* Draw the first map camera for one full length untill it is out of view.
             * Once out of view it is reset back to its original position to come into view again.
             */
            if (foregroundViewport1.getCamera().position.x > graphicsWidth + (graphicsWidth / 2)) {
                // Reset
                foregroundViewport1.getCamera().position.x = (-graphicsWidth / 2);
            }
            else {
                foregroundViewport1.update(graphicsWidth, graphicsHeight);
                foregroundTiledMapRenderer.setView((OrthographicCamera) foregroundViewport1.getCamera());
                foregroundTiledMapRenderer.render(foregroundMapLayers);
            }

            /* Draw the second map camera for one full length untill it is out of view.
             * Once out of view it is reset back to its original position to come into view again.
             */
            if (foregroundViewport2.getCamera().position.x > graphicsWidth + (graphicsWidth / 2)) {
                foregroundViewport2.getCamera().position.x = (-graphicsWidth / 2);
            }
            else {
                foregroundViewport2.update(graphicsWidth, graphicsHeight);
                foregroundTiledMapRenderer.setView((OrthographicCamera) foregroundViewport2.getCamera());
                foregroundTiledMapRenderer.render(foregroundMapLayers);
            }

        }

        if (player.getDirection() == Character.Direction.LEFT) {

            // ---- BACKGROUND LAYERS ----------------------

            if (backgroundViewport1.getCamera().position.x < -graphicsWidth / 2) {
                backgroundViewport1.getCamera().position.x = graphicsWidth + (graphicsWidth / 2);
            }
            else {
                backgroundViewport1.update(graphicsWidth, graphicsHeight);
                backgroundTiledMapRenderer.setView((OrthographicCamera) backgroundViewport1.getCamera());
                backgroundTiledMapRenderer.render(backgroundMapLayers);
            }

            if (backgroundViewport2.getCamera().position.x < -graphicsWidth / 2) {
                backgroundViewport2.getCamera().position.x = graphicsWidth + (graphicsWidth / 2);
            }
            else {
                backgroundViewport2.update(graphicsWidth, graphicsHeight);
                backgroundTiledMapRenderer.setView((OrthographicCamera) backgroundViewport2.getCamera());
                backgroundTiledMapRenderer.render(backgroundMapLayers);
            }


            // ---- FOREGROUND LAYERS ----------------------

            if (foregroundViewport1.getCamera().position.x < -graphicsWidth / 2) {
                foregroundViewport1.getCamera().position.x = graphicsWidth + (graphicsWidth / 2);
            }
            else {
                foregroundViewport1.update(graphicsWidth, graphicsHeight);
                foregroundTiledMapRenderer.setView((OrthographicCamera) foregroundViewport1.getCamera());
                foregroundTiledMapRenderer.render(foregroundMapLayers);
            }

            if (foregroundViewport2.getCamera().position.x < -graphicsWidth / 2) {
                foregroundViewport2.getCamera().position.x = graphicsWidth + (graphicsWidth / 2);
            }
            else {
                foregroundViewport2.update(graphicsWidth, graphicsHeight);
                foregroundTiledMapRenderer.setView((OrthographicCamera) foregroundViewport2.getCamera());
                foregroundTiledMapRenderer.render(foregroundMapLayers);
            }
        }
    }

    public void dispose() {
        loadedMap.dispose();
    }


    public TiledMap getLoadedMap() { return loadedMap; }

    public float getGroundLevel() { return groundLevel; }

    public Sprite[] getCollisionSprites() { return collisionSprites; }
}
