package com.traore.abasse.tafsir.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private int TEMPS_SPLASH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.son);
        mediaPlayer.start();
        TEMPS_SPLASH = mediaPlayer.getDuration();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        },TEMPS_SPLASH);
    }
}
