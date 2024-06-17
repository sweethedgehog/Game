package com.mygdx.game.objects;

import static com.mygdx.game.GameSettings.BULLET_BIT;
import static com.mygdx.game.GameSettings.SCREEN_HEIGHT;
import static com.mygdx.game.GameSettings.SCREEN_WIDTH;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class BulletObject extends GameObject{
    private Vector2 startPos;
    private int range;
    public boolean wasHit;
    public BulletObject(int x, int y, int width, int height, String texturePath, int velocity, int rotate, int range, World world) {
        super(texturePath, x, y, width, height, BULLET_BIT, world);
        body.setLinearVelocity(new Vector2(
                (float) Math.cos(Math.toRadians(rotate)) * velocity,
                (float) Math.sin(Math.toRadians(rotate)) * velocity
        ));
        body.setBullet(true);
        startPos = new Vector2(x, y);
        this.range = range;
        setRotation(rotate - 90);
        wasHit = false;
        body.setType(BodyDef.BodyType.KinematicBody);
    }
    public BulletObject(int x, int y, int width, int height, String texturePath, int velocity, int rotate, World world) {
        super(texturePath, x, y, width, height, BULLET_BIT, world);
        body.setLinearVelocity(new Vector2(
                (float) Math.cos(Math.toRadians(rotate)) * velocity,
                (float) Math.sin(Math.toRadians(rotate)) * velocity
        ));
        body.setBullet(true);
        startPos = Vector2.Zero;
        setRotation(rotate - 90);
        wasHit = false;
        body.setType(BodyDef.BodyType.KinematicBody);
    }
    public double getRotateInRad(){
        return Math.toRadians(sprite.getRotation());
    }
    public boolean hasToBeDestroyed(){
        return (getY() - height > SCREEN_HEIGHT) ||
                (getY() + height / 2 < 0) ||
                (getX() - width / 2 > SCREEN_WIDTH) ||
                (getX() + width / 2 < 0) ||
                (Math.pow(startPos.x - getX(), 2)
                        + Math.pow(startPos.y - getY(), 2) >= range * range && range != 0) ||
                wasHit;
    }
    @Override
    public void hit(){
        wasHit = true;
    }
}
