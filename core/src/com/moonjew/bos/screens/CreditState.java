package com.moonjew.bos.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.moonjew.bos.BlowingOffSteam;

public class CreditState implements Screen {
    private final BlowingOffSteam app;

    private Stage stage;
    private Skin skin;
    private BitmapFont font;

    public CreditState(final BlowingOffSteam app){
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

        root.setDebug(true);

        root.defaults().expand();

        Table table = new Table();
        table.defaults().uniform().space(10);

        table.add();

        Label label = new Label("Credits", skin);
        label.getStyle().font = font;
        label.setStyle(label.getStyle());
        label.setColor(0,0,0,1);
        Label.LabelStyle style = label.getStyle();

        table.add(label);

        TextButton textButton = new TextButton("Back", skin);
        textButton.getStyle().font = this.font;
        textButton.setStyle(textButton.getStyle());
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                app.setScreen(new TitleScreen(app));
                dispose();
            }
        });

        table.add(textButton);

        root.add(table);

        root.row();

        table.defaults().expandX().uniform();
        table = new Table();
        label = new Label("Art", skin);
        label.setStyle(style);
        table.add(label).uniform();

        table.add().expandX().width(Value.percentWidth(0.5f, label));

        label = new Label("CeaShell", skin);
        label.setStyle(style);
        table.add(label).uniform();


        root.add(table).expandX();

        root.row();

        table.defaults().expandX().uniform();
        table = new Table();
        label = new Label("Gameplay", skin);
        label.setStyle(style);
        table.add(label).uniform();

        table.add().expandX().width(Value.percentWidth(0.5f, label));

        label = new Label("MoonJew", skin);
        label.setStyle(style);
        table.add(label).uniform();


        root.add(table).expandX().top();
//        root.defaults().uniform().top().padTop(50).expand();
//        root.add();
//

//

//
//        root.row();
//
//        root.defaults().reset();
//        Table table = new Table();
//
//        label = new Label("Art:", skin);
//        label.setStyle(style);
//        table.add(label).right().padLeft(50);
//
//        table.row();
//        table.add();
//        table.row();
//
//        label = new Label("Gameplay:", skin);
//        label.setStyle(style);
//        table.add(label).right().padLeft(50);
//
//        root.add(table).expand().uniform();
////
//        root.add().width(50);
//
//        table = new Table();
//        label = new Label("CeaShell", skin);
//        label.setStyle(style);
//        table.add(label).left().padRight(50);
//
//        table.row();
//        table.add();
//        table.row();
//
//        label = new Label("MoonJew", skin);
//        label.setStyle(style);
//        table.add(label).left();
//
//        root.add(table).expand().padRight(50);

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
        app.sb.begin();
        app.sb.draw(new Texture("menupic.png"), 0, 0, BlowingOffSteam.WIDTH, BlowingOffSteam.HEIGHT);
        app.sb.end();
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

