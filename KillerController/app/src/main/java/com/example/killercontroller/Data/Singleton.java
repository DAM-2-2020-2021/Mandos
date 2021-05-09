package com.example.killercontroller.Data;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

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

    public void levitate(final View movableView, final float Y) {
        final long yourDuration = 2000;
        final TimeInterpolator yourInterpolator = new DecelerateInterpolator();
        movableView.animate().
                translationYBy(Y).
                setDuration(yourDuration).
                setInterpolator(yourInterpolator).
                setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        levitate(movableView, -Y);
                    }
                });
    }
}
