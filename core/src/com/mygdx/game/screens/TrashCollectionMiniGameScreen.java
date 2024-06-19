package com.mygdx.game.screens;

import static com.mygdx.game.GameResources.BACKGROUND_IMG_PATH;
import static com.mygdx.game.GameResources.BLACKOUT_FULL_IMG_PATH;
import static com.mygdx.game.GameResources.BLACKOUT_TOP_IMG_PATH;
import static com.mygdx.game.GameResources.PAUSE_IMG_PATH;
import static com.mygdx.game.GameResources.SHIP_IMG_PATH;
import static com.mygdx.game.GameResources.TRASH_IMG_PATH;
import static com.mygdx.game.GameSettings.COUNT_OF_TRASH_TO_DESTRUCT_IN_TRASH_COLLECTOR;
import static com.mygdx.game.GameSettings.SHIP_HEIGHT;
import static com.mygdx.game.GameSettings.SHIP_WIDTH;
import static com.mygdx.game.GameSettings.TRASH_HEIGHT;
import static com.mygdx.game.GameSettings.TRASH_WIDTH;
import static com.mygdx.game.GameState.PAUSED;
import static com.mygdx.game.GameState.PLAYING;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.GameResources;
import com.mygdx.game.GameSession;
import com.mygdx.game.GameSettings;
import com.mygdx.game.GameState;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.components.ButtonView;
import com.mygdx.game.components.ImageView;
import com.mygdx.game.components.LiveView;
import com.mygdx.game.components.MovingBackGroundView;
import com.mygdx.game.components.TextView;
import com.mygdx.game.managers.ContactManager;
import com.mygdx.game.objects.BulletObject;
import com.mygdx.game.objects.GameObject;
import com.mygdx.game.objects.ShipObject;
import com.mygdx.game.objects.TrashObject;

import java.util.ArrayList;
import java.util.Iterator;

public class TrashCollectionMiniGameScreen extends ScreenAdapter {
    MyGdxGame myGdxGame;
    ShipObject shipObject;
    GameSession gameSession;

    ArrayList<TrashObject> trashArray;
    ArrayList<BulletObject> bulletArray;

    ContactManager contactManager;

    MovingBackGroundView movingBackGroundView;
    ImageView topBlackoutView;
    ImageView fullBlackoutView;
    LiveView liveView;
    TextView pauseTextView;
    TextView trashDestructedView;
    TextView endGameTextView;
    ButtonView homeButton;
    ButtonView continueButton;
    ButtonView pauseButton;
    ButtonView homeButton2;

    public TrashCollectionMiniGameScreen (MyGdxGame myGdxGame){
        this.myGdxGame = myGdxGame;

        gameSession = new GameSession();
        contactManager = new ContactManager(myGdxGame.world);

        movingBackGroundView = new MovingBackGroundView(BACKGROUND_IMG_PATH);

        topBlackoutView = new ImageView(0, 1180, BLACKOUT_TOP_IMG_PATH);
        fullBlackoutView = new ImageView(0, 0, BLACKOUT_FULL_IMG_PATH);

        pauseTextView = new TextView(myGdxGame.largeWhiteFont, 282, 842, "Pause");
        endGameTextView = new TextView(myGdxGame.largeWhiteFont, 255, 842);
        trashDestructedView = new TextView(myGdxGame.commonWhiteFont, 50, 1215);

        liveView = new LiveView(305, 1215);

        homeButton2 = new ButtonView(
                280, 365,
                160, 70,
                myGdxGame.commonBlackFont,
                GameResources.BUTTON_SHORT_BG_IMG_PATH,
                "Home"
        );
        pauseButton = new ButtonView(605, 1200,
                46, 54,
                PAUSE_IMG_PATH
        );
        homeButton = new ButtonView(
                138, 695,
                200, 70,
                myGdxGame.commonBlackFont,
                GameResources.BUTTON_SHORT_BG_IMG_PATH,
                "Home"
        );
        continueButton = new ButtonView(
                393, 695,
                200, 70,
                myGdxGame.commonBlackFont,
                GameResources.BUTTON_SHORT_BG_IMG_PATH,
                "Continue"
        );

        trashArray = new ArrayList<>();
        bulletArray = new ArrayList<>();
    }
    @Override
    public void show(){
        restartGame();
    }
    @Override
    public void render(float delta){
        handleInput();

        if (gameSession.state == PLAYING) {

            myGdxGame.worldStep();

            if (COUNT_OF_TRASH_TO_DESTRUCT_IN_TRASH_COLLECTOR - gameSession.getDestructedTrashNumber() <= 0) {
                endGameTextView.setText("You win!");
                gameSession.endGame();
            }
            trashDestructedView.setText((COUNT_OF_TRASH_TO_DESTRUCT_IN_TRASH_COLLECTOR - gameSession.getDestructedTrashNumber()) + " trash left");
            liveView.setLeftLives(shipObject.getLiveLeft());
            if (gameSession.shouldSpawnTrash()){
                TrashObject trashObject = new TrashObject(
                        TRASH_WIDTH, TRASH_HEIGHT,
                        TRASH_IMG_PATH,
                        myGdxGame.world
                );
                trashArray.add(trashObject);
            }
            if (shipObject.needToShoot()){
                bulletArray = shipObject.shoot(bulletArray, myGdxGame.world);
                if (myGdxGame.audioManager.isSoundOn) myGdxGame.audioManager.shootSound.play();
            }
            trashUpdate();
            bulletUpdate();
            if (!shipObject.isAlive()) {
                endGameTextView.setText("You lose!");
                gameSession.endGame();
            }
            movingBackGroundView.move();
        }
        draw();
    }
    private void restartGame(){
        clearObjectArray(trashArray);
        clearObjectArray(bulletArray);

        if (shipObject != null)
            myGdxGame.world.destroyBody(shipObject.body);

        shipObject = new ShipObject(
                GameSettings.SCREEN_WIDTH / 2, 150,
                SHIP_WIDTH, SHIP_HEIGHT,
                myGdxGame.weapon,
                SHIP_IMG_PATH,
                myGdxGame.world
        );

        gameSession.startGame();
    }
    private void handleInput() {
        if (Gdx.input.isTouched()) {
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            switch (gameSession.state) {
                case PLAYING:
                    if (pauseButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        gameSession.pauseGame();
                        break;
                    }
                    shipObject.move(myGdxGame.touch);
                    break;
                case PAUSED:
                    if (continueButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y))
                        gameSession.resumeGame();
                    if (homeButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y))
                        myGdxGame.setScreen(myGdxGame.menuScreen);
                    break;
                case ENDED:
                    if (homeButton2.isHit(myGdxGame.touch.x, myGdxGame.touch.y))
                        myGdxGame.setScreen(myGdxGame.menuScreen);
                    break;
            }
        }
    }
    private void trashUpdate() {
        Iterator<TrashObject> iterator = trashArray.iterator();
        while (iterator.hasNext()) {
            TrashObject trash = iterator.next();
            boolean hasToBeDestroyed = !trash.isAlive() || !trash.isInFrame();

            if (!trash.isAlive()) {
                gameSession.destructionRegistration();
                if (myGdxGame.audioManager.isSoundOn)
                    myGdxGame.audioManager.explosionSound.play(0.2f);
            }

            if (hasToBeDestroyed) {
                myGdxGame.world.destroyBody(trash.body);
                iterator.remove();
            }
        }
    }

    private void bulletUpdate() {
        Iterator<BulletObject> iterator = bulletArray.iterator();
        while (iterator.hasNext()) {
            BulletObject bullet = iterator.next();
            if (bullet.hasToBeDestroyed()) {
                myGdxGame.world.destroyBody(bullet.body);
                iterator.remove();
            }
        }
    }
    private void draw(){
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();
        movingBackGroundView.draw(myGdxGame.batch);
        shipObject.draw(myGdxGame.batch);
        for (TrashObject trash: trashArray)
            trash.draw(myGdxGame.batch);
        for (BulletObject bullet : bulletArray)
            bullet.draw(myGdxGame.batch);
        topBlackoutView.draw(myGdxGame.batch);
        trashDestructedView.draw(myGdxGame.batch);
        liveView.draw(myGdxGame.batch);
        pauseButton.draw(myGdxGame.batch);
        if (gameSession.state == PAUSED) {
            fullBlackoutView.draw(myGdxGame.batch);
            pauseTextView.draw(myGdxGame.batch);
            homeButton.draw(myGdxGame.batch);
            continueButton.draw(myGdxGame.batch);
        }
        else if (gameSession.state == GameState.ENDED) {
            fullBlackoutView.draw(myGdxGame.batch);
            endGameTextView.draw(myGdxGame.batch);
            homeButton2.draw(myGdxGame.batch);
        }
        myGdxGame.batch.end();
    }
    private void clearObjectArray(ArrayList<? extends GameObject> arrayList) {
        Iterator<? extends GameObject> iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            GameObject element = iterator.next();
            myGdxGame.world.destroyBody(element.body);
            iterator.remove();
        }
    }
}
