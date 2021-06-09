package com.example.killercontroller.Data;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.example.killercontroller.R;

public class Sound extends Activity{

    public static void alert(View v, float leftVolume, float rightVolume) {
        MediaPlayer mp = MediaPlayer.create(v.getContext(), R.raw.alert);
        mp.setVolume(leftVolume, rightVolume);
        mp.start();
    }

    public static void shoot(View v, float leftVolume, float rightVolume) {
        MediaPlayer mp = MediaPlayer.create(v.getContext(), R.raw.shoot);
        mp.setVolume(leftVolume, rightVolume);
        mp.start();
    }

    public static void shootPowerUp(View v, float leftVolume, float rightVolume) {
        MediaPlayer mp = MediaPlayer.create(v.getContext(), R.raw.shoot_powerup);
        mp.setVolume(leftVolume, rightVolume);
        mp.start();
    }

    public static void shield(View v, float leftVolume, float rightVolume) {
        MediaPlayer mp = MediaPlayer.create(v.getContext(), R.raw.shield);
        mp.setVolume(leftVolume, rightVolume);
        mp.start();
    }

    public static void death(Context v, float leftVolume, float rightVolume) {
        MediaPlayer mp = MediaPlayer.create(v, R.raw.death);
        mp.setVolume(leftVolume, rightVolume);
        mp.start();
    }

    public static void deathCollision(View v, float leftVolume, float rightVolume) {
        MediaPlayer mp = MediaPlayer.create(v.getContext(), R.raw.death_collision);
        mp.setVolume(leftVolume, rightVolume);
        mp.start();
    }

    public static void notice(View v, float leftVolume, float rightVolume) {
        MediaPlayer mp = MediaPlayer.create(v.getContext(), R.raw.notice);
        mp.setVolume(leftVolume, rightVolume);
        mp.start();
    }

    public static void alertPowerUp(View v, float leftVolume, float rightVolume) {
        MediaPlayer mp = MediaPlayer.create(v.getContext(), R.raw.powerup_alert);
        mp.setVolume(leftVolume, rightVolume);
        mp.start();
    }

    public static void teleport(View v, float leftVolume, float rightVolume) {
        MediaPlayer mp = MediaPlayer.create(v.getContext(), R.raw.teleport);
        mp.setVolume(leftVolume, rightVolume);
        mp.start();
    }

    public static void speed(View v, float leftVolume, float rightVolume) {
        MediaPlayer mp = MediaPlayer.create(v.getContext(), R.raw.speed_powerup);
        mp.setVolume(leftVolume, rightVolume);
        mp.start();
    }

    public static void menuSelect(View v, float leftVolume, float rightVolume) {
        MediaPlayer mp = MediaPlayer.create(v.getContext(), R.raw.menu_select);
        mp.setVolume(leftVolume, rightVolume);
        mp.start();
    }

}
