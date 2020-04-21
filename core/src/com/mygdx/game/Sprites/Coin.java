package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MarioGame;
import com.mygdx.game.Scenes.Hud;

public class Coin extends InteractiveTileObject {
    private static TiledMapTileSet set;
    private final int BLANK_COIN =28;
    public Coin(World world, TiledMap tiledMap, Rectangle bounds) {
        super(world, tiledMap, bounds);
        set = tiledMap.getTileSets().getTileSet("worldTileSet");
        fixture.setUserData(this);
        setCategoryFilter(MarioGame.COIN_BIT);

    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Contact", "Coin");
        getCell().setTile(set.getTile(BLANK_COIN));
        Hud.addScore(100);

    }
}
