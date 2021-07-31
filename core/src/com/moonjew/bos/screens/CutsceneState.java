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
import com.badlogic.gdx.utils.Align;
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
    public int curLine;
    private String[] lines;
    private String[] lines2;
    private String[] lines3;
    private String[] lines4;

    public CutsceneState(final BlowingOffSteam app, GameScreen screen) {
        this.app = app;
        this.game = screen;
        curLine = 0;
        lines = new String[]{
                "Once upon a time", "there was a little\nunderwater ship.",
                "It was powered by\nsteam from\nunderwater\nvolcanoes.",
                "And so it set off\non a quest to\nexplore the deep", "blue", "sea."
        };
        lines2 = new String[] {
                "It had \nsuccessfully \ntravelled one \nlayer of the \ndeep ocean,",
                "perilously \navoiding \nvicious sea \ncreatures.",
                "But there\nwas another, \nmore challenging\n layer to come.",
                "He was on a quest.",
                "What was he \nsearching for?",
                "We shall see."
        };

        lines3 = new String[] {
                "As another\nlayer passes\nby,", "the little\nship knows his\ngoal is within\nreach." , "But,", "his success\nis not yet\nguaranteed.", "Anything can\nstill go wrong...", "Will it?"
        };

        lines4 = new String[] {
                "Wow., You've done it.", "You completed\nthe journey.", "What was the\npoint?", "Well, when you\nstarted playing\nyou were\nfrustrated with\nthe controls.", "Now, you are\nless disappointed.\n (I hope)", "So, you blew off\nsome steam."
        };

        skin = new Skin(Gdx.files.internal("metalui/metal-ui.json"));
        font = new BitmapFont(Gdx.files.internal("font1.fnt"));

        System.out.println("CutsceneState.CutsceneState");
    }

    @Override
    public void show() {
        stage = new Stage(new StretchViewport(BlowingOffSteam.WIDTH, BlowingOffSteam.HEIGHT));
        Table root = new Table();
        stage.addActor(root);
        root.setFillParent(true);

        if (game.level == 0) {
            label = new Label(lines[0], skin);
        }
        else if (game.level == 1) {
            label = new Label(lines2[0], skin);
        }
        else if (game.level == 2) {
            label = new Label(lines3[0], skin);
        }
        else if (game.level == 3) {
            label = new Label(lines4[0], skin);
        }
        System.out.println("game.level = " + game.level);
        label.getStyle().font = font;
        label.getStyle().fontColor = Color.WHITE;
        label.setAlignment(Align.center);
        label.setStyle(label.getStyle());
        root.add(label);
//        curLine = 0;
//        label.setPosition(BlowingOffSteam.WIDTH/2f - label.getWidth()/2f, BlowingOffSteam.HEIGHT/2f - label.getHeight()/2f);
    }

    public void update(float delta){
        if(Gdx.input.justTouched()){
            curLine++;

            if (game.level == 0) {
                System.out.println("Level = 0");
                if(curLine >= lines.length){
                    curLine = 0;
                    app.setScreen(game);
                    return;
                }
                label.setText(lines[curLine]);
            }
            else if (game.level == 1) {
                System.out.println("Level = 1");
                if(curLine >= lines2.length){
                    curLine = 0;
                    app.setScreen(game);
                    return;
                }
                label.setText(lines2[curLine]);
            } else if(game.level == 2){
                if(curLine >= lines3.length){
                    curLine = 0;
                    app.setScreen(game);
                    return;
                }
                label.setText(lines3[curLine]);
            } else if(game.level == 3){
                if(curLine >= lines4.length){
                    curLine = 0;
                    app.setScreen(new CreditState(app));
                    return;
                }
                label.setText(lines4[curLine]);
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);
        stage.draw();
//        System.out.println(curLine);

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
