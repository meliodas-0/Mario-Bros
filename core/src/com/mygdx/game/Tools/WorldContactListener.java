package com.mygdx.game.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.Flag.Flag;
import com.mygdx.game.Items.Item;
import com.mygdx.game.MarioGame;
import com.mygdx.game.Enemy.Enemy;
import com.mygdx.game.Sprites.InteractiveTileObject;
import com.mygdx.game.Sprites.Mario;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {

        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        int cDef = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;
        if(fixtureA == null || fixtureB == null)return;


        switch (cDef){
            case MarioGame.MARIO_HEAD_BIT | MarioGame.COIN_BIT:
            case MarioGame.MARIO_HEAD_BIT | MarioGame.BRICK_BIT:
            case MarioGame.MARIO_HEAD_BIT | MarioGame.POWERUP_BIT:
                if(fixtureA.getFilterData().categoryBits == MarioGame.MARIO_HEAD_BIT)
                    ((InteractiveTileObject)fixtureB.getUserData()).onHeadHit((Mario)fixtureA.getUserData());
                else
                    ((InteractiveTileObject)fixtureA.getUserData()).onHeadHit((Mario)fixtureB.getUserData());
                break;
            case MarioGame.ENEMY_HEAD_BIT | MarioGame.MARIO_BIT:
                if(fixtureA.getFilterData().categoryBits==MarioGame.ENEMY_HEAD_BIT)
                    ((Enemy)fixtureA.getUserData()).hitOnHead((Mario)fixtureB.getUserData());
                else
                    ((Enemy)fixtureB.getUserData()).hitOnHead((Mario)fixtureA.getUserData());
                break;
            case MarioGame.ENEMY_BIT | MarioGame.OBJECT_BIT:
                if(fixtureA.getFilterData().categoryBits==MarioGame.ENEMY_BIT)
                    ((Enemy)fixtureA.getUserData()).reverseVelocity(true,false);
                else
                    ((Enemy)fixtureB.getUserData()).reverseVelocity(true,false);
                break;
            case MarioGame.MARIO_BIT | MarioGame.ENEMY_BIT:
                if(fixtureA.getFilterData().categoryBits == MarioGame.MARIO_BIT)
                    ((Mario)fixtureA.getUserData()).hit((Enemy)fixtureB.getUserData());
                else
                    ((Mario)fixtureB.getUserData()).hit((Enemy)fixtureA.getUserData());
                break;
            case MarioGame.ENEMY_BIT:
                ((Enemy)fixtureA.getUserData()).hitByEnemy((Enemy)fixtureB.getUserData());
                ((Enemy)fixtureB.getUserData()).hitByEnemy((Enemy)fixtureA.getUserData());
                break;
            case MarioGame.ITEM_BIT | MarioGame.OBJECT_BIT:
                if(fixtureA.getFilterData().categoryBits==MarioGame.ITEM_BIT)
                    ((Item)fixtureA.getUserData()).reverseVelocity(true,false);
                else
                    ((Item)fixtureB.getUserData()).reverseVelocity(true,false);
                break;
            case MarioGame.ITEM_BIT | MarioGame.MARIO_BIT:
                if(fixtureA.getFilterData().categoryBits==MarioGame.ITEM_BIT)
                    ((Item)fixtureA.getUserData()).use((Mario) fixtureB.getUserData());
                else
                    ((Item)fixtureB.getUserData()).use((Mario) fixtureA.getUserData());
                break;
            case MarioGame.MARIO_BIT | MarioGame.FLAG_BIT:
                if(fixtureA.getFilterData().categoryBits==MarioGame.MARIO_BIT)
                ((Mario)fixtureA.getUserData()).complete((Flag)fixtureB.getUserData());
                else
                ((Mario)fixtureB.getUserData()).complete((Flag)fixtureA.getUserData());
                break;
            default:
                break;
        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
