package com.moonjew.bos.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.moonjew.bos.BlowingOffSteam;
import com.moonjew.bos.Map;
import com.moonjew.bos.entities.Player;

public class GameScreen implements Screen {
    public static final float PPM = 32;

    private final BlowingOffSteam app;


    //UI
    private Stage stage;
    private Skin skin;
    private BitmapFont font;

    //Game stuff
    private Box2DDebugRenderer b2dr;
    private World world;
    private Player player;

    public GameScreen(BlowingOffSteam app){
        this.app = app;
        this.stage = new Stage(new FitViewport(BlowingOffSteam.WIDTH, BlowingOffSteam.HEIGHT, app.cam));
        this.world = new World(new Vector2(0,0), false);
        this.player = new Player(world);
        this.b2dr = new Box2DDebugRenderer();

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        this.skin = new Skin(Gdx.files.internal("metalui/metal-ui.json"));
        this.font = new BitmapFont(Gdx.files.internal("font1.fnt"));

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        TextButton test = new TextButton("Game", skin);
        test.getStyle().font = this.font;
        test.setStyle(test.getStyle());

        root.add(test);

    }

    public void handleInput(){
        Body p = player.getBody();

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            p.setAngularVelocity(p.getAngularVelocity() + 5);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            p.setAngularVelocity(p.getAngularVelocity() - 5);
        }
        p.setAngularDamping(0.93f);

        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            p.applyForceToCenter(new Vector2(0,10).rotateDeg(p.getAngle()), false);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            p.setLinearVelocity(p.getLinearVelocity().x * 0.93f, p.getLinearVelocity().y * 0.93f);
        }
        p.setLinearDamping(0.98f);

        if(p.getLinearVelocity().x > 2.5f){
            p.setLinearVelocity(2.5f, p.getLinearVelocity().y);
        } else if(p.getLinearVelocity().x < -2.5f){
            p.setLinearVelocity(-2.5f, p.getLinearVelocity().y);
        }
        if(p.getLinearVelocity().y > 2.5f){
            p.setLinearVelocity(p.getLinearVelocity().x, 2.5f);
        } else if(p.getLinearVelocity().y < -2.5f){
            p.setLinearVelocity(p.getLinearVelocity().x, -2.5f);
        }


    }

    public void cameraUpdate() {
        Vector3 position = app.cam.position;
        position.x = player.getBody().getPosition().x * PPM;
        position.y = player.getBody().getPosition().y * PPM;
        app.cam.position.set(position);
        app.cam.update();

    }

    public void update(float delta){
        stage.act(delta);
        world.step(1/60f, 6, 2);

        handleInput();
        cameraUpdate();

        player.update(delta, app.cam);
    }

    @Override
    public void render(float delta) {
        stage.getViewport().apply();
        Gdx.gl.glClearColor(.9f, .9f, .9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        app.sb.begin();
        player.render(app.sb, app.cam);
        stage.draw();
        app.sb.end();

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
