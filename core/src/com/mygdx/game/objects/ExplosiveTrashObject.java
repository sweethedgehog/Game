package com.mygdx.game.objects;

import static com.mygdx.game.GameSettings.EXPLOSIVE_TRASH_MAX_LIVES;
import static com.mygdx.game.GameSettings.EXPLOSIVE_TRASH_ROTATE_VELOCITY;
import static com.mygdx.game.GameSettings.PADDING_HORIZONTAL;
import static com.mygdx.game.GameSettings.SCREEN_HEIGHT;
import static com.mygdx.game.GameSettings.SCREEN_WIDTH;
import static com.mygdx.game.GameSettings.TRASH_BIT;
import static com.mygdx.game.GameSettings.TRASH_VELOCITY;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.GameSettings;

import java.util.Random;

public class ExplosiveTrashObject extends GameObject{
    int lives;
    private int rotateDirection;
    public ExplosiveTrashObject(int width, int height, String texturePath, World world){
        super(
                texturePath,
                width / 2 + PADDING_HORIZONTAL + (new Random()).nextInt((SCREEN_WIDTH - 2 * PADDING_HORIZONTAL - width)),
                SCREEN_HEIGHT + height / 2,
                width, height,
                TRASH_BIT,
                world
            );
        body.setLinearVelocity(new Vector2(0, -TRASH_VELOCITY));
        rotateDirection = new Random().nextInt(2) * 2 - 1;
        lives = EXPLOSIVE_TRASH_MAX_LIVES;
    }
    public boolean isInFrame() {
        return !((getY() - height > SCREEN_HEIGHT) ||
                (getY() + height / 2 < 0) ||
                (getX() - width / 2 > SCREEN_WIDTH) ||
                (getX() + width / 2 < 0));
    }
    @Override
    public void hit(int typeBonus){
        lives--;
    }
    public void rotateConst(){
        super.rotate(EXPLOSIVE_TRASH_ROTATE_VELOCITY * rotateDirection);
    }
    public boolean isAlive(){
        return lives > 0;
    }
}
