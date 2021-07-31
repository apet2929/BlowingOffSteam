package com.moonjew.bos.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

public class GameScreen implements Screen {
    public static final float PPM = 64;

    private final BlowingOffSteam app;
    public CutsceneState cutsceneState;

    //UI
    private Stage stage;
    private Skin skin;
    private BitmapFont font;
    private Table root;
    private Label steamLabel;
    private Label scoreLabel;
    private float score;

    private TextButton restartButton;
    private TextButton nextLevelButton;

    //Game stuff
    public int level;
    private Array<TiledMap> levels;
    private TiledMapTileLayer rockLayer;
    private TiledMapTileLayer seaweedLayer;
    private OrthogonalTiledMapRenderer tmr;
    private Box2DDebugRenderer b2dr;
    private World world;
    private Player player;
    private CollisionListener collisionListener;
    int rows;
    Texture umbre;
    Texture steamBar;
    Texture emptyBar;
    boolean dead;
    boolean endOfLevel;

    Array<Fish> fish;
    Array<SteamVolcano> volcanoes;
    Array<Seaweed> seaweed;

    public GameScreen(BlowingOffSteam app, int level){
        this.level = level;
        this.app = app;
        cutsceneState = new CutsceneState(app, this);
    }

    public void loadLevels(){
        levels = new Array<>();
        TmxMapLoader loader = new TmxMapLoader();
        levels.add(loader.load("level1.tmx"));
        levels.add(loader.load("level2.tmx"));
        levels.add(loader.load("level3.tmx"));

    }

    public void initWorld(){
        this.world = new World(new Vector2(0,0), false);
        this.player = new Player(world);
        dead = false;
        endOfLevel = false;
        this.b2dr = new Box2DDebugRenderer();
        this.score = (level + 1) * 1000;

        collisionListener = new CollisionListener(player);

        tmr = new OrthogonalTiledMapRenderer(levels.get(level));

        fish = new Array<>();
        volcanoes = new Array<>();
        seaweed = new Array<>();

        app.cam.position.set(player.getBody().getPosition().x * PPM, player.getBody().getPosition().y * PPM, 0);

        world.setContactListener(collisionListener);
        TiledMapTileLayer layer = (TiledMapTileLayer) levels.get(level).getLayers().get("rocks");
        rockLayer = layer;
        for (int row = 0; row < layer.getHeight(); row++) {
            for (int col = 0; col < layer.getWidth(); col++) {
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);
                if (cell == null) continue;
                if (cell.getTile() == null) continue;

                createBox((int) ((col + 0.5f) * PPM), (int) ((row + 0.5f) * PPM), (int)PPM, (int)PPM, true);
            }
        }

        layer = (TiledMapTileLayer) levels.get(level).getLayers().get("seaweed");
        seaweedLayer = layer;
        for (int row = 0; row < layer.getHeight(); row++) {
            for (int col = 0; col < layer.getWidth(); col++) {
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);
                if (cell == null) continue;
                if (cell.getTile() == null) continue;
//                System.out.println("New seaweed");
                seaweed.add(new Seaweed(col + 0.5f, row + 0.5f, world));
            }
        }

        Texture fishTexture = new Texture(Gdx.files.internal("fish1.png"));
        layer = (TiledMapTileLayer) levels.get(level).getLayers().get("fish");
        for (int row = 0; row < layer.getHeight(); row++) {
            for (int col = 0; col < layer.getWidth(); col++) {
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);
                if (cell == null) continue;
                if (cell.getTile() == null) continue;
                System.out.println(col + " | " + row);
                fish.add(new Fish(col + 0.5f, row + 0.5f, fishTexture, world));
            }
        }

        layer = (TiledMapTileLayer) levels.get(level).getLayers().get("volcanoes");
        if(layer != null) {
            for (int row = 0; row < layer.getHeight(); row++) {
                for (int col = 0; col < layer.getWidth(); col++) {
                    TiledMapTileLayer.Cell cell = layer.getCell(col, row);
                    if (cell == null) continue;
                    if (cell.getTile() == null) continue;

                    volcanoes.add(new SteamVolcano(col + 0.5f, row + 0.5f, world));
                }
            }
        }

        createBoxScaled(-1, -2, 1, 150, true);
        createBoxScaled(11, -2, 1, 150, true);
    }

    @Override
    public void show() {

        loadLevels();
        initWorld();

        rows = 100;
        umbre = new Texture(Gdx.files.internal("umbre.png"));
        steamBar = new Texture("steambar.png");
        emptyBar = new Texture("emptybar.png");

        this.stage = new Stage(new StretchViewport(BlowingOffSteam.WIDTH, BlowingOffSteam.HEIGHT));

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

        nextLevelButton = new TextButton("Next Level", skin);
        nextLevelButton.setBounds(Gdx.graphics.getWidth()/2f - nextLevelButton.getWidth()/2,
                Gdx.graphics.getHeight()/2f - nextLevelButton.getHeight()/2f,
                nextLevelButton.getWidth(), nextLevelButton.getHeight());
        nextLevelButton.getStyle().font = this.font;
        nextLevelButton.setStyle(nextLevelButton.getStyle());
        final GameScreen gameScreen = this;
        nextLevelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                level++;
                cutsceneState.curLine = 0;
                app.setScreen(cutsceneState);

                if(level != levels.size) {
                    initWorld();
                } else {
                    dispose();
                }
                root.removeActor(nextLevelButton);
            }
        });

        scoreLabel = new Label("Score: ", skin);
        scoreLabel.getStyle().font = font;
        scoreLabel.setStyle(scoreLabel.getStyle());
        scoreLabel.setColor(1,1,1,1);
        root.add(scoreLabel).padBottom(500);
    }

    public void handleInput(){
        if(!endOfLevel) {
            Body p = player.getBody();

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                p.setAngularVelocity(player.tempTurnSpeed);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                p.setAngularVelocity(-player.tempTurnSpeed);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                if (player.steam - player.steamCost >= 0) {
                    player.createBubble();
                    p.applyForceToCenter(new Vector2(0, player.tempAccelSpeed).rotateRad(p.getAngle()), false);
                    player.steam -= player.steamCost;

                }
            }

            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
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
        else if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && endOfLevel){
            level++;
            initWorld();
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

        if((Math.round(player.steam) <= 0 || player.getBody().getPosition().y * PPM + PPM/2f< app.cam.position.y - app.cam.viewportHeight/2f) && !dead){
            player.steam = 0;
            root.row();
            root.add(restartButton).padTop(-500);
            System.out.println("dead = " + dead);
            dead = true;
        }

        if(dead){
            restartButton.act(delta);
        }
        else if(!endOfLevel){
            score -= delta * 8.5f;
            scoreLabel.setText("Score: " + (int) score);
        }

        if(player.getBody().getPosition().y >= 90){
            endOfLevel = true;
            root.row();
            root.add(nextLevelButton).padTop(-500);

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
        app.sb.draw(umbre, 0,0, BlowingOffSteam.WIDTH, rows * PPM);

//        for(Seaweed seaweed : seaweed){
//            seaweed.render(app.sb);
//        }
        for(SteamVolcano volcano : volcanoes){
            volcano.render(app.sb);
        }
        for(Fish fish : fish){
            fish.render(app.sb);
        }

        player.render(app.sb, app.cam);
        app.sb.end();

        tmr.setView(app.cam);
        tmr.getBatch().begin();
        tmr.renderTileLayer(seaweedLayer);
        tmr.renderTileLayer(rockLayer);
        tmr.getBatch().end();

//        b2dr.render(world, app.cam.combined.scl(PPM));

        app.sb.begin();

//        scoreLabel.setX(app.cam.position.x - Gdx.graphics.getWidth()/2f);
//        scoreLabel.setY(app.cam.position.y - Gdx.graphics.getHeight()/2f);
//        scoreLabel.draw(app.sb, 0.5f);

        app.sb.draw(emptyBar, app.cam.position.x - 150, app.cam.position.y + 275, 300, 15);
        app.sb.draw(steamBar, app.cam.position.x - 150, app.cam.position.y + 275, player.steam * 3, 15);

        app.sb.end();

        stage.draw();

    }

    public Body createBox(float x, float y, float width, float height, boolean isStatic){
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

    public Body createBoxScaled(float x, float y, float width, float height, boolean isStatic){
        return createBox(x * PPM, y * PPM, width * PPM, height * PPM, isStatic);
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