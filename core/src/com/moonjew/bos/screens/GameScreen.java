package com.moonjew.bos.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.moonjew.bos.BlowingOffSteam;
import com.moonjew.bos.CollisionListener;
import com.moonjew.bos.entities.Fish;
import com.moonjew.bos.entities.Player;

import java.util.ArrayList;

public class GameScreen implements Screen {
    public static final float PPM = 32;

    private final BlowingOffSteam app;


    //UI
    private Stage stage;
    private Skin skin;
    private BitmapFont font;

    //Game stuff
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tmr;
    private Box2DDebugRenderer b2dr;
    private World world;
    private Player player;

    TextureRegion rock;
    Array<Body> rocks;
    Array<Fish> fish;

    public GameScreen(BlowingOffSteam app){
        this.app = app;
        this.stage = new Stage(new FitViewport(BlowingOffSteam.WIDTH, BlowingOffSteam.HEIGHT, app.cam));
        this.world = new World(new Vector2(0,0), false);
        this.player = new Player(world);
        this.b2dr = new Box2DDebugRenderer();
        rocks = new Array<>(100);
        rock = new TextureRegion(new Texture(Gdx.files.internal("rock.png")), 64, 0, 32, 32);
        tiledMap = new TmxMapLoader().load("test.tmx");
        tmr = new OrthogonalTiledMapRenderer(tiledMap);
        fish = new Array<>();
        initWorld();

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

        root.add(test).top().left();

    }

    public void handleInput(){
        Body p = player.getBody();

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            p.setAngularVelocity(2);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            p.setAngularVelocity(-2);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            p.applyForceToCenter(new Vector2(0,10).rotateRad(p.getAngle()), false);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            p.setLinearVelocity(p.getLinearVelocity().x * 0.93f, p.getLinearVelocity().y * 0.93f);
        }

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


        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
        player.update(delta, app.cam);
        for(Fish fish : fish){
            fish.update(delta);
        }

        handleInput();
        cameraUpdate();


    }

    @Override
    public void render(float delta) {
        stage.getViewport().apply();
        Gdx.gl.glClearColor(.9f, .9f, .9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        app.sb.setProjectionMatrix(app.cam.combined);
        app.sb.begin();
        player.render(app.sb, app.cam);
        int w = rock.getRegionWidth() / 2;
        int h = rock.getRegionHeight() / 2;
        for(Body body : rocks){
            app.sb.draw(rock, body.getPosition().x * PPM - w, body.getPosition().y * PPM - h);
        }
        app.sb.end();
        stage.draw();

        tmr.setView(app.cam);
        tmr.render();

        b2dr.render(world, app.cam.combined.scl(PPM));

    }

    public void initWorld(){
        world.setContactListener(new CollisionListener());
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get("rocks");
        for (int row = 0; row < layer.getHeight(); row++) {
            for (int col = 0; col < layer.getWidth(); col++) {
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);
                if(cell == null) continue;
                if(cell.getTile() == null) continue;

                createBox((int) ((col + 0.5f) * 32), (int) ((row + 0.5f) * 32), 32, 32, true);
            }
        }
        fish.add(new Fish(5, 5, new Texture(Gdx.files.internal("rock.png")), world));
    }

    public Body createBox(int x, int y, int width, int height, boolean isStatic){
        Body pBody;
        BodyDef def = new BodyDef();

        if(isStatic) def.type = BodyDef.BodyType.StaticBody;
        else def.type = BodyDef.BodyType.DynamicBody;

        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = true;
        pBody = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2f / PPM, height / 2f / PPM);
        pBody.createFixture(shape, 1.0f);
        shape.dispose();

        return pBody;
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
        player.dispose();
        world.dispose();
        b2dr.dispose();

    }
}
