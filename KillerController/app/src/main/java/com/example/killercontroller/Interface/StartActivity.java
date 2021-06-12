package com.example.killercontroller.Interface;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.format.Formatter;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.killercontroller.Communication.Message;
import com.example.killercontroller.Data.Singleton;
import com.example.killercontroller.R;

import java.util.List;

import eu.cifpfbmoll.netlib.node.NodeManager;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {


    private Singleton singleton;
    private TableLayout table;
    private TextView startTextView;
    private Button testing, playButton;
    private String ip;
    private int myAdminId = 0;
    private EditText name;
    private Dialog connectDialog;
    private boolean adminseted = false, nicknameAck = false;
    private final String NICKNAME = "NICKNAME", TEAM = "TEAM", READY = "READY", SPACECRAFT_TYPE = "SPACECRAFT TYPE", ADMIN = "ADMIN", NICKNAMEACK = "NICKNAMEACK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        this.singleton = Singleton.getInstance();
        this.startTextView = (TextView) findViewById(R.id.start);
        this.startTextView.setOnClickListener(this);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        LinearLayout layout = (LinearLayout) findViewById(R.id.start_layout);
        layout.setOnClickListener(this);

        this.table = (TableLayout) findViewById(R.id.table);
        titleAnimation();

        final TextView subtitle = (TextView) findViewById(R.id.subtitle);
        final Animation fade1 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        subtitle.startAnimation(fade1);
        fade1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            public void onAnimationEnd(Animation animation) {
                setStartVisible(subtitle);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void setStartVisible(final TextView subtitle) {
        final Context context = StartActivity.this.getApplicationContext();
        Animation fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        subtitle.startAnimation(fadeOut);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            public void onAnimationEnd(Animation animation) {
                subtitle.setVisibility(View.GONE);
                Animation fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
                startTextView.startAnimation(fadeIn);
                startTextView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void titleAnimation() {
        Animation spinin = AnimationUtils.loadAnimation(this, R.anim.splash_animation);
        LayoutAnimationController controller = new LayoutAnimationController(spinin);

        for (int i = 0; i < table.getChildCount(); i++) {
            TableRow row = (TableRow) table.getChildAt(i);
            row.setLayoutAnimation(controller);
        }
        Context context = StartActivity.this.getApplicationContext();
        Animation fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out_title);
        table.startAnimation(fadeOut);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            public void onAnimationEnd(Animation animation) {
                table.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    public void startConfigureActivity(String playerName) {
        Intent intent;
        intent = new Intent(this, ConfigureActivity.class);
        intent.putExtra("PLAYER KEY", playerName);
        intent.putExtra("ID NODE SERVER", this.myAdminId);
        startActivity(intent);
        this.singleton.getNodeManager().unregister(Message.class);
    }

    public void setPlayerName() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Dialog dialog = new Dialog(StartActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_name);
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;
                dialog.getWindow().setLayout(width, height);
                dialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
                name = dialog.findViewById(R.id.name_user);
                playButton = (Button) dialog.findViewById(R.id.play_btn);
                playButton.setOnClickListener(StartActivity.this);

                WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
                ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
                System.out.println(ip);
                System.out.println(ip);
                System.out.println(ip);
                System.out.println(ip);
                System.out.println(ip);

                dialog.setCancelable(false);
                dialog.show();
            }
        });

    }

    public void setLoadingScreen() {

        connectDialog = new Dialog(this);
        connectDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        connectDialog.setContentView(R.layout.loading_screen);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        connectDialog.getWindow().setLayout(width, height);
        connectDialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
        this.testing = (Button) connectDialog.findViewById(R.id.button_testing_start);
        this.testing.setOnClickListener(StartActivity.this);

        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        System.out.println(ip);
        singleton.setNodeManager(new NodeManager(ip));
        List<String> ips = singleton.getNodeManager().getIpsForSubnet("172.20.10");
        singleton.getNodeManager().register(Message.class, (id, serverMessage) -> {
            switch (serverMessage.getMessageType()) {
                case ADMIN:
                    this.myAdminId = Integer.parseInt(serverMessage.getMessage());
                    System.out.println("recibe el paquete");
                    connectDialog.dismiss();
                    if (!this.adminseted) {
                        setPlayerName();
                        this.adminseted = true;
                    }
                    break;
                case NICKNAMEACK:
                    this.nicknameAck = true;
                    startConfigureActivity(name.getText().toString());
                default:
                    System.out.println("Paquete inesperado." + serverMessage.getMessageType() + " " + serverMessage.getMessage());
            }
        });
        this.singleton.getNodeManager().startScan(ips);
        connectDialog.setCancelable(false);
        connectDialog.show();

    }

    @Override
    public void onClick(View v) {
        System.out.println(v.getId());
        switch (v.getId()) {
            case R.id.start:
                setLoadingScreen();
                break;
            case R.id.play_btn:
                Message message = new Message();
                System.out.println(this.name.getText().toString());
                message.setMessageType("NICKNAME");
                if (this.name.getText().toString().equals("")) {
                    this.name.setText("Player");
                }
                message.setMessage(this.name.getText().toString());


                singleton.getNodeManager().send(this.myAdminId, message);
                this.playButton.setEnabled(false);
                while (!this.nicknameAck) {
                    try {
                        Thread.sleep(2000);
                        singleton.getNodeManager().send(this.myAdminId, message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(message.getMessage());
                break;
            case R.id.button_testing_start:
                connectDialog.dismiss();
                setPlayerName();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Singleton singleton = Singleton.getInstance();
        singleton.setMediaPlayer(MediaPlayer.create(this, R.raw.musica_menu));
        singleton.getMediaPlayer().setLooping(true);
        singleton.getMediaPlayer().setVolume(0.2f, 0.2f);
        singleton.getMediaPlayer().start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (this.singleton.getMediaPlayer() != null) {
            this.singleton.getMediaPlayer().pause();
        }
    }
}