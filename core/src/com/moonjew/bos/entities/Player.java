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
import com.moonjew.bos.Animation;

import static com.moonjew.bos.screens.GameScreen.PPM;

public class Player {
    private Animation animation;
    private Sprite sprite;
    private Body body;

    public Player(World world) {
        this.animation = new Animation(new TextureRegion(new Texture(Gdx.files.internal("ship_animations.png")), 64, 32), 2, 0.5f);
        this.sprite = new Sprite(animation.getFrame());
        this.sprite.setBounds(0,0, 50, 50);
        this.sprite.setOriginCenter();
        this.body = createBox(world, 0,0,32,32, false);
    }

    public void update(float delta, Camera cam){
        animation.update(delta);
        this.sprite.setRotation(body.getAngle());
        this.sprite.setPosition(cam.position.x + body.getPosition().x, cam.position.y + body.getPosition().y);
    }

    public Body createBox(World world, int x, int y, int width, int height, boolean isStatic){
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

    public void render(SpriteBatch sb, Camera cam) {
        TextureRegion r = animation.getFrame();
        sprite.setRegion(r);
        sprite.draw(sb);
    }

    public Body getBody() {
        return body;
    }
}
