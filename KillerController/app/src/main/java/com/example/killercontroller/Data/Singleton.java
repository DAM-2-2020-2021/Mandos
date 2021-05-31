package com.example.killercontroller.Data;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.media.MediaPlayer;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import eu.cifpfbmoll.netlib.node.NodeManager;

public class Singleton {

    private MediaPlayer mediaPlayer;
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

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
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

    public void setListeners(){
        try {
            NodeManager nodeManager = new NodeManager(InetAddress.getLocalHost().getHostAddress());
            nodeManager.register(User.class, (id, user) -> {
                System.out.println();
            });
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
