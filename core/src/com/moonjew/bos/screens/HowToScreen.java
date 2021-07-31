package com.moonjew.bos.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
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

public class HowToScreen implements Screen {
    private final BlowingOffSteam app;

    private Stage stage;
    private Skin skin;
    private BitmapFont font;

    public HowToScreen(final BlowingOffSteam app){
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
        TextButton textButton = new TextButton("How to Play", skin);
        textButton.getStyle().font = this.font;
        textButton.setStyle(textButton.getStyle());
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                app.setScreen(new TitleScreen(app));
            }
        });
        root.add(textButton).top().center().spaceBottom(150);

        root.row();

//        Label label = new Label("You play as an underwater explorer, \n" +
//                " navigating through the dangerous seas \n" +
//                "in his trusty steam-powered ship. \n " +
//                "To THRUST forward, press and hold space. \n" +
//                "To rotate, press the \n" +
//                "LEFT and RIGHT arrow keys. \n" +
//                "To stop, press BACK. When you THRUST, you lose STEAM. \n" +
//                "If you run out of STEAM, \n" +
//                "it's GAME OVER. Thankfully, the seas \n " +
//                "are filled with underwater volcanoes, which can \n " +
//                "restore your STEAM. Hover over them to refill! \n" +
//                "Try and avoid the fish, as you will lose \n " +
//                "STEAM if you bump into them. \n " +
//                "Can you find your way out of all 4 levels? \n " +
//                "Good luck!", skin);

        Label label = new Label("MOVE FORWARD->SPACE \n" +
                "ROTATE->LEFT/RIGHT \n " +
                "BRAKE->BACK \n " +
                "AVOID FISH \n " +
                "RESTORE STEAM \n IN VOLCANOES \n " +
                "GET TO THE END"
                , skin);

        label.getStyle().font = font;
        label.setAlignment(Align.center);
        label.setStyle(label.getStyle());
        root.add(label).center();

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

    }
}
