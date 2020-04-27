package com.mygdx.game.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Enemy.Enemy;
import com.mygdx.game.Enemy.Turtle;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.Bricks;
import com.mygdx.game.Sprites.Coin;
import com.mygdx.game.Flag.Flag;
import com.mygdx.game.Enemy.Goomba;
import com.mygdx.game.Sprites.Ground;
import com.mygdx.game.Sprites.HiddenCoin;
import com.mygdx.game.Sprites.HiddenStar;
import com.mygdx.game.Sprites.Lives;
import com.mygdx.game.Sprites.Pipe;
import com.mygdx.game.Sprites.PowerUp;
import com.mygdx.game.Sprites.UnderGroundPipes;

public class B2WorldCreator {
    private Array<Goomba> goombas;
    private Array<Turtle> turtles;

    public Array<Enemy> getEnemy() {
        Array<Enemy> enemies = new Array<>();
        enemies.addAll(goombas);
        enemies.addAll(turtles);
        return enemies;
    }



    public B2WorldCreator(PlayScreen screen) {

        World world = screen.getWorld();
        TiledMap map = screen.getMap();


        //creating static body for hidden coins
        for(MapObject mapObject: map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){

            new HiddenCoin(screen, mapObject);
        }

        //creating static body for ground
        for(MapObject mapObject: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){

            new Ground(screen, mapObject);
        }

        //creating static body for flag
        for(MapObject mapObject: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){

            new Flag(screen, mapObject);
        }

        //creating static body for bricks
        for(MapObject mapObject: map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            new Bricks(screen, mapObject);

        }

        //creating static body for pipes
        for(MapObject mapObject: map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){

            new Pipe(screen, mapObject);
        }
        //creating static body for undergroundPipes
        for(MapObject mapObject: map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){

            new UnderGroundPipes(screen, mapObject);
        }

        //creating static body for lives
        for(MapObject mapObject: map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){

            new Lives(screen, mapObject);
        }

        //creating static body for power ups
        for(MapObject mapObject: map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)){

            new PowerUp(screen, mapObject);
        }

        //creating static body for hiddenStar
        for(MapObject mapObject: map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)){

            new HiddenStar(screen, mapObject);
        }

        //creating static body for coins
        for(MapObject mapObject: map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)){

            new Coin(screen, mapObject);
        }

        goombas = new Array<>();
        for(MapObject mapObject: map.getLayers().get(12).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)mapObject).getRectangle();
            goombas.add(new Goomba(screen,rect.getX()/100,rect.getY()/100));
        }
        turtles = new Array<>();
        for(MapObject mapObject: map.getLayers().get(13).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)mapObject).getRectangle();
            turtles.add(new Turtle(screen,rect.getX()/100,rect.getY()/100));
        }
    }
    public void removeTurtle(Turtle turtle){
        turtles.removeValue(turtle,true);
    }
}
