package com.mygdx.game.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.mygdx.game.MarioGame;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Screens.PlayScreen;

public class Coin extends InteractiveTileObject {
    private static TiledMapTileSet set;
    private final int BLANK_COIN =28;
    public Coin(PlayScreen screen, MapObject mapObject) {
        super(screen, mapObject);
        set = tiledMap.getTileSets().getTileSet("worldTileSet");
        fixture.setUserData(this);
        setCategoryFilter(MarioGame.COIN_BIT);
    }

    @Override
    public void onHeadHit(Mario mario) {
        if(getCell().getTile().getId() == BLANK_COIN){
            MarioGame.manager.get("Audio/Sounds/bump.wav", Sound.class).play();}
        else{
            MarioGame.manager.get("Audio/Sounds/coin.wav", Sound.class).play();
            System.out.println(body.getPosition().x+","+body.getPosition().y);
//            screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x,body.getPosition().y+16), Mushroom.class));
            Hud.addScore(200);
        }
        getCell().setTile(set.getTile(BLANK_COIN));

    }
}
