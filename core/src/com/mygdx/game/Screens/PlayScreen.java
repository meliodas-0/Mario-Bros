package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MarioGame;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Sprites.Mario;

public class PlayScreen implements Screen, InputProcessor {

    private MarioGame game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;

    private World world;
    private Box2DDebugRenderer b2dr;

    private Mario mario;
    Sprite sprite;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    BitmapFont bf;

    public PlayScreen(MarioGame marioGame){

        sprite = new Sprite(new Texture("badlogic.jpg"));
        bf = new BitmapFont();
        bf.setColor(Color.WHITE);
        sprite.setPosition(Gdx.graphics.getWidth()/2 - sprite.getWidth()/2, Gdx.graphics.getHeight()/2 - sprite.getHeight()/2);
        Gdx.input.setInputProcessor(this);
        this.game = marioGame;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(MarioGame.V_width/MarioGame.PPM, MarioGame.V_height/MarioGame.PPM, gameCam);
        hud = new Hud(game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);

        gameCam.position.set(400, 110, 0);
        gameCam.zoom = 0.2f;

        world = new World(new Vector2(0, -13), true);
        b2dr = new Box2DDebugRenderer();

        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;


        //creating static body for hidden coins
        for(MapObject mapObject: map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)mapObject).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(rect.getX() + rect.getWidth()/2, rect.getY() + rect.getHeight()/2);
            body = world.createBody(bodyDef);

            shape.setAsBox(rect.getWidth()/2, rect.getHeight()/2);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
        }

        //creating static body for ground
        for(MapObject mapObject: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)mapObject).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(rect.getX() + rect.getWidth()/2, rect.getY() + rect.getHeight()/2);
            body = world.createBody(bodyDef);

            shape.setAsBox(rect.getWidth()/2, rect.getHeight()/2);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
        }

        //creating static body for flag
        for(MapObject mapObject: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)mapObject).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(rect.getX() + rect.getWidth()/2, rect.getY() + rect.getHeight()/2);
            body = world.createBody(bodyDef);

            shape.setAsBox(rect.getWidth()/2, rect.getHeight()/2);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
        }

        //creating static body for bricks
        for(MapObject mapObject: map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)mapObject).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(rect.getX() + rect.getWidth()/2, rect.getY() + rect.getHeight()/2);
            body = world.createBody(bodyDef);

            shape.setAsBox(rect.getWidth()/2, rect.getHeight()/2);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
        }

        //creating static body for pipes
        for(MapObject mapObject: map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)mapObject).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(rect.getX() + rect.getWidth()/2, rect.getY() + rect.getHeight()/2);
            body = world.createBody(bodyDef);

            shape.setAsBox(rect.getWidth()/2, rect.getHeight()/2);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
        }
        //creating static body for undergroundPipes
        for(MapObject mapObject: map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)mapObject).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(rect.getX() + rect.getWidth()/2, rect.getY() + rect.getHeight()/2);
            body = world.createBody(bodyDef);

            shape.setAsBox(rect.getWidth()/2, rect.getHeight()/2);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
        }

        //creating static body for lives
        for(MapObject mapObject: map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)mapObject).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(rect.getX() + rect.getWidth()/2, rect.getY() + rect.getHeight()/2);
            body = world.createBody(bodyDef);

            shape.setAsBox(rect.getWidth()/2, rect.getHeight()/2);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
        }

        //creating static body for power ups
        for(MapObject mapObject: map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)mapObject).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(rect.getX() + rect.getWidth()/2, rect.getY() + rect.getHeight()/2);
            body = world.createBody(bodyDef);

            shape.setAsBox(rect.getWidth()/2, rect.getHeight()/2);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
        }

        //creating static body for hiddenStar
        for(MapObject mapObject: map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)mapObject).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(rect.getX() + rect.getWidth()/2, rect.getY() + rect.getHeight()/2);
            body = world.createBody(bodyDef);

            shape.setAsBox(rect.getWidth()/2, rect.getHeight()/2);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
        }

        //creating static body for coins
        for(MapObject mapObject: map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)mapObject).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(rect.getX() + rect.getWidth()/2, rect.getY() + rect.getHeight()/2);
            body = world.createBody(bodyDef);

            shape.setAsBox(rect.getWidth()/2, rect.getHeight()/2);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
        }

        mario = new Mario(world);

        mario.body.setLinearDamping(0.3f);

    }

    @Override
    public void show() {

    }

    public void handleInputs(final float delta){

//        if(Gdx.input.justTouched() && Gdx.input.getDeltaY() < 0){
//            mario.body.applyLinearImpulse(new Vector2(0, 10000), mario.body.getWorldCenter(), true);
//        }
//        if(Gdx.input.justTouched() && Gdx.input.getDeltaX() < 0 && mario.body.getLinearVelocity().x >= -2){
//            mario.body.applyLinearImpulse(new Vector2(-1000, 0), mario.body.getWorldCenter(), true);
//        }
//        if(Gdx.input.justTouched() && Gdx.input.getDeltaX() > 0 && mario.body.getLinearVelocity().x <= 2){
//            mario.body.applyLinearImpulse(new Vector2(1000, 100f), mario.body.getWorldCenter(), true);
//        }

    }

    public void update(float delta){
        handleInputs(delta);
        gameCam.position.x = mario.body.getPosition().x;
        gameCam.update();
        world.step(1/30f, 10, 10);
        renderer.setView(gameCam);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
        update(delta);
        b2dr.render(world, gameCam.combined);
        game.batch.begin();
        game.batch.draw(new Texture("up.png"), 190, Gdx.graphics.getHeight()-800, 250, 200);
        game.batch.draw(new Texture("right.png"), 440, 100 , 200, 250);
        game.batch.draw(new Texture("left.png"), 25, 80 , 200, 250);
        bf.draw(game.batch, sprite.getX() +"   "+ mario.body.getLinearDamping() +" " + sprite.getY(), Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        game.batch.end();
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gameCam.viewportHeight = height;
        gameCam.viewportWidth = width;
        gameCam.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {


        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        int y = Gdx.graphics.getHeight() - screenY;
        sprite.setPosition(screenX, y);
        if(screenX>= 190 && screenX<= 440 && y >= 250 && y <= 450 ){
            mario.body.applyLinearImpulse(new Vector2(0, 60f), mario.body.getWorldCenter(),true);
        }
        if(screenX>= 440 && screenX<= 640 && y >= 100 && y <= 350 ){
            mario.body.applyForceToCenter(new Vector2(30, 0), true);
        }
        if(screenX>= 25 && screenX<= 225 && y >= 80 && y <= 330 ){
            mario.body.applyForceToCenter(new Vector2(-30, 0), true);
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
