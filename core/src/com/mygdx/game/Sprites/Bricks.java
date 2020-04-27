package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.mygdx.game.MarioGame;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Screens.PlayScreen;

public class Bricks extends InteractiveTileObject {
    public Bricks(PlayScreen screen, MapObject  mapObject) {
        super(screen, mapObject);
        fixture.setUserData(this);
        setCategoryFilter(MarioGame.BRICK_BIT);
    }

    @Override
    public void onHeadHit(Mario mario) {
        Gdx.app.log("Contact", "Bricks");
        setCategoryFilter(MarioGame.DESTROYED_BIT);
        getCell().setTile(null);
        Hud.addScore(200);
        MarioGame.manager.get("Audio/Sounds/breakblock.wav", Sound.class).play();

    }
}
