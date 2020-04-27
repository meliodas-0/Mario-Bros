package com.mygdx.game.Enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MarioGame;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.Mario;
import com.mygdx.game.Tools.B2WorldCreator;

import sun.security.krb5.internal.crypto.Des;

public class Turtle extends Enemy{

    private final TextureRegion shell;
    public enum State {WALKING,MOVING_SHELL,STANDING_SHELL,DEAD}
    public static final int KICK_LEFT = -20;
    public static final int KICK_RIGHT = 20;
    public State currentState,previousState;
    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    private float deadRotationDegree;
    private boolean Destroyed;

    public Turtle(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 0, 0, 16, 24));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"), 16, 0, 16, 24));
        shell = new TextureRegion(screen.getAtlas().findRegion("turtle"), 64, 0, 16, 24);
        walkAnimation = new Animation(0.2f, frames);
        currentState = previousState = State.WALKING;
        deadRotationDegree = 0;
        setBounds(getX()*100, getY()*100, 16 , 24);

    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX()*100, getY()*100);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 );
        fdef.filter.categoryBits = MarioGame.ENEMY_BIT;
        fdef.filter.maskBits = MarioGame.GROUND_BIT |
                MarioGame.COIN_BIT |
                MarioGame.BRICK_BIT |
                MarioGame.ENEMY_BIT |
                MarioGame.OBJECT_BIT |
                MarioGame.MARIO_BIT ;

        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);

        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5, 8).scl(1);
        vertice[1] = new Vector2(5, 8).scl(1);
        vertice[2] = new Vector2(-3, 3).scl(1);
        vertice[3] = new Vector2(3, 3).scl(1);
        head.set(vertice);

        fdef.shape = head;
        fdef.restitution = 1.8f;
        fdef.filter.categoryBits = MarioGame.ENEMY_HEAD_BIT;
        body.createFixture(fdef).setUserData(this);
    }

    public TextureRegion getFrame(float dt){
        TextureRegion region;

        switch (currentState){
            case MOVING_SHELL:
            case STANDING_SHELL:
                region = shell;
                break;
            case WALKING:
            default:
                region = (TextureRegion) walkAnimation.getKeyFrame(stateTime, true);
                break;
        }

        if(velocity.x > 0 && !region.isFlipX()){
            region.flip(true, false);
        }
        if(velocity.x < 0 && region.isFlipX()){
            region.flip(true, false);
        }
        stateTime = currentState == previousState ? stateTime + dt : 0;
        previousState = currentState;
        return region;
    }

    @Override
    public void update(float dt) {
        if(body.getPosition().y<0){
            currentState = State.DEAD;
        }
        if(!Destroyed){
            setRegion(getFrame(dt));
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - 8);
            if(getCurrentState() == State.STANDING_SHELL && stateTime > 5){
                currentState = State.WALKING;
                velocity.x = 5;
            }
            else if(currentState == State.DEAD){
                deadRotationDegree +=3;
                rotate(deadRotationDegree);
                if(stateTime>3 &&!Destroyed){
                    world.destroyBody(body);
                    Destroyed= true;
//                    b2WorldCreator.removeTurtle(this);
                }
            }
            else{
                body.setLinearVelocity(velocity);
            }
        }

    }

    @Override
    public void hitOnHead(Mario mario) {
        if(getCurrentState() == State.STANDING_SHELL) {
            if(mario.body.getPosition().x > body.getPosition().x)
                velocity.x = -30;
            else
                velocity.x = 30;
            currentState = State.MOVING_SHELL;
            System.out.println("Set to moving shell");
        }
        else {
            currentState = State.STANDING_SHELL;
            velocity.x = 0;
        }
    }
    public void draw(Batch batch){
        if(!Destroyed)
            super.draw(batch);
    }

    @Override
    public void hitByEnemy(Enemy enemy) {
        if(enemy instanceof Turtle){
            if(((Turtle)enemy).currentState == State.MOVING_SHELL&& currentState!=State.MOVING_SHELL){
                killed();
            }
            else if(currentState == State.MOVING_SHELL && ((Turtle)enemy).currentState == State.WALKING)
                return;
            else
                reverseVelocity(true,false);
        }
        else if(currentState!=State.MOVING_SHELL)
            reverseVelocity(true,false);
    }

    public State getCurrentState() {
        return currentState;
    }

    public void kick(int direction){
        velocity.x = direction;
        currentState = State.MOVING_SHELL;
    }

    public void killed(){
        currentState = State.DEAD;

        Filter filter = new Filter();
        filter.maskBits = MarioGame.NOTHING_BIT;

        for(Fixture fixture : body.getFixtureList()){
            fixture.setFilterData(filter);
        }
        body.applyLinearImpulse(new Vector2(10,0),body.getWorldCenter(),true);

    }
}
