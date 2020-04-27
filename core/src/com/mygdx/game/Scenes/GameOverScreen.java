package com.mygdx.game.Scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MarioGame;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.Mario;

public class GameOverScreen implements Screen {

    private Viewport viewport;
    private Stage stage;
    private Game game;


    public GameOverScreen(Game game) {
        this.game = game;
        viewport = new FitViewport(MarioGame.V_width, MarioGame.V_height, new OrthographicCamera());
        stage = new Stage(viewport,((MarioGame)game).batch);
        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        Table table = new Table();
        table.center();
        table.setFillParent(true);
        Label gameOver = new Label("Game Over",font);
        gameOver.setFontScale(5);
        Label playAgain = new Label("PlayAgain",font);
        playAgain.setFontScale(5);
        table.add(gameOver).expandX();
        table.row();
        table.add(playAgain).expandX().padTop(10);
        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(Gdx.input.isTouched()){
            game.setScreen(new PlayScreen((MarioGame) game));
            dispose();
        }
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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
        stage.dispose();
    }
}
