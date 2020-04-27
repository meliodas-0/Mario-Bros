package com.mygdx.game.Sprites;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Enemy.Enemy;
import com.mygdx.game.Enemy.Turtle;
import com.mygdx.game.Flag.Flag;
import com.mygdx.game.MarioGame;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Screens.PlayScreen;

public class Mario extends Sprite {

    public enum State {FALLING, JUMPING, STANDING, RUNNING, STOPPING, GROWING,DEAD,COMPLETED,COMPLETING};
    public State currentState, previousState;
    public World world;
    public Body body;
    private Animation marioRun,bigMarioRun,growMario;
    private PlayScreen screen;
    private boolean runningRight,marioIsBig =false,runGrowthAnimation=false,timeTodefineMario=false,timeToRedefineMario,marioIsDead=false,stageIsCompleted=false;
    private float stateTimer;
    private TextureRegion marioStand, marioStop, marioJump,bigMarioStand,bigMarioJump,bigMarioStop,marioDead;

    public Mario( PlayScreen screen){
        this.screen = screen;
        this.world = screen.getWorld();

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<>();

        for(int i = 1; i<4; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"), i*16, 0, 16, 16));
        }

        marioRun = new Animation(0.1f, frames);
        frames.clear();
        for(int i = 1; i<4; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i*16, 0, 16, 32));
        }
        bigMarioRun = new Animation(0.1f, frames);
        frames.clear();

        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),240,0,16,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),0,0,16,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),240,0,16,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),0,0,16,32));

        growMario = new Animation(0.2f,frames);

        marioJump = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 80, 0, 16, 16);
        bigMarioJump = new TextureRegion(screen.getAtlas().findRegion("big_mario"),80,0,16,32);

        marioStand = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 0 ,0, 16, 16);
        bigMarioStand = new TextureRegion(screen.getAtlas().findRegion("big_mario"),0,0,16,32);

        marioDead = new TextureRegion(screen.getAtlas().findRegion("little_mario"),96,0,16,16);

        marioStop = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 64, 0, 16, 16);
        bigMarioStop = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 64, 0, 16, 32);

        defineMario();
        setBounds(0, 0, 16, 16);
        setRegion(marioStand);

    }

    public void update(float delta, int keyPressed){
        if(marioIsBig)
            setPosition(body.getPosition().x - getWidth()/2 , body.getPosition().y - getHeight()/2 );
        else{
            setPosition(body.getPosition().x - getWidth()/2 , body.getPosition().y - getHeight()/2 );}
        setRegion(getFrame(delta, keyPressed));
        if(timeTodefineMario)
            defineBigMario();
        if(timeToRedefineMario)
            redefineMario();

    }

    private void redefineMario() {
        Vector2 position = body.getPosition();
        world.destroyBody(body);
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(position);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodyDef);
        FixtureDef fdef = new FixtureDef();
        fdef.density = .002f;
        CircleShape shape = new CircleShape();
        shape.setRadius(7);
        fdef.filter.categoryBits = MarioGame.MARIO_BIT;
        fdef.filter.maskBits = MarioGame.GROUND_BIT |
                MarioGame.COIN_BIT |
                MarioGame.BRICK_BIT |
                MarioGame.ENEMY_BIT |
                MarioGame.OBJECT_BIT |
                MarioGame.ENEMY_HEAD_BIT |
                MarioGame.POWERUP_BIT |
                MarioGame.ITEM_BIT;
        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2, 7), new Vector2(2, 7));
        fdef.filter.categoryBits = MarioGame.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData(this);
        body.setFixedRotation(true);
        timeToRedefineMario = false;
    }

    private TextureRegion getFrame(float delta, int keyPressed){
        currentState = getState(keyPressed);
        TextureRegion region;
        switch (currentState){
            case DEAD:
                region = marioDead;
                break;
            case GROWING:
                region = (TextureRegion) growMario.getKeyFrame(stateTimer);
                if(growMario.isAnimationFinished(stateTimer)){
                    runGrowthAnimation = false;
                }
                break;
            case JUMPING:
                region = marioIsBig ? bigMarioJump: marioJump;
                break;
            case RUNNING:
                region = marioIsBig? (TextureRegion) bigMarioRun.getKeyFrame(stateTimer, true) : (TextureRegion) marioRun.getKeyFrame(stateTimer, true);
                break;
            case STOPPING:
                body.setLinearVelocity(0,0);
                region = marioIsBig? bigMarioStop : marioStop;
                break;
            case FALLING:
            case COMPLETING:
            case STANDING:
            default:
                region = marioIsBig? bigMarioStand : marioStand;
                break;
        }

        if((body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            body.setLinearVelocity(0,0);
            region.flip(true, false);
            runningRight = false;
        }else if((body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }

        if(State.STOPPING == currentState || State.COMPLETING == currentState){
            region.flip(true, false);
        }

        stateTimer = currentState == previousState ? stateTimer + delta : 0 ;
        previousState = currentState;
        return region;

    }


    private State getState(int keyPressed){
        if(stageIsCompleted)
            return State.COMPLETING;
        else if(marioIsDead)
            return State.DEAD;
        else if(runGrowthAnimation)
            return State.GROWING;
        else if(body.getLinearVelocity().y > 0.000001f || (body.getLinearVelocity().y<0 && previousState == State.JUMPING))
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

    public void complete(Flag flag) {
        stageIsCompleted = true;
        MarioGame.manager.get("Audio/Sounds/stageclear.wav", Sound.class).play();
        MarioGame.manager.get("Audio/Music/mario_music.ogg", Music.class).stop();

        body.applyForceToCenter(0,-10,true);
        Hud.addScore((int) (body.getPosition().y*10));

//        Filter filter = new Filter();
//        filter.categoryBits = MarioGame.MARIO_BIT;
//        filter.maskBits = MarioGame.GROUND_BIT;
//        for (Fixture fixture : body.getFixtureList()) {
//            fixture.setFilterData(filter);
//        }
        if(body.getLinearVelocity().y<=0)
            body.applyLinearImpulse(new Vector2(30, 0), body.getWorldCenter(), true);

    }
    public void hit(Enemy enemy){
        if(enemy instanceof Turtle &&((Turtle)enemy).getCurrentState()==Turtle.State.STANDING_SHELL){
            ((Turtle) enemy).kick(this.getX() <= enemy.getX() ?Turtle.KICK_RIGHT : Turtle.KICK_LEFT);
        }
        else {
            if (marioIsBig) {
                marioIsBig = false;
                timeToRedefineMario = true;
                setBounds(getX(), getY(), getWidth(), getHeight() / 2);
                MarioGame.manager.get("Audio/Sounds/powerdown.wav", Sound.class).play();
            } else {
                MarioGame.manager.get("Audio/Music/mario_music.ogg", Music.class).stop();
                MarioGame.manager.get("Audio/Sounds/mariodie.wav", Sound.class).play();
                marioIsDead = true;
                Filter filter = new Filter();
                filter.maskBits = MarioGame.NOTHING_BIT;
                for (Fixture fixture : body.getFixtureList()) {
                    fixture.setFilterData(filter);
                }
//                body.applyForceToCenter(new Vector2(0,40),true);
                body.setGravityScale(0);
                body.applyLinearImpulse(new Vector2(0, 4f), body.getWorldCenter(), true);

            }
        }
    }

    public void grow() {
        if (!marioIsBig) {
            runGrowthAnimation = true;
            marioIsBig = true;
            timeTodefineMario = true;
            setBounds(getX(), getY(), getWidth() * 1, getHeight() * 2);
            MarioGame.manager.get("Audio/Sounds/powerup.wav", Sound.class).play();
        }

    }

    public float getStateTimer() {
        return stateTimer;
    }


    private void defineBigMario() {
        Vector2 currentPosition = body.getPosition();
        world.destroyBody(body);
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(currentPosition.add(0,16)) ;
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodyDef);
        FixtureDef fdef = new FixtureDef();
        fdef.density = .002f;
        CircleShape shape = new CircleShape();
        shape.setRadius(7);
        fdef.filter.categoryBits = MarioGame.MARIO_BIT;
        fdef.filter.maskBits = MarioGame.GROUND_BIT |
                MarioGame.COIN_BIT |
                MarioGame.BRICK_BIT |
                MarioGame.ENEMY_BIT |
                MarioGame.OBJECT_BIT |
                MarioGame.ENEMY_HEAD_BIT |
                MarioGame.POWERUP_BIT |
                MarioGame.ITEM_BIT |
                MarioGame.FLAG_BIT;
        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
        shape.setPosition(new Vector2(0,-14));
        body.createFixture(fdef).setUserData(this);
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2, 7), new Vector2(2, 7));
        fdef.filter.categoryBits = MarioGame.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData(this);
        body.setFixedRotation(true);
        timeTodefineMario = false;
    }

    private void defineMario()  {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(208, 80);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        FixtureDef fdef = new FixtureDef();
        fdef.density = .002f;
        CircleShape shape = new CircleShape();
        shape.setRadius(7);
        fdef.shape = shape;
        body.setFixedRotation(true);
        fdef.filter.categoryBits = MarioGame.MARIO_BIT;
        fdef.filter.maskBits = MarioGame.GROUND_BIT |
                MarioGame.COIN_BIT |
                MarioGame.BRICK_BIT |
                MarioGame.ENEMY_BIT |
                MarioGame.OBJECT_BIT |
                MarioGame.ENEMY_HEAD_BIT |
                MarioGame.POWERUP_BIT |
                MarioGame.ITEM_BIT |
                MarioGame.FLAG_BIT;
        body.createFixture(fdef).setUserData(this);
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2, 7), new Vector2(2, 7));
        fdef.filter.categoryBits = MarioGame.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData(this);
    }
}
