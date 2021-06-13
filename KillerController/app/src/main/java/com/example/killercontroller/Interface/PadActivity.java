package com.example.killercontroller.Interface;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.os.Vibrator;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.killercontroller.Communication.Message;
import com.example.killercontroller.Data.Singleton;
import com.example.killercontroller.Data.Sound;
import com.example.killercontroller.R;

public class PadActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private TextView left, right, shoot, move, redScore, blueScore;
    private ImageView shipPad;
    private Singleton singleton;
    private int currentScreen;
    private Chronometer chronometer;
    private final String SCORE = "SCORE", DEAD = "DEAD", FINISH = "FINISH", ADMIN = "ADMIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pad);

        this.singleton = Singleton.getInstance();
        this.singleton.getMediaPlayer().stop();
        this.singleton.setMediaPlayer(MediaPlayer.create(this, R.raw.musica_partida));
        this.singleton.getMediaPlayer().setVolume(0.2f, 0.2f);
        this.singleton.getMediaPlayer().setLooping(true);
        this.singleton.getMediaPlayer().start();

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        this.chronometer = (Chronometer) findViewById(R.id.match_time);
        this.chronometer.setTypeface(ResourcesCompat.getFont(this, R.font.pixelart));
        this.chronometer.start();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        this.shipPad = (ImageView) findViewById(R.id.ship_pad);

        Drawable d = getResources().getDrawable(extras.getInt("SHIP"));
        this.shipPad.setImageDrawable(d);
        this.currentScreen = extras.getInt("ID NODE SERVER");
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
        this.redScore = (TextView) findViewById(R.id.red_score);
        this.blueScore = (TextView) findViewById(R.id.blue_score);

        this.singleton.getNodeManager().register(Message.class, (id, serverMessage) -> {
            System.out.println(serverMessage.getMessage());
            switch (serverMessage.getMessageType()) {
                case SCORE:
                    updateScores(serverMessage);
                    System.out.println("recibe el paquete de puntuacion");
                    break;
                case DEAD:
                    showDeathScreen();
                    break;
                case FINISH:
                    if (Integer.parseInt(this.redScore.getText().toString()) > Integer.parseInt(this.blueScore.getText().toString())) {
                        showLooseScreen();
                    } else {
                        showWinScreen();
                    }
                    break;
                case ADMIN:
                    this.currentScreen = Integer.parseInt(serverMessage.getMessage());
                    break;
            }
        });

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

    private void updateScores(Message serverMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView redScore = (TextView) findViewById(R.id.red_score);
                TextView blueScore = (TextView) findViewById(R.id.blue_score);
                String scoreFromServer = serverMessage.getMessageType();
                String[] scores = scoreFromServer.split(":");
                for (int i = 0; i < scores.length; i++) {
                    System.out.println(scores[i]);
                }
                redScore.setText(scores[0]);
                blueScore.setText(scores[1]);
                showDeathScreen();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Vibrator vibe = (Vibrator) PadActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
        switch (v.getId()) {
            case R.id.left:
                Message left = new Message();
                left.setMessageType("MOVEMENT");
                left.setMessage("LEFT");
                this.singleton.getNodeManager().send(this.currentScreen, left);
                vibe.vibrate(80);
                // showDeathScreen();
                break;
            case R.id.right:
                Message right = new Message();
                right.setMessageType("MOVEMENT");
                right.setMessage("RIGHT");
                this.singleton.getNodeManager().send(this.currentScreen, right);
                Toast.makeText(this, "Right", Toast.LENGTH_SHORT).show();
                vibe.vibrate(80);
                //showLooseScreen();
                // this.channel.send();
                break;
            case R.id.move:
                Message move = new Message();
                move.setMessageType("MOVEMENT");
                move.setMessage("MOVE");
                this.singleton.getNodeManager().send(this.currentScreen, move);
                Toast.makeText(this, "Moving", Toast.LENGTH_SHORT).show();
                vibe.vibrate(80);
                //  showWinScreen();
                // this.channel.send();;
                break;
            case R.id.shoot:
                Message shoot = new Message();
                shoot.setMessageType("SHOOT");
                shoot.setMessage("SHOOT");
                this.singleton.getNodeManager().send(this.currentScreen, shoot);
                Toast.makeText(this, "Shooting", Toast.LENGTH_SHORT).show();
                vibe.vibrate(80);
                Sound.shoot(v, 0.9f, 0.9f);
                break;
            default:
                System.out.println("Option not found");
        }
    }

    @Override
    public boolean onLongClick(View v) {
       /* Vibrator vibe = (Vibrator) PadActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
        while (v.isPressed()) {
            switch (v.getId()) {
                case R.id.left:
                    Toast.makeText(this, "left", Toast.LENGTH_SHORT).show();
                    vibe.vibrate(80);
                    // showDeathScreen();
                    break;
                case R.id.right:
                    Toast.makeText(this, "Right", Toast.LENGTH_SHORT).show();
                    vibe.vibrate(80);
                    //showLooseScreen();
                    // this.channel.send();
                    break;
                case R.id.move:
                    Toast.makeText(this, "Moving", Toast.LENGTH_SHORT).show();
                    vibe.vibrate(80);
                    //  showWinScreen();
                    // this.channel.send();;
                    break;
                case R.id.shoot:
                    Toast.makeText(this, "Shooting", Toast.LENGTH_SHORT).show();
                    vibe.vibrate(80);
                    Sound.shoot(v, 0.9f, 0.9f);
                    break;
                default:
                    System.out.println("Option not found");
            }
        }*/
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
    }

    /**
     * Show a custom dialog for a death screen
     */
    private void showDeathScreen() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Dialog dialog = new Dialog(PadActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.death_screen);
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;
                dialog.getWindow().setLayout(width, height);
                dialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
                dialog.show();
                Sound.death(PadActivity.this.getBaseContext(), 0.7f, 0.7f);
                new CountDownTimer(5000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        dialog.dismiss();
                    }
                }.start();
            }
        });
    }

    /**
     * Show a custom dialog for a loose screen
     */
    private void showLooseScreen() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Dialog dialog = new Dialog(PadActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.loose_screen);
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;
                dialog.getWindow().setLayout(width, height);
                dialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
                dialog.setCancelable(true);
                dialog.show();

                TextView looseTextView = (TextView) dialog.findViewById(R.id.loose_message);
                Context context = PadActivity.this.getApplicationContext();
                Animation loose = AnimationUtils.loadAnimation(context, R.anim.loose_animation);
                looseTextView.startAnimation(loose);
            }
        });
    }


    /**
     * Show a custom dialog for a win screen
     */
    private void showWinScreen() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Dialog dialog = new Dialog(PadActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.win_screen);
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;
                dialog.getWindow().setLayout(width, height);
                dialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
                dialog.show();

                TextView looseTextView = (TextView) dialog.findViewById(R.id.win_message);
                Context context = PadActivity.this.getApplicationContext();
                Animation loose = AnimationUtils.loadAnimation(context, R.anim.win_animation);
                looseTextView.startAnimation(loose);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.singleton.getMediaPlayer() != null) this.singleton.getMediaPlayer().start();
    }

}