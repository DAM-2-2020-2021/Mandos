package com.example.killercontroller.Interface;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaActionSound;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.killercontroller.Data.Singleton;
import com.example.killercontroller.R;

public class ConfigureActivity extends AppCompatActivity implements View.OnClickListener {

    private int shipIndex;
    private ImageSwitcher img_switcher;
    private TextView textViewPlayerName;
    private String playerName;
    private ImageView back, next, ship;
    private MediaPlayer mediaPlayer;
    private int imgList[] = {R.drawable.nave1,
            R.drawable.nave2,
            R.drawable.nave3};
    private int currentIndex = 0;
    private int count = imgList.length;
    private Singleton instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure);

        this.img_switcher = (ImageSwitcher) findViewById(R.id.img_switcher);
        this.mediaPlayer = MediaPlayer.create(this, R.raw.musica_menu);
        this.mediaPlayer.setLooping(true);
        this.mediaPlayer.start();

        this.textViewPlayerName = (TextView) findViewById(R.id.player_name);
        setPlayerName();

        this.img_switcher = (ImageSwitcher) findViewById(R.id.img_switcher);
        img_switcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imgView = new ImageView(getApplicationContext());
                imgView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imgView.setLayoutParams(new ImageSwitcher.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT,
                        LinearLayout.LayoutParams.FILL_PARENT
                ));
                return imgView;
            }
        });
        img_switcher.setImageResource(imgList[0]);


        this.instance = Singleton.getInstance();
        this.instance.levitate(img_switcher.getCurrentView(),20);
        this.back = (ImageView) findViewById(R.id.back_arrow);
        this.back.setOnClickListener(this);
        this.next = (ImageView) findViewById(R.id.next_arrow);
        this.next.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.back_arrow:
                this.img_switcher.setInAnimation(this, R.anim.from_right);
                this.img_switcher.setOutAnimation(this, R.anim.to_left);
                --this.currentIndex;
                if (currentIndex < 0) {
                    this.currentIndex = this.imgList.length - 1;
                }
                this.img_switcher.setImageResource(this.imgList[this.currentIndex]);
                break;
            case R.id.next_arrow:
                this.img_switcher.setInAnimation(this, R.anim.from_left);
                this.img_switcher.setOutAnimation(this, R.anim.to_right);
                ++this.currentIndex;
                if (currentIndex == count) {
                    this.currentIndex = 0;
                }
                this.img_switcher.setImageResource(this.imgList[this.currentIndex]);
                break;
            default:
                System.out.println("Invalid option");
        }
        this.instance.levitate(img_switcher.getCurrentView(),20);
    }

    public void startPadActivity(View v) {

        System.out.println(textViewPlayerName.getText().toString());
        Intent intent;
        intent = new Intent(this, PadActivity.class);
        intent.putExtra("SHIP", this.imgList[this.currentIndex]);
        startActivity(intent);

    }

    private void setPlayerName() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        playerName = extras.getString("PLAYER KEY");

        if (playerName.equals("")) {
            playerName = "Player";
        }
        textViewPlayerName.setText(playerName);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ConfigureActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.mediaPlayer != null) this.mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (this.mediaPlayer != null) {
            this.mediaPlayer.pause();
        }
    }
}
