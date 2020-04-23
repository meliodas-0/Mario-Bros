package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MarioGame;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Sprites.Mario;
import com.mygdx.game.Tools.B2WorldCreator;
import com.mygdx.game.Tools.WorldContactListener;

public class PlayScreen implements Screen, InputProcessor {

    private MarioGame game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;

    private TextureAtlas atlas;

    private World world;
    private Box2DDebugRenderer b2dr;

    private Mario mario;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private Texture left, right, up;

    BitmapFont bf;

    int KEY_PRESSED;
    public final static int KEY_UP = 0 , KEY_LEFT = 1, KEY_RIGHT = 2;

    public PlayScreen(MarioGame marioGame){

        up = new Texture("up.png");
        left = new Texture("left.png");
        right = new Texture("right.png");

        atlas = new TextureAtlas("Mario_and_Enemies.pack.txt");

        bf = new BitmapFont();
        bf.setColor(Color.WHITE);
        Gdx.input.setInputProcessor(this);
        this.game = marioGame;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(MarioGame.V_width/MarioGame.PPM, MarioGame.V_height/MarioGame.PPM, gameCam);
        hud = new Hud(game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);

        gameCam.position.set(216, 105, 0);
        gameCam.zoom = 0.19f;

        world = new World(new Vector2(0, -16), true);
        b2dr = new Box2DDebugRenderer();


        new B2WorldCreator(world, map);

        mario = new Mario(world, this);
        mario.body.setGravityScale(2.4f);

        world.setContactListener(new WorldContactListener());

    }

    public TextureAtlas getAtlas() {
        return atlas;
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
        mario.update(delta, KEY_PRESSED);
        hud.update(delta);
        world.step(1/30f, 6, 2);
        renderer.setView(gameCam);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
        update(delta);
        b2dr.render(world, gameCam.combined);
        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        game.batch.draw(up, gameCam.position.x - 170,gameCam.position.y - 40 , 50, 40);
        game.batch.draw(right, gameCam.position.x - 130, gameCam.position.y - 75 , 40, 50);
        game.batch.draw(left, gameCam.position.x - 200, gameCam.position.y - 80 , 40, 50);
        mario.draw(game.batch);
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
        left.dispose();
        up.dispose();
        right.dispose();
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
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
        if(screenX>= 190 && screenX<= 440 && y >= 250 && y <= 450 ){
            mario.body.applyLinearImpulse(new Vector2(0, 10f), mario.body.getWorldCenter(),true);
            KEY_PRESSED = KEY_UP;
        }
        if(screenX>= 440 && screenX<= 640 && y >= 100 && y <= 350 ){
            mario.body.applyForceToCenter(new Vector2(30, 0), true);
            KEY_PRESSED = KEY_RIGHT;
        }
        if(screenX>= 25 && screenX<= 225 && y >= 80 && y <= 330 ){
            mario.body.applyForceToCenter(new Vector2(-30, 0), true);
            KEY_PRESSED = KEY_LEFT;
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
