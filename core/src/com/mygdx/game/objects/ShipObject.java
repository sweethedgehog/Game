package com.mygdx.game.objects;

import static com.mygdx.game.GameResources.BULLET_IMG_PATH;
import static com.mygdx.game.GameResources.SHIELD_IMG_PATH;
import static com.mygdx.game.GameSettings.BARRIER_BULLET_VELOCITY;
import static com.mygdx.game.GameSettings.BULLET_HEIGHT;
import static com.mygdx.game.GameSettings.BULLET_VELOCITY;
import static com.mygdx.game.GameSettings.BULLET_WIDTH;
import static com.mygdx.game.GameSettings.HEAL_BONUS;
import static com.mygdx.game.GameSettings.SCREEN_HEIGHT;
import static com.mygdx.game.GameSettings.SCREEN_WIDTH;
import static com.mygdx.game.GameSettings.SHIELD_BONUS;
import static com.mygdx.game.GameSettings.SHIELD_DURATION;
import static com.mygdx.game.GameSettings.SHIP_BIT;
import static com.mygdx.game.GameSettings.SHIP_FORCE_RATIO;
import static com.mygdx.game.GameSettings.SHIP_LIVES;
import static com.mygdx.game.GameSettings.SHOOTING_COOL_DOWN;
import static com.mygdx.game.GameSettings.SHOTGUN_BULLET_HEIGHT;
import static com.mygdx.game.GameSettings.SHOTGUN_BULLET_VELOCITY;
import static com.mygdx.game.GameSettings.SHOTGUN_BULLET_WIDTH;
import static com.mygdx.game.GameSettings.ULTRA_KILL_BONUS;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

public class ShipObject extends GameObject{
    Texture shieldTexture;
    long lastShotTime;
    int lives;
    long lastShieldOn;
    private int weapon;
    public ShipObject(int x, int y, int width, int height, int weapon, String texturePath, World world) {
        super(texturePath, x, y, width, height, SHIP_BIT, world);
        body.setLinearDamping(10);
        lives = SHIP_LIVES;
        shieldTexture = new Texture(SHIELD_IMG_PATH);
        this.weapon = weapon;
    }
    public ShipObject(int x, int y, int width, int height, int weapon, String texturePath, World world, boolean isKinematic) {
        super(texturePath, x, y, width, height, SHIP_BIT, world);
        this.weapon = weapon;
        lives = SHIP_LIVES;
        if (isKinematic)
            body.setType(BodyDef.BodyType.KinematicBody);
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
        if (shieldTexture != null && TimeUtils.millis() - lastShieldOn < SHIELD_DURATION)
            batch.draw(shieldTexture, getX() - (width * 0.75f), getY() - (height * 0.75f), width * 1.5f, height * 1.5f);
    }
    @Override
    public void hit(int typeBonus){
        switch (typeBonus){
            default:
                if (!(TimeUtils.millis() - lastShieldOn < SHIELD_DURATION))
                    lives--;
                break;
            case HEAL_BONUS:
                lives++;
                if (lives > SHIP_LIVES)
                    lives = SHIP_LIVES;
                break;
            case SHIELD_BONUS:
                lastShieldOn = TimeUtils.millis();
                break;
            case ULTRA_KILL_BONUS:
                break;
        }
    }
    public boolean isAlive(){
        return lives > 0;
    }
    public int getLiveLeft(){
        return lives;
    }
    public boolean needToShoot(){
        if (TimeUtils.millis() - lastShotTime >= SHOOTING_COOL_DOWN[weapon - 1]){
            lastShotTime = TimeUtils.millis();
            return true;
        }
        return false;
    }
    public ArrayList<BulletObject> shoot(ArrayList<BulletObject> bulletArray, World world){
        BulletObject bulletObject;
        int count;
        switch (weapon){
            case 1:
                bulletObject = new BulletObject(getX(), getY() + height / 2, BULLET_WIDTH, BULLET_HEIGHT, BULLET_IMG_PATH, BULLET_VELOCITY, 90, world);
                bulletArray.add(bulletObject);
                break;
            case 2:
                count = 5;
                for (int i = 0; i < count; i++){
                    bulletObject = new BulletObject(getX(),
                            getY() + height / 2 + SHOTGUN_BULLET_HEIGHT / 2,
                            SHOTGUN_BULLET_WIDTH, SHOTGUN_BULLET_HEIGHT, BULLET_IMG_PATH, SHOTGUN_BULLET_VELOCITY,
                            180 / (count + 1) * (i + 1), 50, world
                    );
                    bulletArray.add(bulletObject);
                }
                break;
            case 3:
                count = 32;
                for (int i = 0; i < count; i++){
                    int angle = 360 / count * i;
                    float nowBulletWidth = (float) (Math.max(SHOTGUN_BULLET_WIDTH, SHOTGUN_BULLET_HEIGHT) * Math.cos(Math.toRadians(angle)));
                    float nowBulletHeight = (float) (Math.max(SHOTGUN_BULLET_WIDTH, SHOTGUN_BULLET_HEIGHT) * Math.sin(Math.toRadians(angle)));
                    bulletObject = new BulletObject((int) (getX() + (width / 2 + 20) * Math.cos(Math.toRadians(angle)) + nowBulletWidth / 2),
                            (int) (getY() + (height / 2 + 10) * Math.sin(Math.toRadians(angle)) + nowBulletHeight / 2),
                            SHOTGUN_BULLET_WIDTH, SHOTGUN_BULLET_HEIGHT, BULLET_IMG_PATH, BARRIER_BULLET_VELOCITY,
                            angle, world
                    );
                    bulletArray.add(bulletObject);
                }
                break;
        }
        return bulletArray;
    }
}
