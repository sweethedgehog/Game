package com.mygdx.game.objects;

import static com.mygdx.game.GameSettings.PADDING_HORIZONTAL;
import static com.mygdx.game.GameSettings.SCREEN_HEIGHT;
import static com.mygdx.game.GameSettings.SCREEN_WIDTH;
import static com.mygdx.game.GameSettings.SHIELD_BONUS_BIT;
import static com.mygdx.game.GameSettings.TRASH_VELOCITY;
import static com.mygdx.game.GameSettings.ULTRA_KILL_BONUS_BIT;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

public class UltraKillBonusObject extends GameObject{
    public boolean wasUsed;
    public UltraKillBonusObject(int width, int height, String texturePath, World world){
        super(
                texturePath,
                width / 2 + PADDING_HORIZONTAL + (new Random()).nextInt((SCREEN_WIDTH - 2 * PADDING_HORIZONTAL - width)),
                SCREEN_HEIGHT + height / 2,
                width, height,
                ULTRA_KILL_BONUS_BIT,
                world
        );
        wasUsed = false;
        body.setType(BodyDef.BodyType.KinematicBody);
        body.setLinearVelocity(0, -TRASH_VELOCITY);
    }
    public boolean isInFrame() {
        return !((getY() - height / 2 > SCREEN_HEIGHT) ||
                (getY() + height / 2 < 0) ||
                (getX() - width / 2 > SCREEN_WIDTH) ||
                (getX() + width / 2 < 0));
    }
    @Override
    public void hit(int typeBonus){
        wasUsed = true;
    }
}
