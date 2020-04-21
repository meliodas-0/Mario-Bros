package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MarioGame;
import com.mygdx.game.Scenes.Hud;

public class Bricks extends InteractiveTileObject {
    public Bricks(World world, TiledMap tiledMap, Rectangle bounds) {
        super(world, tiledMap, bounds);
        fixture.setUserData(this);
        setCategoryFilter(MarioGame.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Contact", "Bricks");
        setCategoryFilter(MarioGame.DESTROYED_BIT);
        getCell().setTile(null);
        Hud.addScore(200);
    }
}
