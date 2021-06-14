package com.example.killercontroller.Interface;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
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

public class PadActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView left, right, shoot, move, redScore, blueScore;
    private ImageView shipPad;
    private Singleton singleton;
    private int currentScreen;
    private Chronometer chronometer;
    private int shipDrawableId;
    private String teamValue;
    private final String SCORE = "SCORE", DEAD = "DEAD", FINISH = "FINISH", ADMIN = "ADMIN", TEAM_RED_VALUE = "RED", TEAM_BLUE_VALUE = "BLUE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pad);

        this.singleton = Singleton.getInstance();
        this.singleton.getMediaPlayer().stop();
        this.singleton.getMediaPlayer().release();
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
        this.teamValue = extras.getString("TEAM");
        this.shipDrawableId = extras.getInt("SHIP");
        Drawable d = getResources().getDrawable(extras.getInt("SHIP"));
        this.shipPad.setImageDrawable(d);
        this.currentScreen = extras.getInt("ID NODE SERVER");
        this.left = (TextView) findViewById(R.id.left);
        this.left.setOnClickListener(this);
        this.right = (TextView) findViewById(R.id.right);
        this.right.setOnClickListener(this);
        this.move = (TextView) findViewById(R.id.move);
        this.move.setOnClickListener(this);
        this.shoot = (TextView) findViewById(R.id.shoot);
        this.shoot.setOnClickListener(this);
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
                    if (this.teamValue.equals(TEAM_RED_VALUE)) {
                        if (Integer.parseInt(this.redScore.getText().toString()) > Integer.parseInt(this.blueScore.getText().toString())) {
                            showWinScreen();
                        } else if (Integer.parseInt(this.redScore.getText().toString()) < Integer.parseInt(this.blueScore.getText().toString())) {
                            showLooseScreen();
                        } else {
                            showDrawScreen();
                        }
                    } else if (this.teamValue.equals(TEAM_BLUE_VALUE)) {
                        if (Integer.parseInt(this.redScore.getText().toString()) > Integer.parseInt(this.blueScore.getText().toString())) {
                            showLooseScreen();
                        } else if (Integer.parseInt(this.redScore.getText().toString()) < Integer.parseInt(this.blueScore.getText().toString())) {
                            showWinScreen();
                        } else {
                            showDrawScreen();
                        }
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
                startInitialActivity();
            }
        });
        Button exit = dialog.findViewById(R.id.exit_btn);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startInitialActivity();
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
                String scoreFromServer = serverMessage.getMessage();
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
                break;
            case R.id.right:
                Message right = new Message();
                right.setMessageType("MOVEMENT");
                right.setMessage("RIGHT");
                this.singleton.getNodeManager().send(this.currentScreen, right);
                Toast.makeText(this, "Right", Toast.LENGTH_SHORT).show();
                vibe.vibrate(80);
                break;
            case R.id.move:
                Message move = new Message();
                move.setMessageType("MOVEMENT");
                move.setMessage("MOVE");
                this.singleton.getNodeManager().send(this.currentScreen, move);
                Toast.makeText(this, "Moving", Toast.LENGTH_SHORT).show();
                vibe.vibrate(80);
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
    public void onBackPressed() {
        // super.onBackPressed();
        exitGame();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
                ImageView shipPad = (ImageView) findViewById(R.id.ship_pad);
                shipPad.setBackgroundResource(R.drawable.explosion);
                shipPad.setImageDrawable(null);

                AnimationDrawable explosion = (AnimationDrawable) shipPad.getBackground();
                explosion.start();

                Sound.death(PadActivity.this.getBaseContext(), 0.7f, 0.7f);
                new CountDownTimer(3000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        dialog.dismiss();
                        Drawable ship = getDrawable(shipDrawableId);
                        shipPad.setImageDrawable(ship);
                        shipPad.setBackgroundResource(R.color.translucent_grey);


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
                TextView redScore = (TextView) findViewById(R.id.red_score);
                TextView blueScore = (TextView) findViewById(R.id.blue_score);
                TextView dialogRedScore = (TextView) dialog.findViewById(R.id.red_score_loose);
                TextView dialogBlueScore = (TextView) dialog.findViewById(R.id.blue_score_loose);
                dialogRedScore.setText(redScore.getText().toString());
                dialogBlueScore.setText(blueScore.getText().toString());
                Context context = PadActivity.this.getApplicationContext();
                Animation loose = AnimationUtils.loadAnimation(context, R.anim.loose_animation);
                looseTextView.startAnimation(loose);
                new CountDownTimer(5000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        dialog.dismiss();
                        startInitialActivity();
                    }
                }.start();
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
                TextView redScore = (TextView) findViewById(R.id.red_score);
                TextView blueScore = (TextView) findViewById(R.id.blue_score);
                TextView dialogRedScore = (TextView) dialog.findViewById(R.id.red_score_win);
                TextView dialogBlueScore = (TextView) dialog.findViewById(R.id.blue_score_win);
                dialogRedScore.setText(redScore.getText().toString());
                dialogBlueScore.setText(blueScore.getText().toString());

                Context context = PadActivity.this.getApplicationContext();
                Animation loose = AnimationUtils.loadAnimation(context, R.anim.win_animation);
                looseTextView.startAnimation(loose);

                new CountDownTimer(5000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        dialog.dismiss();
                        startInitialActivity();
                    }
                }.start();

            }
        });
    }

    private void showDrawScreen() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Dialog dialog = new Dialog(PadActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.draw_screen);
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;
                dialog.getWindow().setLayout(width, height);
                dialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
                dialog.setCancelable(true);
                dialog.show();

                TextView looseTextView = (TextView) dialog.findViewById(R.id.draw_message);
                TextView redScore = (TextView) findViewById(R.id.red_score);
                TextView blueScore = (TextView) findViewById(R.id.blue_score);
                TextView dialogRedScore = (TextView) dialog.findViewById(R.id.red_score_draw);
                TextView dialogBlueScore = (TextView) dialog.findViewById(R.id.blue_score_draw);
                dialogRedScore.setText(redScore.getText().toString());
                dialogBlueScore.setText(blueScore.getText().toString());
                Context context = PadActivity.this.getApplicationContext();
                Animation loose = AnimationUtils.loadAnimation(context, R.anim.loose_animation);
                looseTextView.startAnimation(loose);
                new CountDownTimer(5000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        startInitialActivity();
                        dialog.dismiss();

                    }
                }.start();
            }
        });
    }

    public void startInitialActivity() {

        // this.singleton.getNodeManager().removeNodeId(this.currentScreen);
        Intent intent;
        intent = new Intent(this, StartActivity.class);
        startActivity(intent);
        this.singleton.getNodeManager().unregister(Message.class);
        this.singleton.setNodeManager(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}