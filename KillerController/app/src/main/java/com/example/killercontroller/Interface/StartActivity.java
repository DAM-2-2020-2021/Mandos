package com.example.killercontroller.Interface;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
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

import com.example.killercontroller.Data.Singleton;
import com.example.killercontroller.R;

import java.net.InetAddress;
import java.net.UnknownHostException;

import eu.cifpfbmoll.netlib.node.NodeManager;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer mediaPlayer;
    private Singleton singleton;
    private TableLayout table;
    private TextView startTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        this.singleton = Singleton.getInstance();
        this.singleton.setMediaPlayer(MediaPlayer.create(this, R.raw.musica_menu));
        this.singleton.getMediaPlayer().setLooping(true);
        this.singleton.getMediaPlayer().start();
        this.startTextView = (TextView) findViewById(R.id.start);

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

    private void setStartVisible(final TextView subtitle){
        final Context context = StartActivity.this.getApplicationContext();
        Animation fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        subtitle.startAnimation(fadeOut);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            public void onAnimationEnd(Animation animation) {
                subtitle.setVisibility(View.GONE);
                Animation fadeIn = AnimationUtils.loadAnimation(context,R.anim.fade_in);
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

        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println(ip);
        NodeManager nodeManager = new NodeManager(1,ip);
        if (nodeManager.getNodeServer().join()){
            System.out.println("Funciona");
        }
        Intent intent;
        intent = new Intent(this, ConfigureActivity.class);
        intent.putExtra("PLAYER KEY", playerName);
        startActivity(intent);
    }

    public void setPlayerName() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_name);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
        final EditText name = dialog.findViewById(R.id.name_user);
        final TextView planerNameTextView = dialog.findViewById(R.id.name_dialog_textview);

        final Button play = dialog.findViewById(R.id.play_btn);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout loadingLayout = dialog.findViewById(R.id.loading_layout);
                loadingLayout.setVisibility(View.VISIBLE);
                name.setVisibility(View.GONE);
                play.setVisibility(View.GONE);
                planerNameTextView.setVisibility(View.GONE);

                startConfigureActivity(name.getText().toString());
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
    protected void onResume() {
        super.onResume();
        if (this.singleton.getMediaPlayer() != null) this.singleton.getMediaPlayer().start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (this.singleton.getMediaPlayer() != null) {
            this.singleton.getMediaPlayer().pause();
        }
    }
}