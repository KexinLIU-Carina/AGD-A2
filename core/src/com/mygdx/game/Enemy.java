package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;



public class Enemy extends Character {


    public enum EnemyType { GROUND, AIR }

    private EnemyType type;
    private String name;
    private boolean hasProjectile = false;






    // ---------- GETTERS AND SETTERS -------------------------------------
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public EnemyType getType() { return type; }

    public void setType(EnemyType type) { this.type = type; }

    public boolean isHasProjectile() { return hasProjectile; }

    public void setHasProjectile(boolean hasProjectile) { this.hasProjectile = hasProjectile; }
}
