package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MarioGame;
import com.mygdx.game.Screens.PlayScreen;

public class Mario extends Sprite {

    public enum State {FALLING, JUMPING, STANDING, RUNNING, STOPPING};
    public State currentState, previousState;
    public World world;
    public Body body;
    private Animation marioRun;
    private boolean runningRight;
    private float stateTimer;
    private TextureRegion marioStand, marioStop, marioJump;

    public Mario(World world, PlayScreen playScreen) {
        super(playScreen.getAtlas().findRegion("little_mario"));
        this.world = world;
        defineMario();

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<>();

        for(int i = 1; i<4; i++){
            frames.add(new TextureRegion(getTexture(), i*16, 10, 16, 16));
        }

        marioRun = new Animation(0.1f, frames);
        frames.clear();

        marioJump = new TextureRegion(getTexture(), 80, 10, 16, 16);
        marioStand = new TextureRegion(getTexture(), 0 ,10, 16, 16);
        setBounds(0, 0, 16, 16);
        marioStop = new TextureRegion(getTexture(), 64, 10, 16, 16);
        setRegion(marioStand);

    }

    public void update(float delta, int keyPressed){
        //marioRun.setFrameDuration(body.getLinearVelocity().x);
        setPosition(body.getPosition().x - getWidth()/2 , body.getPosition().y - getHeight()/2 );
        setRegion(getFrame(delta, keyPressed));
    }

    public TextureRegion getFrame(float delta, int keyPressed){
        currentState = getState(keyPressed);

        TextureRegion region;

        switch (currentState){
            case JUMPING:
                region = marioJump;
                break;
            case RUNNING:
                region = (TextureRegion) marioRun.getKeyFrame(stateTimer, true);
                break;
            case STOPPING:
                region = marioStop;
                break;
            case FALLING:
            case STANDING:
            default:
                region = marioStand;
                break;
        }

        if((body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }else if((body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }

        if(State.STOPPING == currentState){
            region.flip(true, false);
        }

        stateTimer = currentState == previousState ? stateTimer + delta : 0 ;
        previousState = currentState;
        return region;

    }

    public State getState(int keyPressed){

        if(body.getLinearVelocity().y > 0.000001f || (body.getLinearVelocity().y<0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if(body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if((body.getLinearVelocity().x > 0 && keyPressed == PlayScreen.KEY_LEFT )|| (body.getLinearVelocity().x < 0 && keyPressed == PlayScreen.KEY_RIGHT)){
            return State.STOPPING;
        }
        else if(body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;
    }

    private void defineMario() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(160, 80);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodyDef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6);
        fdef.filter.categoryBits = MarioGame.MARIO_BIT;
        fdef.filter.maskBits = MarioGame.DEFAULT_BIT | MarioGame.COIN_BIT | MarioGame.BRICK_BIT;
        fdef.shape = shape;
        body.createFixture(fdef);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2, 6), new Vector2(2, 6));
        fdef.shape = head;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("head");
    }
}
