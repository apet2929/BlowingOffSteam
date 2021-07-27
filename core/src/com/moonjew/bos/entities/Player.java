package com.moonjew.bos.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.moonjew.bos.BlowingOffSteam;

public class Player {
    private Sprite sprite;
    private Vector2 velocity;

    public Player(){
        this.sprite = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("ship_animations.png")), 0, 0, 32, 32));
        this.sprite.setBounds(0,0, 50, 50);
        this.sprite.setOriginCenter();
        this.velocity = new Vector2();
    }

    public void update(float delta){
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            this.sprite.rotate(delta * 50);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            this.sprite.rotate(delta * -50);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            this.velocity.rotateDeg(-sprite.getRotation());
            this.velocity.add(0, 15 * delta);

            this.velocity.rotateDeg(sprite.getRotation());
            this.velocity.clamp(-10, 10);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            this.velocity.x *= 0.95;
            this.velocity.y *= 0.95;
        }

        float x = sprite.getX() + velocity.x;
        float y = sprite.getY() + velocity.y;
        if(x < 0){
            x = BlowingOffSteam.WIDTH;
        } else if(x > BlowingOffSteam.WIDTH){
            x = 0;
        }
        if(y < 0){
            y = BlowingOffSteam.HEIGHT;
        } else if(y > BlowingOffSteam.HEIGHT){
            y = 0;
        }
        this.sprite.setPosition(x, y);
    }

    public void render(SpriteBatch sb, Camera cam) {
        sprite.draw(sb);
    }

}
