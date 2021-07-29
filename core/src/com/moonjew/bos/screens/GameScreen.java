package com.moonjew.bos.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.moonjew.bos.BlowingOffSteam;
import com.moonjew.bos.CollisionListener;
import com.moonjew.bos.entities.Fish;
import com.moonjew.bos.entities.Player;
import com.moonjew.bos.entities.Seaweed;
import com.moonjew.bos.entities.SteamVolcano;

public class GameScreen implements Screen {
    public static final float PPM = 64;

    private final BlowingOffSteam app;


    //UI
    private Stage stage;
    private Skin skin;
    private BitmapFont font;
    private Label steamLabel;

    //Game stuff
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tmr;
    private Box2DDebugRenderer b2dr;
    private World world;
    private Player player;
    private CollisionListener collisionListener;

    //Shaders

    Array<Fish> fish;
    Array<SteamVolcano> volcanoes;
    Array<Seaweed> seaweed;
    Texture testTexture;

    public GameScreen(BlowingOffSteam app){
        this.app = app;
        this.stage = new Stage(new FitViewport(BlowingOffSteam.WIDTH, BlowingOffSteam.HEIGHT, app.cam));
        this.world = new World(new Vector2(0,0), false);
        this.player = new Player(world);
        this.b2dr = new Box2DDebugRenderer();
        tiledMap = new TmxMapLoader().load("test.tmx");
        tmr = new OrthogonalTiledMapRenderer(tiledMap);
        fish = new Array<>();
        volcanoes = new Array<>();
        seaweed = new Array<>();
        collisionListener = new CollisionListener(player);
        initWorld();

        testTexture = new Texture(Gdx.files.internal("badlogic.jpg"));

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

        steamLabel = new Label("Steam: ", skin);

    }

    public void handleInput(){
        Body p = player.getBody();

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            p.setAngularVelocity(player.tempTurnSpeed);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            p.setAngularVelocity(-player.tempTurnSpeed);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            if(player.steam - player.steamCost >= 0) {
                p.applyForceToCenter(new Vector2(0, player.tempAccelSpeed).rotateRad(p.getAngle()), false);
                player.steam -= player.steamCost;
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            p.setLinearVelocity(p.getLinearVelocity().x * 0.93f, p.getLinearVelocity().y * 0.93f);
        }

        if(p.getLinearVelocity().x > player.tempMaxSpeed){
            p.setLinearVelocity(player.tempMaxSpeed, p.getLinearVelocity().y);
        } else if(p.getLinearVelocity().x < -player.tempMaxSpeed){
            p.setLinearVelocity(-player.tempMaxSpeed, p.getLinearVelocity().y);
        }
        if(p.getLinearVelocity().y > player.tempMaxSpeed){
            p.setLinearVelocity(p.getLinearVelocity().x, player.tempMaxSpeed);
        } else if(p.getLinearVelocity().y < -player.tempMaxSpeed){
            p.setLinearVelocity(p.getLinearVelocity().x, -player.tempMaxSpeed);
        }
    }

    public void cameraUpdate() {
        Vector3 position = app.cam.position;
        if (position.y <= (player.getBody().getPosition().y * PPM) + PPM * 2) {
            position.y = player.getBody().getPosition().y * PPM + PPM * 2;
        }
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

        for(Seaweed seaweed : seaweed){
            seaweed.render(app.sb);
        }
        for(SteamVolcano volcano : volcanoes){
            volcano.render(app.sb);
        }
        for(Fish fish : fish){
            fish.render(app.sb);
        }

        player.render(app.sb, app.cam);

        app.sb.end();

        stage.draw();

        tmr.setView(app.cam);
        tmr.render();

        b2dr.render(world, app.cam.combined.scl(PPM));

        app.sb.begin();

        String steamText = "Steam: " + String.format("%.2f", player.steam);;
        steamLabel.setText(steamText);
        steamLabel.setX(app.cam.position.x - Gdx.graphics.getWidth()/2f);
        steamLabel.setY(app.cam.position.y - Gdx.graphics.getHeight()/2f);
        steamLabel.draw(app.sb, 0.5f);

        app.sb.end();

    }

    public void initWorld(){
        world.setContactListener(collisionListener);
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get("rocks");

        for (int row = 0; row < layer.getHeight(); row++) {
            for (int col = 0; col < layer.getWidth(); col++) {
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);
                if (cell == null) continue;
                if (cell.getTile() == null) continue;

                createBox((int) ((col + 0.5f) * PPM), (int) ((row + 0.5f) * PPM), (int)PPM, (int)PPM, true);
            }
        }

        volcanoes.add(new SteamVolcano(5, 6, world));
        fish.add(new Fish(5, 5, new Texture(Gdx.files.internal("fish1.png")), world));
        seaweed.add(new Seaweed(5,3, world));
        seaweed.add(new Seaweed(4, 3, world));
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