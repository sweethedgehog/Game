package com.mygdx.game;

import static com.mygdx.game.GameSettings.STARTING_TRASH_APPEARANCE_COOL_DOWN;
import static com.mygdx.game.GameState.ENDED;

import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.managers.MemoryManager;

import java.util.ArrayList;

public class GameSession {
    long nextTrashSpawnTime;
    long sessionStartTime;
    long pauseStartTime;
    private int score;
    int destructedTrashNumber;
    public GameState state;
    public GameSession(){}
    public void startGame(){
        sessionStartTime = TimeUtils.millis();
        score = 0;
        destructedTrashNumber = 0;
        state = GameState.PLAYING;
        nextTrashSpawnTime = sessionStartTime + (long) (STARTING_TRASH_APPEARANCE_COOL_DOWN * getTrashPeriodCoolDown());
    }
    public void pauseGame(){
        pauseStartTime = TimeUtils.millis();
        state = GameState.PAUSED;
    }
    public void resumeGame(){
        sessionStartTime += TimeUtils.millis() - pauseStartTime;
        state = GameState.PLAYING;
    }
    public boolean shouldSpawnTrash() {
        if (nextTrashSpawnTime <= TimeUtils.millis()) {
            nextTrashSpawnTime = TimeUtils.millis() + (long) (STARTING_TRASH_APPEARANCE_COOL_DOWN
                    * getTrashPeriodCoolDown());
            return true;
        }
        return false;
    }
    private float getTrashPeriodCoolDown() {
        return (float) Math.exp(-0.001 * (TimeUtils.millis() - sessionStartTime + 1) / 1000);
    }
    public void destructionRegistration() {
        destructedTrashNumber += 1;
    }
    public void updateScore() {
        score = (int) (TimeUtils.millis() - sessionStartTime) / 100 + destructedTrashNumber * 100;
    }
    public int getScore() {
        return score;
    }
    public void endGame() {
        updateScore();
        state = ENDED;
        ArrayList<Integer> recordsTable = MemoryManager.loadRecordsTable();
        if (recordsTable == null) {
            recordsTable = new ArrayList<>();
        }
        int foundIdx = 0;
        for (; foundIdx < recordsTable.size(); foundIdx++)
            if (recordsTable.get(foundIdx) < getScore())
                break;

        recordsTable.add(foundIdx, getScore());
        MemoryManager.saveTableOfRecords(recordsTable);
    }
}
