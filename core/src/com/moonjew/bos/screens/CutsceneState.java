package com.moonjew.bos.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.moonjew.bos.BlowingOffSteam;
import org.w3c.dom.Text;

import java.util.Locale;

public class CutsceneState implements Screen {
    private final BlowingOffSteam app;
    private GameScreen game;

    private Skin skin;
    private BitmapFont font;
    private Label label;
    private Stage stage;
    private int curLine;
    private String[] lines;

    public CutsceneState(final BlowingOffSteam app, GameScreen screen) {
        this.app = app;
        this.game = screen;
        curLine = 0;
        lines = new String[]{
                "Test 1", "Test 2", "Test 3"
        };
        System.out.println("CutsceneState.CutsceneState");
    }
    @Override
    public void show() {
        stage = new Stage(new StretchViewport(BlowingOffSteam.WIDTH, BlowingOffSteam.HEIGHT));
        Table root = new Table();
        stage.addActor(root);
        root.setFillParent(true);
        skin = new Skin(Gdx.files.internal("metalui/metal-ui.json"));
        font = new BitmapFont(Gdx.files.internal("font1.fnt"));
        label = new Label(lines[0], skin);
        label.getStyle().font = font;
        label.getStyle().fontColor = Color.WHITE;
        label.setStyle(label.getStyle());
        root.add(label);
//        curLine = 0;
//        label.setPosition(BlowingOffSteam.WIDTH/2f - label.getWidth()/2f, BlowingOffSteam.HEIGHT/2f - label.getHeight()/2f);
    }

    public void update(float delta){
        if(Gdx.input.justTouched()){
            curLine++;

            if(curLine >= lines.length){
                app.setScreen(game);
                return;
            }
            label.setText(lines[curLine]);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);
        stage.draw();
        System.out.println(curLine);

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

    }
}
