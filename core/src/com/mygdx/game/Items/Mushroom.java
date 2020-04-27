package com.mygdx.game.Items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.mygdx.game.MarioGame;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.Mario;

import java.awt.geom.RectangularShape;

public class Mushroom extends Item {
    public Mushroom(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        setRegion(screen.getAtlas().findRegion("mushroom"),0,0,16,16);
        velocity = new Vector2(10,0);
    }

    @Override
    public void defineItem() {
        BodyDef bodyDef = new BodyDef();
        System.out.println("mushroom created");
        bodyDef.position.set(getX(),getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        FixtureDef fdef = new FixtureDef();

        EdgeShape shape1 = new EdgeShape();
        shape1.set(-8,-8,8,8);
        EdgeShape shape2 = new EdgeShape();
        shape2.set(8,-8,-8,8);
        fdef.shape = shape1;
        body.createFixture(fdef).setUserData(this);
        fdef.shape = shape2;
        body.createFixture(fdef).setUserData(this);
        fdef.filter.categoryBits = MarioGame.ITEM_BIT;
        fdef.filter.maskBits =MarioGame.GROUND_BIT |
                MarioGame.COIN_BIT |
                MarioGame.POWERUP_BIT |
                MarioGame.BRICK_BIT |
                MarioGame.ENEMY_BIT |
                MarioGame.OBJECT_BIT |
                MarioGame.MARIO_BIT ;
        body.createFixture(fdef).setUserData(this);

    }

    @Override
    public void use(Mario mario) {
        destroy();
        mario.grow();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(body.getPosition().x-getWidth()/2,body.getPosition().y-getHeight()/2);
        velocity.y=body.getLinearVelocity().y;
        body.setLinearVelocity(velocity);
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }
}
