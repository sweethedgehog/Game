package com.mygdx.game.objects;

import static com.mygdx.game.GameSettings.PADDING_HORIZONTAL;
import static com.mygdx.game.GameSettings.SCREEN_HEIGHT;
import static com.mygdx.game.GameSettings.SCREEN_WIDTH;
import static com.mygdx.game.GameSettings.TRASH_BIT;
import static com.mygdx.game.GameSettings.TRASH_MAX_LIVES;
import static com.mygdx.game.GameSettings.TRASH_VELOCITY;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.GameSettings;

import java.util.Random;

public class TrashObject extends GameObject {
    int lives;
    public TrashObject(int width, int height, String texturePath, World world) {
        super(
                texturePath,
                width / 2 + PADDING_HORIZONTAL + (new Random()).nextInt((SCREEN_WIDTH - 2 * PADDING_HORIZONTAL - width)),
                SCREEN_HEIGHT + height / 2,
                width, height,
                TRASH_BIT,
                world
            );

        lives = TRASH_MAX_LIVES;
        body.setLinearVelocity(new Vector2(0, -TRASH_VELOCITY));
    }

    public boolean isInFrame() {
        return !((getY() - height / 2 > SCREEN_HEIGHT) ||
                (getY() + height / 2 < 0) ||
                (getX() - width / 2 > SCREEN_WIDTH) ||
                (getX() + width / 2 < 0));
    }
    @Override
    public void hit(int typeBonus){
        lives--;
    }
    public boolean isAlive(){
        return lives > 0;
    }

}

