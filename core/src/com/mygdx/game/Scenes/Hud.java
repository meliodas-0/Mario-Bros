package com.mygdx.game.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MarioGame;

public class Hud implements Disposable {

    public Stage stage;
    private Viewport viewport;
    private Integer worldTimer;
    private float timeCount;
    private static Integer score;
    private Integer coins;

    Label countDownLabel;
    static Label scoreLabel;
    Label timeLabel;
    Label levelLabel;
    Label worldLabel;
    Label marioLabel;
    Label coinsLabel;

    public Hud(SpriteBatch sb){
        worldTimer = 300;
        timeCount = 0;
        score = 0;
        coins = 0;

        viewport = new FitViewport(MarioGame.V_width, MarioGame.V_height, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        countDownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("Time", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel = new Label("World", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        marioLabel = new Label("Mario", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        coinsLabel = new Label("Coins X " + String.format("%02d", coins), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        marioLabel.setFontScale(3);
        countDownLabel.setFontScale(3);
        scoreLabel.setFontScale(3);
        timeLabel.setFontScale(3);
        levelLabel.setFontScale(3);
        worldLabel.setFontScale(3);
        coinsLabel.setFontScale(3);

        table.add(marioLabel).expandX().padTop(10);
        table.add(new Actor()).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX();
        table.add(coinsLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countDownLabel).expandX();

        stage.addActor(table);

    }

    public void update(float dt){
        timeCount += dt;
        if(timeCount>=1){
            worldTimer--;
            countDownLabel.setText(String.format("%03d", worldTimer));
            timeCount =0;
        }
    }
    public static void addScore(int value){
        score +=value;
        scoreLabel.setText(String.format("%06d",score));
    }
    @Override
    public void dispose() {
        stage.dispose();
    }
}
