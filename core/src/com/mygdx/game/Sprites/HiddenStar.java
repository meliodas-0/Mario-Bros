package com.mygdx.game.Sprites;

import com.badlogic.gdx.maps.MapObject;
import com.mygdx.game.Screens.PlayScreen;

public class HiddenStar extends InteractiveTileObject {
    @Override
    public void onHeadHit(Mario mario) {

    }

    public HiddenStar(PlayScreen screen, MapObject  mapObject) {
        super(screen, mapObject);
        fixture.setUserData(this);
    }
}
