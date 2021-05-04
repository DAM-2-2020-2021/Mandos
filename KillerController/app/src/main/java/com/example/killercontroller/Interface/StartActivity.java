package com.example.killercontroller.Interface;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.killercontroller.R;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        this.mediaPlayer = MediaPlayer.create(this,R.raw.musica_menu);
        this.mediaPlayer.start();
        this.mediaPlayer.setLooping(true);

        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.start_layout);
        layout.setOnClickListener(this);


    }

    public void startPadActivity(String playerName){

        Intent intent;
        intent = new Intent(this, ConfigureActivity.class);
        intent.putExtra("PLAYER KEY", playerName);
        intent.putExtra("SONG TIME", this.mediaPlayer.getCurrentPosition());
        System.out.println(this.mediaPlayer.getCurrentPosition());
        startActivity(intent);
    }

    public void setPlayerName(){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_name);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        dialog.getWindow().setLayout(width,height);
        dialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
        final EditText name = dialog.findViewById(R.id.name_user);

        Button play = dialog.findViewById(R.id.play_btn);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPadActivity(name.getText().toString());
                dialog.dismiss();
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        setPlayerName();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(this.mediaPlayer != null) {
            this.mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(this.mediaPlayer != null) this.mediaPlayer.start();
    }
}