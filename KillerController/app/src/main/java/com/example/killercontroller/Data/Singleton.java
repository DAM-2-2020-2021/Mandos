package com.example.killercontroller.Data;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import java.net.Socket;

public class Singleton {
    private static Singleton instance;

    public static Singleton getInstance(){
        return (instance == null) ? instance = new Singleton() : instance;
    }

    private Singleton(){
    }

    // implementation part
    private Socket mSocket;

    public Socket getSocket() {
        return mSocket;
    }

    public void setShipAnimation(View v) {

        ObjectAnimator animationYUp = ObjectAnimator.ofFloat(v, "y", -20f);
        ObjectAnimator animationYDown = ObjectAnimator.ofFloat(v, "y", 20f);
        animationYUp.setDuration(1000);
        animationYDown.setDuration(1000);
        final AnimatorSet animatorSetUp = new AnimatorSet();
        animatorSetUp.play(animationYUp);

        final AnimatorSet animatorSetDown = new AnimatorSet();
        animatorSetDown.play(animationYDown);
        animatorSetUp.addListener(new AnimatorListenerAdapter() {
            private boolean canceled = false;
            @Override
            public void onAnimationEnd(Animator animation) {
                animatorSetDown.start();
            }
        });

        animatorSetDown.addListener(new AnimatorListenerAdapter() {
            private boolean canceled = false;
            @Override
            public void onAnimationEnd(Animator animation) {
                animatorSetUp.start();
            }
        });
        animatorSetUp.start();
    }
}
