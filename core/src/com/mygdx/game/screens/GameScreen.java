package com.mygdx.game.screens;

import static com.mygdx.game.GameResources.BACKGROUND_IMG_PATH;
import static com.mygdx.game.GameResources.BLACKOUT_FULL_IMG_PATH;
import static com.mygdx.game.GameResources.BLACKOUT_TOP_IMG_PATH;
import static com.mygdx.game.GameResources.LIVE_IMG_PATH;
import static com.mygdx.game.GameResources.PAUSE_IMG_PATH;
import static com.mygdx.game.GameResources.PIRATES_IMG_PATH;
import static com.mygdx.game.GameResources.SHIELD_IMG_PATH;
import static com.mygdx.game.GameResources.SHIP_IMG_PATH;
import static com.mygdx.game.GameResources.SKULL_IMG_PATH;
import static com.mygdx.game.GameResources.TRASH_IMG_PATH;
import static com.mygdx.game.GameResources.TRASH_SHARP_IMG_PATH;
import static com.mygdx.game.GameSettings.PIRATES_HEIGHT;
import static com.mygdx.game.GameSettings.PIRATES_WIDTH;
import static com.mygdx.game.GameSettings.SCREEN_HEIGHT;
import static com.mygdx.game.GameSettings.SCREEN_WIDTH;
import static com.mygdx.game.GameSettings.SHIP_HEIGHT;
import static com.mygdx.game.GameSettings.SHIP_WIDTH;
import static com.mygdx.game.GameSettings.TRASH_HEIGHT;
import static com.mygdx.game.GameSettings.TRASH_SHARP_HEIGHT;
import static com.mygdx.game.GameSettings.TRASH_SHARP_WIDTH;
import static com.mygdx.game.GameSettings.TRASH_WIDTH;
import static com.mygdx.game.GameState.PAUSED;
import static com.mygdx.game.GameState.PLAYING;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.components.RecordsListView;
import com.mygdx.game.managers.ContactManager;
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
import com.mygdx.game.managers.MemoryManager;
import com.mygdx.game.objects.BulletObject;
import com.mygdx.game.objects.EnemyBullet;
import com.mygdx.game.objects.ExplosiveTrashObject;
import com.mygdx.game.objects.HealBonusObject;
import com.mygdx.game.objects.PiratesObject;
import com.mygdx.game.objects.ShieldBonusObject;
import com.mygdx.game.objects.ShipObject;
import com.mygdx.game.objects.TrashObject;
import com.mygdx.game.objects.UltraKillBonusObject;

import java.util.ArrayList;
import java.util.Random;

public class GameScreen extends ScreenAdapter {
    MyGdxGame myGdxGame;
    ShipObject shipObject;
    GameSession gameSession;
    ArrayList<TrashObject> trashArray;
    ArrayList<BulletObject> bulletArray;
    ArrayList<EnemyBullet> enemyBulletArray;
    ArrayList<ExplosiveTrashObject> explosiveTrashArray;
    ArrayList<PiratesObject> piratesArray;
    ArrayList<HealBonusObject> healBonusArray;
    ArrayList<ShieldBonusObject> shieldBonusArray;
    ArrayList<UltraKillBonusObject> ultraKillBonusArray;
    ContactManager contactManager;
    MovingBackGroundView movingBackGroundView;
    ImageView topBlackoutView;
    ImageView fullBlackoutView;
    LiveView liveView;
    TextView scoreTextView;
    TextView pauseTextView;
    ButtonView homeButton;
    ButtonView continueButton;
    ButtonView pauseButton;
    TextView recordsTextView;
    RecordsListView recordsListView;
    ButtonView homeButton2;
    public GameScreen (MyGdxGame myGdxGame){
        this.myGdxGame = myGdxGame;

        gameSession = new GameSession();
        contactManager = new ContactManager(myGdxGame.world);

        movingBackGroundView = new MovingBackGroundView(BACKGROUND_IMG_PATH);

        topBlackoutView = new ImageView(0, 1180, BLACKOUT_TOP_IMG_PATH);
        fullBlackoutView = new ImageView(0, 0, BLACKOUT_FULL_IMG_PATH);

        scoreTextView = new TextView(myGdxGame.commonWhiteFont, 50, 1215);
        pauseTextView = new TextView(myGdxGame.largeWhiteFont, 282, 842, "Pause");
        recordsTextView = new TextView(myGdxGame.largeWhiteFont, 206, 842, "Last records");

        liveView = new LiveView(305, 1215);

        recordsListView = new RecordsListView(myGdxGame.commonWhiteFont, 690);

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
        explosiveTrashArray = new ArrayList<>();
        enemyBulletArray = new ArrayList<>();
        piratesArray = new ArrayList<>();
        healBonusArray = new ArrayList<>();
        shieldBonusArray = new ArrayList<>();
        ultraKillBonusArray = new ArrayList<>();

        shipObject = new ShipObject(
                SCREEN_WIDTH / 2, SCREEN_HEIGHT / 4,
                SHIP_WIDTH, SHIP_HEIGHT,
                myGdxGame.weapon,
                SHIP_IMG_PATH,
                myGdxGame.world
        );
    }

    @Override
    public void show(){
        restartGame();
    }
    @Override
    public void render(float delta) {
        handleInput();

        if (gameSession.state == PLAYING){

            myGdxGame.worldStep();

            gameSession.updateScore();
            scoreTextView.setText("Score: " + gameSession.getScore());
            liveView.setLeftLives(shipObject.getLiveLeft());
            if (gameSession.shouldSpawnTrash()){
                int enemyType = new Random().nextInt(19);
                if (enemyType < 4){
                    ExplosiveTrashObject explosiveTrashObject = new ExplosiveTrashObject(
                                TRASH_WIDTH, TRASH_HEIGHT,
                                TRASH_IMG_PATH,
                                myGdxGame.world
                        );
                        explosiveTrashArray.add(explosiveTrashObject);
                } else if (enemyType < 8) {
                    PiratesObject piratesObject = new PiratesObject(
                                PIRATES_WIDTH, PIRATES_HEIGHT,
                                PIRATES_IMG_PATH,
                                myGdxGame.world
                        );
                        piratesArray.add(piratesObject);
                } else if (enemyType < 16) {
                    TrashObject trashObject = new TrashObject(
                                TRASH_WIDTH, TRASH_HEIGHT,
                                TRASH_IMG_PATH,
                                myGdxGame.world
                        );
                        trashArray.add(trashObject);
                } else if (enemyType < 17) {
                    HealBonusObject healBonusObject = new HealBonusObject(35, 30, LIVE_IMG_PATH, myGdxGame.world);
                    healBonusArray.add(healBonusObject);
                }else if (enemyType < 18) {
                    ShieldBonusObject shieldBonusObject = new ShieldBonusObject(35, 30, SHIELD_IMG_PATH, myGdxGame.world);
                    shieldBonusArray.add(shieldBonusObject);
                }else{
                    UltraKillBonusObject ultraKillBonusObject = new UltraKillBonusObject(35, 30, SKULL_IMG_PATH, myGdxGame.world);
                    ultraKillBonusArray.add(ultraKillBonusObject);
                }
            }
            if (shipObject.needToShoot()){
                bulletArray = shipObject.shoot(bulletArray, myGdxGame.world);
                if (myGdxGame.audioManager.isSoundOn) myGdxGame.audioManager.shootSound.play();
            }
            trashUpdate();
            bulletUpdate();
            enemyBulletUpdate();
            explosiveTrashUpdate();
            piratesUpdate();
            healBonusUpdate();
            shieldBonusUpdate();
            ultraKillUpdate();

            if (!shipObject.isAlive()) {
                gameSession.endGame();
                recordsListView.setRecords(MemoryManager.loadRecordsTable());
            }

            movingBackGroundView.move();
        }
        draw();
    }
    private void restartGame() {
        for (int i = 0; i < trashArray.size(); i++) {
            myGdxGame.world.destroyBody(trashArray.get(i).body);
            trashArray.remove(i--);
        }
        for (int i = 0; i < explosiveTrashArray.size(); i++) {
            myGdxGame.world.destroyBody(explosiveTrashArray.get(i).body);
            explosiveTrashArray.remove(i--);
        }
        for (int i = 0; i < piratesArray.size(); i++) {
            myGdxGame.world.destroyBody(piratesArray.get(i).body);
            piratesArray.remove(i--);
        }
        for (int i = 0; i < healBonusArray.size(); i++) {
            myGdxGame.world.destroyBody(healBonusArray.get(i).body);
            healBonusArray.remove(i--);
        }
        for (int i = 0; i < shieldBonusArray.size(); i++) {
            myGdxGame.world.destroyBody(shieldBonusArray.get(i).body);
            shieldBonusArray.remove(i--);
        }
        for (int i = 0; i < ultraKillBonusArray.size(); i++) {
            myGdxGame.world.destroyBody(ultraKillBonusArray.get(i).body);
            ultraKillBonusArray.remove(i--);
        }

        if (shipObject != null)
            myGdxGame.world.destroyBody(shipObject.body);

        shipObject = new ShipObject(
                GameSettings.SCREEN_WIDTH / 2, 150,
                SHIP_WIDTH, SHIP_HEIGHT,
                myGdxGame.weapon,
                SHIP_IMG_PATH,
                myGdxGame.world
        );

        bulletArray.clear();
        enemyBulletArray.clear();
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
    private void draw(){
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();
        movingBackGroundView.draw(myGdxGame.batch);
        shipObject.draw(myGdxGame.batch);
        for (TrashObject trash: trashArray)
            trash.draw(myGdxGame.batch);
        for (HealBonusObject heal: healBonusArray)
            heal.draw(myGdxGame.batch);
        for (ShieldBonusObject shield: shieldBonusArray)
            shield.draw(myGdxGame.batch);
        for (UltraKillBonusObject ultraKill: ultraKillBonusArray)
            ultraKill.draw(myGdxGame.batch);
        for (ExplosiveTrashObject exlpTrash: explosiveTrashArray)
            exlpTrash.draw(myGdxGame.batch);
        for (PiratesObject pirate: piratesArray)
            pirate.draw(myGdxGame.batch);
        for (BulletObject bullet : bulletArray)
            bullet.draw(myGdxGame.batch);
        for (EnemyBullet enBullet : enemyBulletArray)
            enBullet.draw(myGdxGame.batch);

        topBlackoutView.draw(myGdxGame.batch);
        scoreTextView.draw(myGdxGame.batch);
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
            recordsTextView.draw(myGdxGame.batch);
            recordsListView.draw(myGdxGame.batch);
            homeButton2.draw(myGdxGame.batch);
        }
        myGdxGame.batch.end();
    }
    private void trashUpdate(){
        for (int i = 0; i < trashArray.size(); i++){
            boolean hasToBeDestroyed = !trashArray.get(i).isAlive() || !trashArray.get(i).isInFrame();

            if (!trashArray.get(i).isAlive()) {
                gameSession.destructionRegistration();
                if (myGdxGame.audioManager.isSoundOn)
                    myGdxGame.audioManager.explosionSound.play(0.2f);
            }

            if (hasToBeDestroyed) {
                myGdxGame.world.destroyBody(trashArray.get(i).body);
                trashArray.remove(i--);
            }
        }
    }
    private void healBonusUpdate(){
        for (int i = 0; i < healBonusArray.size(); i++){
            boolean hasToBeDestroyed = healBonusArray.get(i).wasUsed || !healBonusArray.get(i).isInFrame();

            if (hasToBeDestroyed) {
                myGdxGame.world.destroyBody(healBonusArray.get(i).body);
                healBonusArray.remove(i--);
            }
        }
    }
    private void shieldBonusUpdate(){
        for (int i = 0; i < shieldBonusArray.size(); i++){
            boolean hasToBeDestroyed = shieldBonusArray.get(i).wasUsed || !shieldBonusArray.get(i).isInFrame();

            if (hasToBeDestroyed) {
                myGdxGame.world.destroyBody(shieldBonusArray.get(i).body);
                shieldBonusArray.remove(i--);
            }
        }
    }
    private void ultraKillUpdate(){
        for (int i = 0; i < ultraKillBonusArray.size(); i++){
            boolean hasToBeDestroyed = ultraKillBonusArray.get(i).wasUsed || !ultraKillBonusArray.get(i).isInFrame();

            if (ultraKillBonusArray.get(i).wasUsed){
                for (int j = 0; j < trashArray.size(); j++) {
                    myGdxGame.world.destroyBody(trashArray.get(j).body);
                    trashArray.remove(j--);
                    gameSession.destructionRegistration();
                }
                for (int j = 0; j < explosiveTrashArray.size(); j++) {
                    myGdxGame.world.destroyBody(explosiveTrashArray.get(j).body);
                    explosiveTrashArray.remove(j--);
                    gameSession.destructionRegistration();
                }
                for (int j = 0; j < piratesArray.size(); j++) {
                    myGdxGame.world.destroyBody(piratesArray.get(j).body);
                    piratesArray.remove(j--);
                    gameSession.destructionRegistration();
                }
                enemyBulletArray.clear();
            }
            if (hasToBeDestroyed) {
                myGdxGame.world.destroyBody(ultraKillBonusArray.get(i).body);
                ultraKillBonusArray.remove(i--);
            }
        }
    }
    private void piratesUpdate(){
        for (int i = 0; i < piratesArray.size(); i++){
            boolean hasToBeDestroyed = !piratesArray.get(i).isAlive() || !piratesArray.get(i).isInFrame();

            if (!piratesArray.get(i).isAlive()) {
                gameSession.destructionRegistration();
                if (myGdxGame.audioManager.isSoundOn)
                    myGdxGame.audioManager.explosionSound.play(0.2f);
            }
            if (piratesArray.get(i).needToRotate())
                piratesArray.get(i).rotateToPos(new Vector2(shipObject.getX(), shipObject.getY()));
            if (hasToBeDestroyed) {
                myGdxGame.world.destroyBody(piratesArray.get(i).body);
                piratesArray.remove(i--);
            }
        }
    }
    private void explosiveTrashUpdate(){
        for (int i = 0; i < explosiveTrashArray.size(); i++){

            boolean hasToBeDestroyed = !explosiveTrashArray.get(i).isAlive() || !explosiveTrashArray.get(i).isInFrame() || shipObject.getY() >= explosiveTrashArray.get(i).getY();
            explosiveTrashArray.get(i).rotateConst();

            if (!explosiveTrashArray.get(i).isAlive() || shipObject.getY() >= explosiveTrashArray.get(i).getY()) {
                gameSession.destructionRegistration();
                if (myGdxGame.audioManager.isSoundOn)
                    myGdxGame.audioManager.explosionSound.play(0.2f);
                EnemyBullet enemyBullet1 = new EnemyBullet(
                        explosiveTrashArray.get(i).getX() + explosiveTrashArray.get(i).width / 2,
                        explosiveTrashArray.get(i).getY(),
                        TRASH_SHARP_WIDTH, TRASH_SHARP_HEIGHT,
                        TRASH_SHARP_IMG_PATH, myGdxGame.world, 0, true
                        );
                enemyBulletArray.add(enemyBullet1);
                EnemyBullet enemyBullet2 = new EnemyBullet(
                        explosiveTrashArray.get(i).getX() - explosiveTrashArray.get(i).width / 2,
                        explosiveTrashArray.get(i).getY(),
                        TRASH_SHARP_WIDTH, TRASH_SHARP_HEIGHT,
                        TRASH_SHARP_IMG_PATH, myGdxGame.world, 180, true
                        );
                enemyBulletArray.add(enemyBullet2);
            }

            if (hasToBeDestroyed) {
                myGdxGame.world.destroyBody(explosiveTrashArray.get(i).body);
                explosiveTrashArray.remove(i--);
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
    private void enemyBulletUpdate(){
        for (int i = 0; i < enemyBulletArray.size(); i++){
            enemyBulletArray.get(i).rotateConst();
            if (enemyBulletArray.get(i).hasToBeDestroyed()){
                    myGdxGame.world.destroyBody(enemyBulletArray.get(i).body);
                enemyBulletArray.remove(i--);
            }
        }
    }
}
