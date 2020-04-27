package com.mygdx.game.Sprites;

import com.badlogic.gdx.maps.MapObject;
import com.mygdx.game.Screens.PlayScreen;

public class Lives extends InteractiveTileObject {
    @Override
    public void onHeadHit(Mario mario) {

    }

    public Lives(PlayScreen screen, MapObject bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
    }
}
