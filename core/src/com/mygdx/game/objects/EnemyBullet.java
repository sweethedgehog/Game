package com.mygdx.game.objects;

import static com.mygdx.game.GameSettings.BULLET_BIT;
import static com.mygdx.game.GameSettings.BULLET_VELOCITY;
import static com.mygdx.game.GameSettings.ENEMY_BULLET_BIT;
import static com.mygdx.game.GameSettings.ENEMY_BULLET_ROTATE_VELOCITY;
import static com.mygdx.game.GameSettings.ENEMY_BULLET_VELOCITY;
import static com.mygdx.game.GameSettings.SCREEN_HEIGHT;
import static com.mygdx.game.GameSettings.SCREEN_WIDTH;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class EnemyBullet extends GameObject{
    public boolean wasHit;
    private boolean isRotating;
    private int rotate;
    public EnemyBullet(int x, int y, int width, int height, String texturePath, World world, int rotate, boolean isRotating){
        super(texturePath, x, y, width, height, ENEMY_BULLET_BIT, world);
        body.setLinearVelocity(new Vector2(
                (float) Math.cos(Math.toRadians(rotate)) * ENEMY_BULLET_VELOCITY,
                (float) Math.sin(Math.toRadians(rotate)) * ENEMY_BULLET_VELOCITY
                ));
        body.setBullet(true);
        setRotation(rotate - 90);
        wasHit = false;
        this.isRotating = isRotating;
        this.rotate = rotate;
    }
    public boolean hasToBeDestroyed(){

        return (getY() - height > SCREEN_HEIGHT) ||
                (getY() + height / 2 < 0) ||
                (getX() - width / 2 > SCREEN_WIDTH) ||
                (getX() + width / 2 < 0) ||
                wasHit;
    }
    @Override
    public void hit(int typeBonus){
        wasHit = true;
    }
    public void rotateConst(){
        if (isRotating && rotate != 0)
            rotate(ENEMY_BULLET_ROTATE_VELOCITY * rotate / Math.abs(rotate));
        else if (rotate == 0)
            rotate(ENEMY_BULLET_ROTATE_VELOCITY);
    }
}
