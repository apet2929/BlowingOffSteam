package com.moonjew.bos;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.moonjew.bos.screens.CreditState;
import com.moonjew.bos.screens.CutsceneState;
import com.moonjew.bos.screens.GameScreen;
import com.moonjew.bos.screens.TitleScreen;

public class BlowingOffSteam extends Game {

	public static final int WIDTH = 640;
	public static final int HEIGHT = 600;

	public OrthographicCamera cam;
	public SpriteBatch sb;

	@Override
	public void create () {
		sb = new SpriteBatch();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, WIDTH / 2f, HEIGHT / 2f);

		this.setScreen(new TitleScreen(this));
	}

	@Override
	public void render () {
		super.render();

		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
			Gdx.app.exit();
		}

	}
	
	@Override
	public void dispose () {
		sb.dispose();

	}
}
