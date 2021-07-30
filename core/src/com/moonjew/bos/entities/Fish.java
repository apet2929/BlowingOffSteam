package com.moonjew.bos.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.moonjew.bos.Animation;

import static com.moonjew.bos.screens.GameScreen.PPM;

public class Fish {
    private int direction;
    private float moveCounter;
    private Animation animation;
    private Sprite sprite;
    private Body body;

    public Fish(float x, float y, Texture texture, World world){
        this.animation = new Animation(new TextureRegion(texture), 4, 1.0f);
        this.sprite = new Sprite(animation.getFrame());
        moveCounter = 0;
        direction = 1;

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set((sprite.getX() / PPM) + x, (sprite.getY() / PPM) + y);
        body = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(((sprite.getWidth() / 2f) - 8) / PPM, ((sprite.getHeight() / 2f) - 8) / PPM);

        body.createFixture(shape, 0.5f);
        shape.dispose();

        body.setUserData("fish");

        body.setLinearVelocity(0.5f,0);
    }

    public void update(float delta){
        animation.update(delta);
        body.setLinearVelocity(0.5f * direction, 0);
        this.moveCounter+=delta;
        if(this.moveCounter > 5){
            this.direction *= -1;
            this.sprite.setBounds(sprite.getX(), sprite.getY(), sprite.getWidth() * -1, sprite.getHeight());
            this.sprite.setX(sprite.getX() - sprite.getWidth());
            this.moveCounter = 0;
        }

    }
    public void render(SpriteBatch sb){
        TextureRegion r = animation.getFrame();
        sprite.setRegion(r);
        sprite.setPosition( body.getPosition().x * PPM - sprite.getWidth()/2, body.getPosition().y * PPM - sprite.getHeight()/2);
        sprite.draw(sb);
    }



}
