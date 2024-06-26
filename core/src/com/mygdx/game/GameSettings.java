package com.mygdx.game;

import javax.swing.plaf.PanelUI;

public class GameSettings {
    public static final int SCREEN_WIDTH = 720;
    public static final int SCREEN_HEIGHT = 1280;

    // Physics settings

    public static final float STEP_TIME = 1f / 60f;
    public static final int VELOCITY_ITERATIONS = 6;
    public static final int POSITION_ITERATIONS = 6;
    public static final float SCALE = 0.05f;

    public static float SHIP_FORCE_RATIO = 10;
    public static float TRASH_VELOCITY = 20;
    public static float PIRATES_VELOCITY = 20;
    public static long STARTING_TRASH_APPEARANCE_COOL_DOWN = 2000; // in [ms] - milliseconds
    public static int BULLET_VELOCITY = 200; // in [m/s] - meter per second
    public static int SHOTGUN_BULLET_VELOCITY = 20; // in [m/s] - meter per second
    public static int BARRIER_BULLET_VELOCITY = 100; // in [m/s] - meter per second
    public static int ENEMY_BULLET_VELOCITY = 50; // in [m/s] - meter per second
    public static int ENEMY_BULLET_ROTATE_VELOCITY = 15;
    public static int[] SHOOTING_COOL_DOWN = {1000, 500, 5000}; // in [ms] - milliseconds
    public static int SHIELD_DURATION = 5000; // in [ms] - milliseconds
    public static int PADDING_HORIZONTAL = 30;
    public static int PIRATES_POS_Y_TO_ROTATE = SCREEN_HEIGHT - 150;
    public static int EXPLOSIVE_TRASH_ROTATE_VELOCITY = 15;

    public static final short TRASH_BIT = 2;
    public static final short SHIP_BIT = 4;
    public static final short BULLET_BIT = 8;
    public static final short ENEMY_BULLET_BIT = 16;
    public static final short PIRATES_BIT = 32;
    public static final short HEAL_BONUS_BIT = 64;
    public static final short SHIELD_BONUS_BIT = 128;
    public static final short ULTRA_KILL_BONUS_BIT = 256;

    // Bonus tags
    public static final int HEAL_BONUS = 1;
    public static final int SHIELD_BONUS = 2;
    public static final int ULTRA_KILL_BONUS = 3;

    // Object sizes

    public static final int SHIP_WIDTH = 150;
    public static final int SHIP_HEIGHT = 150;
    public static final int PIRATES_WIDTH = 150;
    public static final int PIRATES_HEIGHT = 150;
    public static final int TRASH_WIDTH = 140;
    public static final int TRASH_HEIGHT = 100;
    public static final int TRASH_SHARP_WIDTH = 100;
    public static final int TRASH_SHARP_HEIGHT = 70;
    public static final int BULLET_WIDTH = 15;
    public static final int BULLET_HEIGHT = 45;
    public static final int SHOTGUN_BULLET_WIDTH = 23;
    public static final int SHOTGUN_BULLET_HEIGHT = 68;
    public static final int TRASH_MAX_LIVES = 1;
    public static final int EXPLOSIVE_TRASH_MAX_LIVES = 1;
    public static final int SHIP_LIVES = 3;
    public final static int LIVE_PADDING = 6;

    public final static int MINUTES_TO_SURVIVAL_IN_PIRATES = 2;
    public final static int MINUTES_TO_SURVIVAL_IN_PROTECTION = 2;
    public final static int COUNT_OF_TRASH_TO_DESTRUCT_IN_TRASH_COLLECTOR = 20;
}
