package com.example.killercontroller.Interface;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.killercontroller.Communication.Channel;
import com.example.killercontroller.Data.Singleton;
import com.example.killercontroller.R;

public class PadActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private TextView left, right, shoot, move;
    private ImageView shipPad;
    private Singleton singleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pad);

        this.singleton = Singleton.getInstance();
        this.singleton.getMediaPlayer().stop();
        this.singleton.setMediaPlayer(MediaPlayer.create(this, R.raw.musica_partida));
        this.singleton.getMediaPlayer().setLooping(true);
        this.singleton.getMediaPlayer().start();

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        this.shipPad = (ImageView) findViewById(R.id.ship_pad);

        Drawable d = getResources().getDrawable(extras.getInt("SHIP"));
        this.shipPad.setImageDrawable(d);

        this.left = (TextView) findViewById(R.id.left);
        this.left.setOnClickListener(this);
        this.left.setOnLongClickListener(this);
        this.right = (TextView) findViewById(R.id.right);
        this.right.setOnClickListener(this);
        this.right.setOnLongClickListener(this);
        this.move = (TextView) findViewById(R.id.move);
        this.move.setOnClickListener(this);
        this.move.setOnLongClickListener(this);
        this.shoot = (TextView) findViewById(R.id.shoot);
        this.shoot.setOnClickListener(this);
        this.shoot.setOnLongClickListener(this);

        singleton.levitate(this.shipPad, 20);

    }

    public void exitGame() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pause_menu);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);

        Button continues = dialog.findViewById(R.id.continue_btn);
        continues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button exit = dialog.findViewById(R.id.exit_btn);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PadActivity.this.finish();
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        Vibrator vibe = (Vibrator) PadActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
        switch (v.getId()) {
            case R.id.left:
                Toast.makeText(this, "Left", Toast.LENGTH_SHORT).show();
                vibe.vibrate(80);
                // this.channel.send();
                break;
            case R.id.right:
                Toast.makeText(this, "Right", Toast.LENGTH_SHORT).show();
                vibe.vibrate(80);
                // this.channel.send();
                break;
            case R.id.move:
                Toast.makeText(this, "Moving", Toast.LENGTH_SHORT).show();
                vibe.vibrate(80);
                // this.channel.send();;
                break;
            case R.id.shoot:
                Toast.makeText(this, "Shooting", Toast.LENGTH_SHORT).show();
                vibe.vibrate(80);
                // this.channel.send();
                break;
            default:
                System.out.println("Option not found");
        }
    }

    @Override
    public boolean onLongClick(View v) {
        Toast.makeText(this, "LongPress", Toast.LENGTH_SHORT).show();
        Vibrator vibe = (Vibrator) PadActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(80);
        return false;
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        exitGame();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.singleton.getMediaPlayer().stop();
        this.singleton.setMediaPlayer(MediaPlayer.create(this, R.raw.musica_menu));
        this.singleton.getMediaPlayer().setLooping(true);
        this.singleton.getMediaPlayer().start();
    }
}