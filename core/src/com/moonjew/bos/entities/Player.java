package com.moonjew.bos.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.moonjew.bos.Animation;

import static com.moonjew.bos.screens.GameScreen.PPM;

public class Player {
    private Animation animation;
    private Sprite sprite;
    private Body body;

//    Game mechanics

    public float steam;
    public float accelSpeed;
    public float turnSpeed;
    public float maxSpeed;
    public float steamCost;

    public Player(World world) {
        this.animation = new Animation(new TextureRegion(new Texture(Gdx.files.internal("ship_animations.png")), 64, 32), 2, 0.5f);
        this.sprite = new Sprite(animation.getFrame());
        this.sprite.setBounds(Gdx.graphics.getWidth()/2 - sprite.getWidth()/2 ,
                Gdx.graphics.getHeight()/2, 50 , 50);
        this.sprite.setOriginCenter();

        BodyDef def = new BodyDef();

        def.type = BodyDef.BodyType.DynamicBody;

        def.position.set(sprite.getX() / PPM, sprite.getY() / PPM);
        body = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(((sprite.getWidth() / 2f) - 8) / PPM, ((sprite.getHeight() / 2f) - 8) / PPM);
        body.createFixture(shape, 1.0f);
        shape.dispose();
        body.setLinearDamping(0.93f);
        body.setAngularDamping(0.93f);

        this.steam = 20f;
        this.accelSpeed = 10f;
        this.turnSpeed = 2f;
        this.maxSpeed = 2.5f;
        this.steamCost = 0.1f;

    }

    public void update(float delta, Camera cam){
        animation.update(delta);
        this.sprite.setRotation((float) Math.toDegrees(body.getAngle()));
        this.sprite.setPosition( body.getPosition().x * PPM - sprite.getWidth()/2, body.getPosition().y * PPM - sprite.getHeight()/2);

        if(steam < 0) steam = 0;
        if(steam < accelSpeed) {
            accelSpeed = maxSpeed * (steam / accelSpeed);
        }
//        steam > 10 -> max speed
//        steam = 9 -> max speed * 9 / 10

    }

    public void render(SpriteBatch sb, Camera cam) {
        TextureRegion r = animation.getFrame();
        sprite.setRegion(r);
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
