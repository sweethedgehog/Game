package com.mygdx.game.objects;

import static com.mygdx.game.GameSettings.BULLET_BIT;
import static com.mygdx.game.GameSettings.BULLET_VELOCITY;
import static com.mygdx.game.GameSettings.SCREEN_HEIGHT;
import static com.mygdx.game.GameSettings.SCREEN_WIDTH;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class BulletObject extends GameObject{
    public boolean wasHit;
    public BulletObject(int x, int y, int width, int height, String texturePath, World world) {
        super(texturePath, x, y, width, height, BULLET_BIT, world);
        body.setLinearVelocity(new Vector2(0, BULLET_VELOCITY));
        body.setBullet(true);
        wasHit = false;
    }
    public boolean hasToBeDestroyed(){
        return (getY() - height > SCREEN_HEIGHT) ||
                (getY() + height / 2 < 0) ||
                (getX() - width / 2 > SCREEN_WIDTH) ||
                (getX() + width / 2 < 0) || wasHit;
    }
    @Override
    public void hit(){
        wasHit = true;
    }
}
