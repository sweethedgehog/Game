package com.mygdx.game.components;

import static com.mygdx.game.GameResources.LIVE_IMG_PATH;
import static com.mygdx.game.GameSettings.LIVE_PADDING;
import static com.mygdx.game.GameSettings.SHIP_LIVES;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.TextArea;
import java.awt.TexturePaint;

public class LiveView extends View{
    Texture texture;
    int leftLives;
    public LiveView(float x, float y){
        super(x, y);
        texture = new Texture(LIVE_IMG_PATH);
        this.width = texture.getWidth();
        this.height = texture.getHeight();
        leftLives = SHIP_LIVES;
    }
    public void setLeftLives(int leftLives) {
        this.leftLives = leftLives;
    }
    @Override
    public void draw(SpriteBatch batch) {
        if (leftLives > 0) batch.draw(texture, x + (texture.getWidth() + LIVE_PADDING), y, width, height);
        if (leftLives > 1) batch.draw(texture, x, y, width, height);
        if (leftLives > 2) batch.draw(texture, x + 2 * (texture.getWidth() + LIVE_PADDING), y, width, height);
    }
    @Override
    public void dispose() {
        texture.dispose();
    }
}
