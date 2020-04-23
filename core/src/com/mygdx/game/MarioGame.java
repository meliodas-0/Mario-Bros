package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Screens.PlayScreen;

public class MarioGame extends Game {
	public SpriteBatch batch;
	public static final int	V_height = 1080, V_width = 2160;
	public static final float PPM = 100;
	public static final short DEFAULT_BIT=1;
	public static final short MARIO_BIT=2;
	public static final short BRICK_BIT=4;
	public static final short COIN_BIT=8;
	public static final short DESTROYED_BIT=16;

	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}
}
