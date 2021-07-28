package com.moonjew.bos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Arrays;

public class Map {
    private static int TILE_SIZE = 32;
    public enum TileType {
        NONE,
        ROCK,
        CORAL,
        SEAWEED //slows u down
    }
    public TileType[][] tiles;

    private Texture noneTileTexture;

    public Map(){
        this.tiles = new TileType[32][1000];
        for (int i = 0; i < this.tiles.length; i++) {
            Arrays.fill(this.tiles[i], TileType.NONE);
        }
        noneTileTexture = new Texture(Gdx.files.internal("badlogic.jpg"));
    }

    public void render(SpriteBatch sb, Camera cam){
        float x = cam.position.x;
        float y = cam.position.y;

        for(int i = 0; i < tiles.length; i++){
            for(int j = 0; j < tiles[i].length; j++){
                switch (tiles[i][j]){
                    case NONE:
                        sb.draw(noneTileTexture, i * TILE_SIZE,  j * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                        break;
                }
            }
        }
    }


}
