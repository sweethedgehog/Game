package com.mygdx.game.screens;

import static com.mygdx.game.GameResources.BACKGROUND_IMG_PATH;
import static com.mygdx.game.GameResources.BUTTON_SHORT_BG_IMG_PATH;
import static com.mygdx.game.GameResources.SHIP_IMG_PATH;
import static com.mygdx.game.GameSettings.SCREEN_HEIGHT;
import static com.mygdx.game.GameSettings.SCREEN_WIDTH;
import static com.mygdx.game.GameSettings.SHIP_HEIGHT;
import static com.mygdx.game.GameSettings.SHIP_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.components.ButtonView;
import com.mygdx.game.components.MovingBackGroundView;
import com.mygdx.game.components.TextView;
import com.mygdx.game.managers.MemoryManager;
import com.mygdx.game.objects.BulletObject;
import com.mygdx.game.objects.ShipObject;

import java.awt.SystemTray;
import java.util.ArrayList;

public class WeaponSelectionScreen extends ScreenAdapter {
    MyGdxGame myGdxGame;
    MovingBackGroundView backgroundView;
    ArrayList<BulletObject> bulletArray;
    ButtonView weapon1;
    ButtonView weapon2;
    ButtonView weapon3;
    TextView note;
    ShipObject preview1;
    ShipObject preview3;
    ShipObject preview2;
    private static final int buttonPosY = SCREEN_HEIGHT / 8 - 35;
    public WeaponSelectionScreen(MyGdxGame myGdxGame){
        this.myGdxGame = myGdxGame;
        backgroundView = new MovingBackGroundView(BACKGROUND_IMG_PATH);
        note = new TextView(myGdxGame.largeWhiteFont, SCREEN_WIDTH / 2 - 185, SCREEN_HEIGHT - 150, "Select weapon!");
        bulletArray = new ArrayList<>();
        weapon1 = new ButtonView(
                SCREEN_WIDTH / 4 - 80, buttonPosY,
                160, 70,
                myGdxGame.commonBlackFont,
                BUTTON_SHORT_BG_IMG_PATH,
                "weapon1"
        );
        weapon2 = new ButtonView(
                SCREEN_WIDTH / 2 - 80, buttonPosY,
                160, 70,
                myGdxGame.commonBlackFont,
                BUTTON_SHORT_BG_IMG_PATH,
                "weapon2"
        );
        weapon3 = new ButtonView(
                SCREEN_WIDTH / 4 * 3 - 80, buttonPosY,
                160, 70,
                myGdxGame.commonBlackFont,
                BUTTON_SHORT_BG_IMG_PATH,
                "weapon3"
        );

    }
    @Override
    public void show(){
        preview1 = new ShipObject(SCREEN_WIDTH / 4, SCREEN_HEIGHT / 2, SHIP_WIDTH, SHIP_HEIGHT, 1, SHIP_IMG_PATH, myGdxGame.world, true);
        preview2 = new ShipObject(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, SHIP_WIDTH, SHIP_HEIGHT, 2, SHIP_IMG_PATH, myGdxGame.world, true);
        preview3 = new ShipObject(SCREEN_WIDTH / 4 * 3, SCREEN_HEIGHT / 2, SHIP_WIDTH, SHIP_HEIGHT, 3, SHIP_IMG_PATH, myGdxGame.world, true);
    }
    @Override
    public void render(float delta){
        handleInput();

        myGdxGame.worldStep();

        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        if (preview1.needToShoot())
            bulletArray = preview1.shoot(bulletArray, myGdxGame.world);
        if (preview2.needToShoot())
            bulletArray = preview2.shoot(bulletArray, myGdxGame.world);
        if (preview3.needToShoot())
            bulletArray = preview3.shoot(bulletArray, myGdxGame.world);

        myGdxGame.batch.begin();
        backgroundView.draw(myGdxGame.batch);
        preview1.draw(myGdxGame.batch);
        preview2.draw(myGdxGame.batch);
        preview3.draw(myGdxGame.batch);
        for (BulletObject bullet : bulletArray)
            bullet.draw(myGdxGame.batch);
        weapon1.draw(myGdxGame.batch);
        weapon2.draw(myGdxGame.batch);
        weapon3.draw(myGdxGame.batch);
        note.draw(myGdxGame.batch);
        myGdxGame.batch.end();

        bulletUpdate();
    }
    public void startGame(){
        myGdxGame.world.destroyBody(preview1.body);
        myGdxGame.world.destroyBody(preview2.body);
        myGdxGame.world.destroyBody(preview3.body);
    }
    private void handleInput(){
        if (Gdx.input.justTouched()) {
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if (weapon1.isHit(myGdxGame.touch.x, myGdxGame.touch.y)){
                myGdxGame.weapon = 1;
                startGame();
                myGdxGame.setScreen(myGdxGame.gameScreen);
            }
            if (weapon2.isHit(myGdxGame.touch.x, myGdxGame.touch.y)){
                myGdxGame.weapon = 2;
                startGame();
                myGdxGame.setScreen(myGdxGame.gameScreen);
            }
            if (weapon3.isHit(myGdxGame.touch.x, myGdxGame.touch.y)){
                myGdxGame.weapon = 3;
                startGame();
                myGdxGame.setScreen(myGdxGame.gameScreen);
            }

        }
    }
    private void bulletUpdate(){
        for (int i = 0; i < bulletArray.size(); i++){
            if (bulletArray.get(i).hasToBeDestroyed()){
                myGdxGame.world.destroyBody(bulletArray.get(i).body);
                bulletArray.remove(i--);
            }
        }
    }
}
