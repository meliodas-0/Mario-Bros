package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Screens.PlayScreen;

public class MarioGame extends Game {
	public SpriteBatch batch;
	public static final int	V_height = 1080, V_width = 2160;
	public static final float PPM = 100;
	public static final short NOTHING_BIT =0;
	public static final short GROUND_BIT =1;
	public static final short MARIO_BIT=2;
	public static final short BRICK_BIT=4;
	public static final short COIN_BIT=8;
	public static final short DESTROYED_BIT=16;
	public static final short OBJECT_BIT=32;
	public static final short ENEMY_BIT=64;
	public static final short ENEMY_HEAD_BIT=128;
	public static final short ITEM_BIT=256;
	public static final short POWERUP_BIT=512;
	public static final short MARIO_HEAD_BIT=1024;
	public static final short FLAG_BIT=2048;



	public static AssetManager manager;

	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load("Audio/Music/mario_music.ogg", Music.class);
		manager.load("Audio/Sounds/coin.wav", Sound.class);
		manager.load("Audio/Sounds/bump.wav", Sound.class);
		manager.load("Audio/Sounds/breakblock.wav", Sound.class);
		manager.load("Audio/Sounds/powerup.wav", Sound.class);
		manager.load("Audio/Sounds/powerup_spawn.wav", Sound.class);
		manager.load("Audio/Sounds/powerdown.wav", Sound.class);
		manager.load("Audio/Sounds/stomp.wav", Sound.class);
		manager.load("Audio/Sounds/mariodie.wav", Sound.class);
		manager.load("Audio/Sounds/stageclear.wav", Sound.class);

		manager.finishLoading();

		setScreen(new PlayScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		super.dispose();
		manager.dispose();
		batch.dispose();
	}
}
