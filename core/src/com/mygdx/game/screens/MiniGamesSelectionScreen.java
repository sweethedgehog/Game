package com.mygdx.game.screens;

import static com.mygdx.game.GameResources.BACKGROUND_IMG_PATH;
import static com.mygdx.game.GameResources.BACKGROUND_SELECTION_LONG_BG_IMG_PATH;
import static com.mygdx.game.GameResources.BUTTON_LONG_BG_IMG_PATH;
import static com.mygdx.game.GameResources.BUTTON_SHORT_BG_IMG_PATH;
import static com.mygdx.game.GameSettings.COUNT_OF_TRASH_TO_DESTRUCT_IN_TRASH_COLLECTOR;
import static com.mygdx.game.GameSettings.MINUTES_TO_SURVIVAL_IN_PIRATES;
import static com.mygdx.game.GameSettings.MINUTES_TO_SURVIVAL_IN_PROTECTION;
import static com.mygdx.game.GameSettings.SCREEN_HEIGHT;
import static com.mygdx.game.GameSettings.SCREEN_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.components.ButtonView;
import com.mygdx.game.components.ImageView;
import com.mygdx.game.components.MovingBackGroundView;
import com.mygdx.game.components.TextView;

public class MiniGamesSelectionScreen extends ScreenAdapter {
    MyGdxGame myGdxGame;
    MovingBackGroundView backGroundView;
    TextView note;
    TextView explanation;
    ButtonView piratesMiniGame;
    ButtonView trashCollectorMiniGame;
    ButtonView protectionMiniGame;
    ButtonView returnButtonView;
    ImageView selectionView;
    String piratesExplanation = "You have very valuable cargo\non board. But, alas, your\nweapon is broken. Leave for " + MINUTES_TO_SURVIVAL_IN_PIRATES + "\nminutes until help arrives to\nyou!";
    String trashCollectionExplanation = "You have been assigned a\nmission to clear the Earth's \norbit of trash. Remove " + COUNT_OF_TRASH_TO_DESTRUCT_IN_TRASH_COLLECTOR + " \ntrash to complete the task!";
    String protectionExplanation = "You have been entrusted with\nthe mission of protecting the\nescape capsule from all kinds\nof dangers. Protect her\nas long as you can!";
    int modeSelection;
    public MiniGamesSelectionScreen(MyGdxGame myGdxGame){
        this.myGdxGame = myGdxGame;
        backGroundView = new MovingBackGroundView(BACKGROUND_IMG_PATH);
        note = new TextView(myGdxGame.largeWhiteFont, SCREEN_WIDTH / 2 - 220, SCREEN_HEIGHT - 150, "Select mini game!");
        explanation = new TextView(myGdxGame.commonWhiteFont, SCREEN_WIDTH / 2 - 220, SCREEN_HEIGHT / 2 - 265, "");
        piratesMiniGame = new ButtonView(
                SCREEN_WIDTH / 2 - 220, SCREEN_HEIGHT / 2 + 245,
                440, 70,
                myGdxGame.commonBlackFont,
                BUTTON_LONG_BG_IMG_PATH,
                "pirates hunt"
                );
        trashCollectorMiniGame = new ButtonView(
                SCREEN_WIDTH / 2 - 220, SCREEN_HEIGHT / 2 + 150,
                440, 70,
                myGdxGame.commonBlackFont,
                BUTTON_LONG_BG_IMG_PATH,
                "trash collection"
        );
        protectionMiniGame = new ButtonView(
                SCREEN_WIDTH / 2 - 220, SCREEN_HEIGHT / 2 + 55,
                440, 70,
                myGdxGame.commonBlackFont,
                BUTTON_LONG_BG_IMG_PATH,
                "protect the survivors"
        );
        returnButtonView = new ButtonView(
                SCREEN_WIDTH / 2 - 220, SCREEN_HEIGHT / 2 - 500,
                440, 70,
                myGdxGame.commonBlackFont,
                BUTTON_LONG_BG_IMG_PATH,
                "return to menu"
        );
        selectionView = new ImageView(0, 0, 460, 90, BACKGROUND_SELECTION_LONG_BG_IMG_PATH);
        modeSelection = 0;
    }
    @Override
    public void show(){
        modeSelection = 0;
        explanation.setText("");
    }
    @Override
    public void render(float delta){
        handleInput();
        selectionView.moveTo(SCREEN_WIDTH / 2 - 230, SCREEN_HEIGHT / 2 + 330 - 95 * modeSelection);

        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();
        backGroundView.draw(myGdxGame.batch);
        if (modeSelection != 0)
            selectionView.draw(myGdxGame.batch);
        note.draw(myGdxGame.batch);
        piratesMiniGame.draw(myGdxGame.batch);
        trashCollectorMiniGame.draw(myGdxGame.batch);
        protectionMiniGame.draw(myGdxGame.batch);
        returnButtonView.draw(myGdxGame.batch);
        explanation.draw(myGdxGame.batch);
        myGdxGame.batch.end();
    }
    private void handleInput(){
        if (Gdx.input.justTouched()) {
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if (piratesMiniGame.isHit(myGdxGame.touch.x, myGdxGame.touch.y)){
                if (modeSelection == 1)
                    myGdxGame.setScreen(myGdxGame.piratesMiniGameScreen);
                modeSelection = 1;
                explanation.setText(piratesExplanation);
            }
            if (trashCollectorMiniGame.isHit(myGdxGame.touch.x, myGdxGame.touch.y)){
                if (modeSelection == 2) {
                    myGdxGame.weaponSelectionScreen.setGameMode(2);
                    myGdxGame.setScreen(myGdxGame.weaponSelectionScreen);
                }
                modeSelection = 2;
                explanation.setText(trashCollectionExplanation);
            }
            if (protectionMiniGame.isHit(myGdxGame.touch.x, myGdxGame.touch.y)){
                if (modeSelection == 3) {
                    myGdxGame.weaponSelectionScreen.setGameMode(3);
                    myGdxGame.setScreen(myGdxGame.weaponSelectionScreen);
                }
                modeSelection = 3;
                explanation.setText(protectionExplanation);
            }
            if (returnButtonView.isHit(myGdxGame.touch.x, myGdxGame.touch.y))
                myGdxGame.setScreen(myGdxGame.menuScreen);
        }
    }
}
