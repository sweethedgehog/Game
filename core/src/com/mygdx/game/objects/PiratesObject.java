package com.mygdx.game.objects;

import static com.mygdx.game.GameSettings.PADDING_HORIZONTAL;
import static com.mygdx.game.GameSettings.PIRATES_BIT;
import static com.mygdx.game.GameSettings.PIRATES_POS_Y_TO_ROTATE;
import static com.mygdx.game.GameSettings.PIRATES_VELOCITY;
import static com.mygdx.game.GameSettings.SCREEN_HEIGHT;
import static com.mygdx.game.GameSettings.SCREEN_WIDTH;
import static com.mygdx.game.GameSettings.SHIP_BIT;
import static com.mygdx.game.GameSettings.TRASH_BIT;
import static com.mygdx.game.GameSettings.TRASH_VELOCITY;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

public class PiratesObject extends GameObject{
    int lives;
    private boolean wasRotated;
    public PiratesObject (int width, int height, String texturePath, World world){
        super(
                texturePath,
                width / 2 + PADDING_HORIZONTAL + (new Random()).nextInt((SCREEN_WIDTH - 2 * PADDING_HORIZONTAL - width)),
                SCREEN_HEIGHT + height / 2,
                width, height,
                PIRATES_BIT,
                world
        );

        lives = 1;
        body.setLinearVelocity(new Vector2(0, -PIRATES_VELOCITY));
        wasRotated = false;
    }
    @Override
    public void hit(int typeBonus){
        lives--;
    }

    public boolean isAlive(){
        return lives > 0;
    }

    public boolean needToRotate() { return (getY() + height / 2 <= PIRATES_POS_Y_TO_ROTATE) && !wasRotated; }

    public void rotateToPos(Vector2 pos){
        wasRotated = true;
        Vector2 moveVector = normalizeVector(new Vector2(pos.x - getX(), pos.y - getY()));
        body.setLinearVelocity(moveVector.x * PIRATES_VELOCITY, moveVector.y * PIRATES_VELOCITY);
        setRotation((int) - Math.toDegrees(Math.acos(moveVector.x)) + 90);
    }

    public boolean isInFrame() {
        return !((getY() - height / 2 > SCREEN_HEIGHT) ||
                (getY() + height / 2 < 0) ||
                (getX() - width / 2 > SCREEN_WIDTH) ||
                (getX() + width / 2 < 0));
    }

    private Vector2 normalizeVector(Vector2 vector){
        float length = (float) Math.sqrt(vector.x * vector.x + vector.y * vector.y);
        return new Vector2(vector.x / length, vector.y / length);
    }
}
