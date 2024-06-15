package com.mygdx.game.managers;

import static com.mygdx.game.GameResources.BACKGROUND_MUSIC_PATH;
import static com.mygdx.game.GameResources.DESTROY_SOUND_PATH;
import static com.mygdx.game.GameResources.SHOOT_SOUND_PATH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {
    public boolean isSoundOn = true;
    public boolean isMusicOn = true;

    public Music backgroundMusic;
    public Sound shootSound;
    public Sound explosionSound;
    public AudioManager(){
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(BACKGROUND_MUSIC_PATH));
        shootSound = Gdx.audio.newSound(Gdx.files.internal(SHOOT_SOUND_PATH));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal(DESTROY_SOUND_PATH));

        backgroundMusic.setVolume(0.2f);
        backgroundMusic.setLooping(true);

        updateMusicFlag();
        updateSoundFlag();
    }
    public void updateMusicFlag() {
        isMusicOn = MemoryManager.loadIsMusicOn();
        if (isMusicOn) backgroundMusic.play();
        else backgroundMusic.stop();
    }
    public void updateSoundFlag() {
        isSoundOn = MemoryManager.loadIsSoundOn();
    }
}
