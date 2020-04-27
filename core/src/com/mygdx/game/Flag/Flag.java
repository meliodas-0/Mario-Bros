package com.mygdx.game.Flag;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.game.MarioGame;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.InteractiveTileObject;
import com.mygdx.game.Sprites.Mario;

public class Flag extends InteractiveTileObject {

    public static int flag_top = 281;
    @Override
    public void onHeadHit(Mario mario) {

    }

    public Flag(PlayScreen screen,  MapObject mapObject) {
        super(screen, mapObject);
        fixture.setUserData(this);

        setCategoryFilter(MarioGame.FLAG_BIT);
    }


}
