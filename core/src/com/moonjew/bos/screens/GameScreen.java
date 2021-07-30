package com.moonjew.bos.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.moonjew.bos.BlowingOffSteam;
import com.moonjew.bos.CollisionListener;
import com.moonjew.bos.entities.Fish;
import com.moonjew.bos.entities.Player;
import com.moonjew.bos.entities.Seaweed;
import com.moonjew.bos.entities.SteamVolcano;

import java.awt.*;

public class GameScreen implements Screen {
    public static final float PPM = 64;

    private final BlowingOffSteam app;

    //UI
    private Stage stage;
    private Skin skin;
    private BitmapFont font;
    private Table root;
    private Label steamLabel;

    private TextButton restartButton;

    //Game stuff
    private TiledMap tiledMap;
    private TiledMapTileLayer rockLayer;
    private OrthogonalTiledMapRenderer tmr;
    private Box2DDebugRenderer b2dr;
    private World world;
    private Player player;
    private CollisionListener collisionListener;

    int rows;
    Texture umbre;

    //Shaders

    Array<Fish> fish;
    Array<SteamVolcano> volcanoes;
    Array<Seaweed> seaweed;
    Texture testTexture;

    public GameScreen(BlowingOffSteam app){
        this.app = app;
        this.stage = new Stage(new StretchViewport(BlowingOffSteam.WIDTH, BlowingOffSteam.HEIGHT));

        initWorld();

        testTexture = new Texture(Gdx.files.internal("badlogic.jpg"));
        rows = 50;
        umbre = new Texture(Gdx.files.internal("umbre.png"));
    }

    public void initWorld(){
        this.world = new World(new Vector2(0,0), false);
        this.player = new Player(world);
        this.b2dr = new Box2DDebugRenderer();

        collisionListener = new CollisionListener(player);

        tiledMap = new TmxMapLoader().load("test.tmx");
        tmr = new OrthogonalTiledMapRenderer(tiledMap);

        fish = new Array<>();
        volcanoes = new Array<>();
        seaweed = new Array<>();

        app.cam.position.set(player.getBody().getPosition().x * PPM, player.getBody().getPosition().y * PPM, 0);

        world.setContactListener(collisionListener);
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get("rocks");
        rockLayer = layer;
        for (int row = 0; row < layer.getHeight(); row++) {
            for (int col = 0; col < layer.getWidth(); col++) {
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);
                if (cell == null) continue;
                if (cell.getTile() == null) continue;

                createBox((int) ((col + 0.5f) * PPM), (int) ((row + 0.5f) * PPM), (int)PPM, (int)PPM, true);
            }
        }
//        layer = (TiledMapTileLayer) tiledMap.getLayers().get("rockwall");
//        for (int row = 0; row < layer.getHeight(); row++) {
//            for (int col = 0; col < layer.getWidth(); col++) {
//                TiledMapTileLayer.Cell cell = layer.getCell(col, row);
//                if (cell == null) continue;
//                if (cell.getTile() == null) continue;
//
//                createBox((int) ((col + 0.5f) * PPM), (int) ((row + 0.5f) * PPM), (int)PPM, (int)PPM, true);
//            }
//        }
        layer = (TiledMapTileLayer) tiledMap.getLayers().get("seaweed");
        for (int row = 0; row < layer.getHeight(); row++) {
            for (int col = 0; col < layer.getWidth(); col++) {
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);
                if (cell == null) continue;
                if (cell.getTile() == null) continue;
                System.out.println("New seaweed");
                seaweed.add(new Seaweed(col + 0.5f, row + 0.5f, world));
            }
        }

        Texture fishTexture = new Texture(Gdx.files.internal("fish1.png"));
        layer = (TiledMapTileLayer) tiledMap.getLayers().get("fish");
        for (int row = 0; row < layer.getHeight(); row++) {
            for (int col = 0; col < layer.getWidth(); col++) {
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);
                if (cell == null) continue;
                if (cell.getTile() == null) continue;

                fish.add(new Fish(col + 0.5f, row + 0.5f, fishTexture, world));
            }
        }

//        volcanoes.add(new SteamVolcano(5, 6, world));
//        fish.add(new Fish(5, 7, new Texture(Gdx.files.internal("fish1.png")), world));
//        seaweed.add(new Seaweed(5,3, world));
//        seaweed.add(new Seaweed(4, 3, world));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        this.skin = new Skin(Gdx.files.internal("metalui/metal-ui.json"));
        this.font = new BitmapFont(Gdx.files.internal("font1.fnt"));

        root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        restartButton = new TextButton("Restart?", skin);
        restartButton.setBounds(Gdx.graphics.getWidth()/2f - restartButton.getWidth()/2,
                Gdx.graphics.getHeight()/2f - restartButton.getHeight()/2f,
                restartButton.getWidth(), restartButton.getHeight());
        restartButton.getStyle().font = this.font;
        restartButton.setStyle(restartButton.getStyle());
        restartButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                initWorld();
                root.removeActor(restartButton);
            }
        });

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

        if(Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            if(player.steam - player.steamCost >= 0) {
                player.createBubble();
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
        for (SteamVolcano volcano : volcanoes) {
            volcano.update(delta);
        }
        for (Seaweed seaweed : seaweed) {
            seaweed.update(delta);
        }

        handleInput();
        cameraUpdate();

        if(Math.round(player.steam) <= 0){
            root.add(restartButton).top().left();
        }

    }

    @Override
    public void render(float delta) {
        stage.getViewport().apply();
        Gdx.gl.glClearColor(.9f, .9f, .9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        app.sb.setProjectionMatrix(app.cam.combined);
        app.sb.begin();
        app.sb.draw(umbre, 0,0,BlowingOffSteam.WIDTH, rows * PPM);

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
        tmr.getBatch().begin();
        tmr.renderTileLayer(rockLayer);
        tmr.getBatch().end();

//        b2dr.render(world, app.cam.combined.scl(PPM));

        app.sb.begin();

        String steamText = "Steam: " + String.format("%.2f", player.steam);;
        steamLabel.setText(steamText);
        steamLabel.setX(app.cam.position.x - Gdx.graphics.getWidth()/2f);
        steamLabel.setY(app.cam.position.y - Gdx.graphics.getHeight()/2f);
        steamLabel.draw(app.sb, 0.5f);

        app.sb.setColor(1,1,1,0.2f);

        app.sb.draw(umbre, 0,0,BlowingOffSteam.WIDTH, -Gdx.graphics.getHeight() * 2);

        app.sb.end();

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