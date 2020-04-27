package com.mygdx.game.Sprites;

import com.badlogic.gdx.maps.MapObject;
import com.mygdx.game.Screens.PlayScreen;

public class UnderGroundPipes extends InteractiveTileObject {
    @Override
    public void onHeadHit(Mario mario) {

    }

    public UnderGroundPipes(PlayScreen screen, MapObject  mapObject) {
        super(screen, mapObject);
        fixture.setUserData(this);
    }
}
