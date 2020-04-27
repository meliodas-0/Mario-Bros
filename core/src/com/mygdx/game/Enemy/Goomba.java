package com.mygdx.game.Enemy;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MarioGame;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.Mario;

public class Goomba extends Enemy {
    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean Destroyed;

    public Goomba(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for (int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i * 16, 0, 16, 16));
        }
        walkAnimation = new Animation(0.4f, frames);
        stateTime = 0;
        setBounds(getX()*100,getY()*100,16,16);
        setToDestroy = false;

    }

    public void update(float dt){
        stateTime += dt;
        if(setToDestroy&&!Destroyed){
            world.destroyBody(body);
            Destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"),32,0,16,16));
            stateTime = 0;
        }
        else if(!Destroyed){
            body.setLinearVelocity(velocity);
            setPosition(body.getPosition().x-getWidth()/2,body.getPosition().y-getHeight()/2);
            setRegion((TextureRegion) walkAnimation.getKeyFrame(stateTime,true));
        }

    }

    @Override
    protected void defineEnemy() {
        BodyDef bodyDef = new BodyDef();
        System.out.println("body created");
        bodyDef.position.set(getX()*100,getY()*100);
        System.out.println(getX()+ "," +getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodyDef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6);
        fdef.filter.categoryBits = MarioGame.ENEMY_BIT;
        fdef.filter.maskBits = MarioGame.GROUND_BIT |
                MarioGame.COIN_BIT |
                MarioGame.BRICK_BIT |
                MarioGame.ENEMY_BIT |
                MarioGame.OBJECT_BIT |
                MarioGame.MARIO_BIT ;
        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);

        //head
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5,8).scl(1);
        vertice[1] = new Vector2(5,8).scl(1);
        vertice[2] = new Vector2(-3,3).scl(1);
        vertice[3] = new Vector2(3,3).scl(1);
        head.set(vertice);
        fdef.shape = head;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits =MarioGame.ENEMY_HEAD_BIT;
        body.createFixture(fdef).setUserData(this);

    }

    public void draw(Batch batch){
        if(!Destroyed || stateTime<0.5)
            super.draw(batch);
    }

    @Override
    public void hitOnHead(Mario mario) {
        setToDestroy = true;
        MarioGame.manager.get("Audio/Sounds/stomp.wav", Sound.class).play();
    }

    @Override
    public void hitByEnemy(Enemy enemy) {
        if(enemy instanceof Turtle &&((Turtle)enemy).currentState == Turtle.State.MOVING_SHELL)
            setToDestroy = true;
        else
            reverseVelocity(true, false);
    }
}
