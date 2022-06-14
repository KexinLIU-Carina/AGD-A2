package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class LevelEnd extends Character {

    public enum GoalType { PRINCESS, BABY }
    public enum GoalState { IDLE, SPELL }

    private GoalType goalType = GoalType.BABY;
    private GoalState goalState = GoalState.IDLE;

    private Direction direction = Direction.LEFT;


    // ---- ANIMATIONS CONTAINERS -------------------------
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> spellAnimation;


    // ---- ANIMATIONS -------------------------
    private Animation<TextureRegion> princessIdleAnimation;
    private Animation<TextureRegion> princessSpellAnimation;
    private Animation<TextureRegion> babyIdleAnimation;
    private Animation<TextureRegion> babySpellAnimation;


    public LevelEnd() {

        // Initialize size and start position
        super.setDirection(Direction.LEFT);
        super.getStartPosition().x = 5000;


        // ---- ANIMATIONS -------------------------
        // Load all animation frames into animation objects using Game Helper.
        princessIdleAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/LevelEnd/Princess/Idle Blinking.png", 8, 3, 24);
        princessSpellAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/LevelEnd/Princess/Spell.png", 5, 3, 15);

        babyIdleAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/LevelEnd/Super Baby/Idle Blinking.png", 6, 3, 18);
        babySpellAnimation = GameScreen.getInstance().getHelper().processAnimation("Game Characters/LevelEnd/Super Baby/Spell.png", 4, 3, 12);
    }


    @Override
    public void draw(Batch batch, float alpha) {

        super.draw(batch, alpha);
    }

    @Override
    public void act(float delta) {

        switchStates();
    }

    public void reset() {
        super.getSprite().setPosition(getStartPosition().x, getStartPosition().y);
    }


    public void switchStates() {

        if (goalType == GoalType.PRINCESS) {
            idleAnimation = princessIdleAnimation;
            spellAnimation = princessSpellAnimation;
        }

        if (goalType == GoalType.BABY) {
            idleAnimation = babyIdleAnimation;
            spellAnimation = babySpellAnimation;
        }

        if(goalState == GoalState.IDLE) {
            super.loopingAnimation(idleAnimation);
        }
        if(goalState == GoalState.SPELL) {
            if(super.nonLoopingAnimation(spellAnimation)) {
                // changeLevel()
                // *** Put code here to change the level
            }
        }
    }

    // Adds additional AI states specific to this enemy, primarily its Attack state
    public void setAIStates(Player player) {

        if (player.getIsAlive()) {

            // The enemy is meant to turn around if the player goes past it.
            // ** Bugs ** At the moment with the camera movement this is prevented from happening
            if (player.getCenteredSpritePosition().x > getCenteredSpritePosition().x && distanceFromPlayer(player) < 1000) {
                setDirection(Direction.RIGHT);
            } else {
                setDirection(Direction.LEFT);
            }
        }

        // ----- End Level Condition -------------------
        // If the players reaches the endGoal, so that the bounding boxes intersect then the level end goal has been reached.
        if(GameScreen.getInstance().getPlayer().getSprite().getBoundingRectangle().overlaps(getSprite().getBoundingRectangle())) {
            goalState = GoalState.SPELL;
        }
    }

}
