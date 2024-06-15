package com.mygdx.game.objects;

import static com.mygdx.game.GameSettings.SCREEN_HEIGHT;
import static com.mygdx.game.GameSettings.SCREEN_WIDTH;
import static com.mygdx.game.GameSettings.SHIP_BIT;
import static com.mygdx.game.GameSettings.SHIP_FORCE_RATIO;
import static com.mygdx.game.GameSettings.SHIP_LIVES;
import static com.mygdx.game.GameSettings.SHOOTING_COOL_DOWN;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;

public class ShipObject extends GameObject{
    long lastShotTime;
    int lives;

    public ShipObject(int x, int y, int width, int height, String texturePath, World world) {
        super(texturePath, x, y, width, height, SHIP_BIT, world);
        body.setLinearDamping(10);
        lives = SHIP_LIVES;
    }
    private void putInFrame(){
        if((float) getY() > (SCREEN_HEIGHT / 2f - height / 2f)) {
            setY(SCREEN_HEIGHT / 2 - height / 2);
            body.setLinearVelocity(body.getLinearVelocity().x, 0);
        }
        else if(getY() < height / 2f){
            setY(height / 2);
            body.setLinearVelocity(body.getLinearVelocity().x, 0);
        }

        if (getX() < (-width / 2f))
            setX(SCREEN_WIDTH + width / 2);
        else if (getX() > (SCREEN_WIDTH + width / 2f))
            setX(-width / 2);
    }
    public void move(Vector3 direction){
        float xDirection = (direction.x - getX()) * SHIP_FORCE_RATIO;
        float yDirection = (direction.y - getY()) * SHIP_FORCE_RATIO;
        body.applyForceToCenter(new Vector2(xDirection, yDirection), true);
    }
    @Override
    public void draw(SpriteBatch batch){
        putInFrame();
        super.draw(batch);
    }
    @Override
    public void hit(){
        lives--;
    }
    public boolean isAlive(){
        return lives > 0;
    }
    public int getLiveLeft(){
        return lives;
    }
    public boolean needToShoot(){
        if (TimeUtils.millis() - lastShotTime >= SHOOTING_COOL_DOWN){
            lastShotTime = TimeUtils.millis();
            return true;
        }
        return false;
    }
}
