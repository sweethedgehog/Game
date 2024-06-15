package com.mygdx.game;

import static com.mygdx.game.GameResources.FONT_PATH;
import static com.mygdx.game.GameSettings.POSITION_ITERATIONS;
import static com.mygdx.game.GameSettings.SCREEN_HEIGHT;
import static com.mygdx.game.GameSettings.SCREEN_WIDTH;
import static com.mygdx.game.GameSettings.STEP_TIME;
import static com.mygdx.game.GameSettings.VELOCITY_ITERATIONS;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.components.FontBuilder;
import com.mygdx.game.managers.AudioManager;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.screens.MenuScreen;
import com.mygdx.game.screens.SettingsScreen;

public class MyGdxGame extends Game {
	public SpriteBatch batch;
	public OrthographicCamera camera;
	public GameScreen gameScreen;
	public MenuScreen menuScreen;
	public SettingsScreen settingsScreen;
	public World world;
	float accumulator = 0;
	public Vector3 touch;
	public BitmapFont largeWhiteFont;
	public BitmapFont commonWhiteFont;
	public BitmapFont commonBlackFont;
	public AudioManager audioManager;

	@Override
	public void create () {
		Box2D.init();
		world = new World(new Vector2(0, 0), true);
		largeWhiteFont = FontBuilder.generate(48, Color.WHITE, FONT_PATH);
		commonWhiteFont = FontBuilder.generate(24, Color.WHITE, FONT_PATH);
		commonBlackFont = FontBuilder.generate(24, Color.BLACK, FONT_PATH);
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
		gameScreen = new GameScreen(this);
		menuScreen = new MenuScreen(this);
		settingsScreen = new SettingsScreen(this);
		audioManager = new AudioManager();

		setScreen(menuScreen);

	}

	
	@Override
	public void dispose () {
		batch.dispose();
	}
	public void worldStep(){
		float delta = Gdx.graphics.getDeltaTime();
		accumulator += delta;
		if (accumulator >= STEP_TIME){
			accumulator -= STEP_TIME;
			world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
		}
	}
}
