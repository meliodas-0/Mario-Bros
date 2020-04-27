package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Items.Item;
import com.mygdx.game.Items.ItemDef;
import com.mygdx.game.Items.Mushroom;
import com.mygdx.game.MarioGame;
import com.mygdx.game.Scenes.GameOverScreen;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Enemy.Enemy;
import com.mygdx.game.Sprites.Mario;
import com.mygdx.game.Tools.B2WorldCreator;
import com.mygdx.game.Tools.WorldContactListener;

import java.util.concurrent.LinkedBlockingQueue;

public class PlayScreen implements Screen, InputProcessor {

    private MarioGame game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;

    private TextureAtlas atlas;

    private World world;
    private Box2DDebugRenderer b2dr;

    private Mario mario;
    private Music music;
    private B2WorldCreator creator;

    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;
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
        creator = new B2WorldCreator(this);
        mario = new Mario(this);
        mario.body.setGravityScale(2.4f);
        world.setContactListener(new WorldContactListener());
        music = MarioGame.manager.get("Audio/Music/mario_music.ogg",Music.class);
        music.setLooping(true);
        music.play();
        items = new Array<>();
        itemsToSpawn = new LinkedBlockingQueue<>();
    }
    public void spawnItem(ItemDef itemDef){
        itemsToSpawn.add(itemDef);
    }
    public void handleSpawningItem(){
        if(!itemsToSpawn.isEmpty()){
            ItemDef itemDef = itemsToSpawn.poll();
            if(itemDef.type == Mushroom.class){
                items.add(new Mushroom(this,itemDef.position.x,itemDef.position.y));
            }
        }
    }

    public boolean gameOver(){
        if(mario.currentState == Mario.State.DEAD && mario.getStateTimer()>3)
            return true;
        return false;
    }
    public boolean gameCompleted(){
        if(mario.currentState == Mario.State.COMPLETED){
            return true;
        }
        return false;
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }
    @Override
    public void show() {
    }
    public TiledMap getMap(){
        return map;
    }
    public World getWorld(){
        return world;
    }

    public void handleInputs(float delta){

    }

    public void update(float delta){
        handleInputs(delta);
        handleSpawningItem();
        mario.update(delta, KEY_PRESSED);
        for(Enemy enemy:creator.getEnemy()) {
            enemy.update(delta);
            if(enemy.getX()<mario.getX()+224)
                enemy.body.setActive(true);
        }
        for (Item item : items)
            item.update(delta);
        hud.update(delta);
        world.step(1/30f, 6, 2);
        if(mario.currentState!=Mario.State.DEAD && mario.currentState != Mario.State.COMPLETED){
            gameCam.position.x = mario.body.getPosition().x;
        }
        gameCam.update();
        renderer.setView(gameCam);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(83/255f, 168/255f, 255/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
        update(delta);
        b2dr.render(world, gameCam.combined);
        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        game.batch.draw(up, gameCam.position.x + 150,gameCam.position.y - 70 , 26, 23);
        game.batch.draw(right, gameCam.position.x - 140, gameCam.position.y - 70 , 23, 26);
        game.batch.draw(left, gameCam.position.x - 180, gameCam.position.y - 70 , 23, 26);
        mario.draw(game.batch);
        for(Enemy enemy:creator.getEnemy())
            enemy.draw(game.batch);
        for (Item item : items)
            item.draw(game.batch);
        game.batch.end();
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        if(gameOver()){
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
        if(gameCompleted()){
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
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
        if(mario.currentState != Mario.State.DEAD && mario.currentState != Mario.State.COMPLETING && mario.currentState != Mario.State.COMPLETED){
        if(screenX>= 1800 && screenX<= 2050 && y >= 50 && y <= 350 ){
            if(mario.body.getPosition().y<140){
            Vector2 vector2 = new Vector2(0, 3f);
            mario.body.applyLinearImpulse(vector2,new Vector2(0,0),true);
                KEY_PRESSED = KEY_UP;}
        }
        if(screenX>= 440 && screenX<= 640 && y >= 180 && y <= 330 ){
            mario.body.applyForceToCenter(new Vector2(20, 0), true);
            KEY_PRESSED = KEY_RIGHT;
        }
        if(screenX>= 25 && screenX<= 225 && y >= 180 && y <= 330 ){
            mario.body.applyForceToCenter(new Vector2(-20, 0), true);
            KEY_PRESSED = KEY_LEFT;
        }}
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
