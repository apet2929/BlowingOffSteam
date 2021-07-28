package com.moonjew.bos.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.moonjew.bos.Animation;
import com.moonjew.bos.BlowingOffSteam;

public class Player {
    private Animation animation;
    private Sprite sprite;
    private Vector2 position;
    private Vector2 velocity;

    public Player() {
        this.animation = new Animation(new TextureRegion(new Texture(Gdx.files.internal("ship_animations.png")), 64, 32), 2, 0.5f);
        this.sprite = new Sprite(animation.getFrame());
        this.sprite.setBounds(0,0, 50, 50);
        this.sprite.setOriginCenter();
        this.velocity = new Vector2();
        this.position = new Vector2();
    }

    public void update(float delta, Camera cam){
        animation.update(delta);
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            this.sprite.rotate(delta * 75);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            this.sprite.rotate(delta * -75);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            this.velocity.rotateDeg(-sprite.getRotation());
            this.velocity.add(0,  delta);

            this.velocity.rotateDeg(sprite.getRotation());
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            this.velocity.x *= 0.93;
            this.velocity.y *= 0.93;
        }

        if(this.velocity.x > 2.5f){
            this.velocity.x = 2.5f;
        } else if(this.velocity.x < -2.5f){
            this.velocity.x = -2.5f;
        }
        if(this.velocity.y > 2.5f){
            this.velocity.y = 2.5f;
        } else if(this.velocity.y < -2.5f){
            this.velocity.y = -2.5f;
        }

        position.x += velocity.x;
        position.y += velocity.y;

        if(velocity.y > 0) {
            cam.translate(0, velocity.y, 0);
        }

        this.sprite.setPosition(cam.position.x + position.x, cam.position.y + position.y);
    }

    public void render(SpriteBatch sb, Camera cam) {
        TextureRegion r = animation.getFrame();
        sprite.setRegion(r);
        sprite.draw(sb);
    }

}
