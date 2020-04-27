package com.mygdx.game.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Items.ItemDef;
import com.mygdx.game.Items.Mushroom;
import com.mygdx.game.MarioGame;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Screens.PlayScreen;

public class PowerUp extends InteractiveTileObject {
    private static TiledMapTileSet set;
    private final int BLANK_COIN =28;
    @Override
    public void onHeadHit(Mario mario) {
        if(getCell().getTile().getId() == BLANK_COIN){
            MarioGame.manager.get("Audio/Sounds/bump.wav", Sound.class).play();}
        else{
            MarioGame.manager.get("Audio/Sounds/powerup_spawn.wav", Sound.class).play();
            System.out.println(body.getPosition().x+","+body.getPosition().y);
            screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x,body.getPosition().y+16), Mushroom.class));
            Hud.addScore(400);
        }
        getCell().setTile(set.getTile(BLANK_COIN));
    }

    public PowerUp(PlayScreen screen, MapObject  mapObject) {
        super(screen, mapObject);
        set = tiledMap.getTileSets().getTileSet("worldTileSet");
        fixture.setUserData(this);
        setCategoryFilter(MarioGame.POWERUP_BIT);
    }
}
