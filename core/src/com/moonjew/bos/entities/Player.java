package com.moonjew.bos.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.moonjew.bos.Animation;

import static com.moonjew.bos.screens.GameScreen.PPM;

public class Player {
    private Animation animation;
    private Sprite sprite;
    private Body body;

//    Game mechanics

    private float accelSpeed;
    private float turnSpeed;
    private float maxSpeed;
    private float maxSteam;

    public float steam;
    public float tempAccelSpeed;
    public float tempTurnSpeed;
    public float tempMaxSpeed;
    public float steamCost;

    public boolean inVolcano;
    public boolean inSeaweed;

    public Player(World world) {
        this.animation = new Animation(new TextureRegion(new Texture(Gdx.files.internal("ship_animations.png")), 64, 32), 2, 0.5f);
        this.sprite = new Sprite(animation.getFrame());
        this.sprite.setBounds(Gdx.graphics.getWidth()/2f - sprite.getWidth()/2 ,
                Gdx.graphics.getHeight()/2f, PPM , PPM);
        this.sprite.setOriginCenter();

        BodyDef def = new BodyDef();

        def.type = BodyDef.BodyType.DynamicBody;

        int spawnX = -1;
        int spawnY = -3;
        def.position.set((sprite.getX() / PPM) + spawnX, (sprite.getY() / PPM) + spawnY);
        body = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(((sprite.getWidth() / 2f) - 8) / PPM, ((sprite.getHeight() / 2f) - 8) / PPM);

        body.createFixture(shape, 1.0f).setUserData("player");
        shape.dispose();
        body.setLinearDamping(0.93f);
        body.setAngularDamping(0.93f);
        body.setUserData("player");

        this.maxSteam = 100f;
        this.steam = 100f;
        this.accelSpeed = tempAccelSpeed = 10f;
        this.turnSpeed = tempTurnSpeed = 2f;
        this.maxSpeed = tempMaxSpeed = 2.5f;
        this.steamCost = 0.1f;

    }

    public void update(float delta, Camera cam){
        animation.update(delta);
        this.sprite.setRotation((float) Math.toDegrees(body.getAngle()));
        this.sprite.setPosition( body.getPosition().x * PPM - sprite.getWidth()/2, body.getPosition().y * PPM - sprite.getHeight()/2);

        if(inVolcano){
            if(steam + 5 * steamCost <= maxSteam) steam += 5 * steamCost;
            else steam = maxSteam;
        }
        if(inSeaweed){
            tempMaxSpeed = maxSpeed/2;
            tempAccelSpeed = accelSpeed/2;
        }

        if(steam < 0) steam = 0;
        else if(steam < tempAccelSpeed) {
            tempAccelSpeed = tempMaxSpeed * (steam / tempAccelSpeed);
        } else if(!inSeaweed) {
            this.tempAccelSpeed = accelSpeed;
            this.tempTurnSpeed = turnSpeed;
            this.tempMaxSpeed = maxSpeed;
        }

    }

    public void render(SpriteBatch sb, Camera cam) {
        TextureRegion r = animation.getFrame();
        sprite.setRegion(r);
        sb.setProjectionMatrix(cam.combined);
        sprite.draw(sb);
    }

    public Body getBody() {
        return body;
    }

    public void dispose(){
        animation.dispose();
        sprite.getTexture().dispose();
    }
}