package com.example.killercontroller.Interface;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.killercontroller.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView textSplash = (TextView) findViewById(R.id.splash_string);
        Animation fade2 = AnimationUtils.loadAnimation(this, R.anim.splash_animation);
        textSplash.startAnimation(fade2);

        fade2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(SplashActivity.this,
                        StartActivity.class));
                SplashActivity.this.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}