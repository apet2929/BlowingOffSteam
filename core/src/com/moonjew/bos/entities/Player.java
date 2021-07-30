package com.moonjew.bos.entities;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.moonjew.bos.Animation;
import com.moonjew.bos.screens.GameScreen;

import java.lang.ref.SoftReference;
import java.util.Iterator;

import static com.moonjew.bos.screens.GameScreen.PPM;

public class Player {
    private Animation animation;
    private Sprite sprite;
    private Body body;
    private Array<Sprite> bubbles;

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

        this.bubbles = new Array<>(30);

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

        Iterator<Sprite> bubbleIterator = bubbles.iterator();
        while (bubbleIterator.hasNext()){
            Sprite bubble = bubbleIterator.next();
            bubble.setScale(bubble.getScaleX() * 0.95f, bubble.getScaleY() * 0.95f);
            bubble.setAlpha(bubble.getColor().a * 0.98f);
            if(bubble.getScaleX() < 0.1f){
                bubbleIterator.remove();
            }
        }

    }

    public void render(SpriteBatch sb, Camera cam) {
        TextureRegion r = animation.getFrame();
        sprite.setRegion(r);
        sb.setProjectionMatrix(cam.combined);
        for(Sprite bubble : bubbles){
            bubble.draw(sb);
        }
        sprite.draw(sb);


    }

    public void createBubble(){
        Sprite b = new Sprite(new Texture(Gdx.files.internal("bubble.png")));
        int xOffset = (int) (Math.random()*16 - 8);
        Vector2 pos = new Vector2(xOffset, -sprite.getHeight()/2);

        pos.rotateDeg(body.getAngle());
        b.setBounds((body.getPosition().x ) * PPM + pos.x - sprite.getWidth()/2f, (body.getPosition().y) * PPM + pos.y, PPM, PPM);
        b.setOriginCenter();
        bubbles.add(b);
    }

    public Body getBody() {
        return body;
    }

    public void dispose(){
        animation.dispose();
        sprite.getTexture().dispose();
    }
}