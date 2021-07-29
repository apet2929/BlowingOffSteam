package com.moonjew.bos.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.moonjew.bos.Animation;

import static com.moonjew.bos.screens.GameScreen.PPM;

public class SteamVolcano {
    private Animation animation;
    public Sprite sprite;
    private Body body;

    public SteamVolcano(int x, int y, World world){
        this.animation = new Animation(new TextureRegion(new Texture(Gdx.files.internal("badlogic.jpg")), 32, 32), 2, 0.5f);
        this.sprite = new Sprite(animation.getFrame());

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((sprite.getX() / PPM) + x, (sprite.getY() / PPM) + y);
        body = world.createBody(bodyDef);
        body.setUserData("volcano");

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((sprite.getWidth() / 2f) / PPM, (sprite.getHeight() / 2f) / PPM);

        body.createFixture(shape, 1.0f).setSensor(true);

        FixtureDef sensor = new FixtureDef();
        sensor.isSensor = true;
        sensor.shape = shape;
        shape.dispose();

        this.sprite.setPosition(body.getPosition().x * PPM, body.getPosition().y + PPM);
    }

    public void update(float delta){
        animation.update(delta);
    }

    public void render(SpriteBatch sb){
        TextureRegion r = animation.getFrame();
        sprite.setRegion(r);
        sprite.draw(sb);
    }


}