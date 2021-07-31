package com.moonjew.bos.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.moonjew.bos.BlowingOffSteam;

public class TitleScreen implements Screen {
    private final BlowingOffSteam app;

    private Stage stage;
    private Skin skin;
    private BitmapFont font;

    public TitleScreen(final BlowingOffSteam app){
        this.app = app;
        this.stage = new Stage(new FitViewport(BlowingOffSteam.WIDTH, BlowingOffSteam.HEIGHT, app.cam));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        this.skin = new Skin(Gdx.files.internal("metalui/metal-ui.json"));
        this.font = new BitmapFont(Gdx.files.internal("font1.fnt"));
        Table root = new Table();

        stage.addActor(root);
        root.setFillParent(true);

        root.defaults().pad(10);
        TextButton.TextButtonStyle style;
//        TextButton textButton = new TextButton("", skin);
//        textButton.getStyle().font = this.font;
//        textButton.setStyle(textButton.getStyle());
//        style = textButton.getStyle();
//        root.add(textButton).top().center().spaceBottom(150);



        Label title = new Label("Steam Ship\nAdventure!", skin);
        title.getStyle().font = font;
        title.setStyle(title.getStyle());
        title.setColor(0,0,0,1);
        root.add(title).padBottom(150);

        root.row();
        root.row();
        root.row();

        root.row();

        root.defaults().width(300).height(60);

        TextButton textButton = new TextButton("PLAY", skin);
        textButton.getStyle().font = this.font;
        textButton.setStyle(textButton.getStyle());
        style = textButton.getStyle();
        textButton.setStyle(style);
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("TitleScreen.changed");
                app.setScreen(new CutsceneState(app, new GameScreen(app, 0)));
                dispose();
            }
        });
        root.add(textButton).uniform();

        root.row();


        textButton = new TextButton("HOW TO", skin);
        textButton.setStyle(style);
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                app.setScreen(new HowToScreen(app));
            }
        });
        root.add(textButton).uniform();

        root.row();
        textButton = new TextButton("CREDITS", skin);
        textButton.setStyle(style);
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                app.setScreen(new GameScreen(app, 0));
            }
        });
        root.add(textButton).uniform();

        root.row();

        textButton = new TextButton("QUIT", skin);
        textButton.setStyle(style);
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        root.add(textButton).uniform();

        root.row();
    }

    public void update(float delta){
        stage.act(delta);
    }
    @Override
    public void render(float delta) {
        stage.getViewport().apply();
        Gdx.gl.glClearColor(.9f, .9f, .9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);
        stage.getBatch().begin();
        stage.getBatch().draw(new Texture("menupic.png"), 0, 0, BlowingOffSteam.WIDTH, BlowingOffSteam.HEIGHT);
        stage.getBatch().end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        skin.dispose();
        font.dispose();
    }

}
