package com.example.killercontroller.Interface;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.killercontroller.Data.Singleton;
import com.example.killercontroller.R;

public class ConfigureActivity extends AppCompatActivity implements View.OnClickListener {

    private int shipIndex;
    private TextView textViewPlayerName;
    private String playerName;
    private ImageView back, next, ship;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure);

        this.mediaPlayer = MediaPlayer.create(this, R.raw.musica_menu);
        this.mediaPlayer.setLooping(true);
        this.mediaPlayer.start();
        this.textViewPlayerName = (TextView) findViewById(R.id.player_name);
        setPlayerName();
        this.back = (ImageView) findViewById(R.id.back_arrow);
        this.back.setOnClickListener(this);
        this.next = (ImageView) findViewById(R.id.next_arrow);
        this.next.setOnClickListener(this);
        this.ship = (ImageView) findViewById(R.id.ship);

        Singleton instance = Singleton.getInstance();

        instance.setShipAnimation(this.ship);
    }

    /**
     * Método para cambiar la imagen de la nave que aparecé en en centro de la pantalla
     */
    public void changeShip() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.back_arrow:
                break;
            case R.id.next_arrow:
                break;
            default:
                System.out.println("Invalid option");
        }
    }

    public void startPadActivity(View v) {

        System.out.println(textViewPlayerName.getText().toString());
        if (this.textViewPlayerName.getText().toString().length() > 0) {
            startActivity(new Intent(ConfigureActivity.this,
                    PadActivity.class));
        } else {
            Toast.makeText(this, "Nombre no indicado", Toast.LENGTH_SHORT).show();
        }
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
