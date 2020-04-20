package com.mygdx.game.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Sprites.Bricks;
import com.mygdx.game.Sprites.Coin;
import com.mygdx.game.Sprites.Flag;
import com.mygdx.game.Sprites.Ground;
import com.mygdx.game.Sprites.HiddenCoin;
import com.mygdx.game.Sprites.HiddenStar;
import com.mygdx.game.Sprites.Lives;
import com.mygdx.game.Sprites.Pipe;
import com.mygdx.game.Sprites.PowerUp;
import com.mygdx.game.Sprites.UnderGroundPipes;

public class B2WorldCreator {

    public B2WorldCreator(World world, TiledMap map) {

        //creating static body for hidden coins
        for(MapObject mapObject: map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)mapObject).getRectangle();

            new HiddenCoin(world, map, rect);
        }

        //creating static body for ground
        for(MapObject mapObject: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)mapObject).getRectangle();

            new Ground(world, map, rect);
        }

        //creating static body for flag
        for(MapObject mapObject: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)mapObject).getRectangle();

            new Flag(world, map, rect);
        }

        //creating static body for bricks
        for(MapObject mapObject: map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){

            Rectangle rect = ((RectangleMapObject)mapObject).getRectangle();
            new Bricks(world, map, rect);

        }

        //creating static body for pipes
        for(MapObject mapObject: map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)mapObject).getRectangle();

            new Pipe(world, map, rect);
        }
        //creating static body for undergroundPipes
        for(MapObject mapObject: map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)mapObject).getRectangle();

            new UnderGroundPipes(world, map, rect);
        }

        //creating static body for lives
        for(MapObject mapObject: map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)mapObject).getRectangle();

            new Lives(world, map, rect);
        }

        //creating static body for power ups
        for(MapObject mapObject: map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)mapObject).getRectangle();

            new PowerUp(world, map, rect);
        }

        //creating static body for hiddenStar
        for(MapObject mapObject: map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)mapObject).getRectangle();

            new HiddenStar(world, map, rect);
        }

        //creating static body for coins
        for(MapObject mapObject: map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)mapObject).getRectangle();

            new Coin(world, map, rect);
        }


    }
}
