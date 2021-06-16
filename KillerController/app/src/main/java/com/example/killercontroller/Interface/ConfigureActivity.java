package com.example.killercontroller.Interface;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AppCompatActivity;

import com.example.killercontroller.Communication.Message;
import com.example.killercontroller.Utiilities.Singleton;
import com.example.killercontroller.Utiilities.Sound;
import com.example.killercontroller.R;

import java.util.Random;

public class ConfigureActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageSwitcher img_switcher;
    private TextView textViewPlayerName;
    private String playerName;
    private ImageView back, next;
    private int imgList[] = {R.drawable.nave_tipo1,
            R.drawable.nave_tipo2,
            R.drawable.nave_tipo3};
    private int currentIndex = 0;
    private int count = imgList.length;
    private Singleton singleton;
    private int idServer;
    private RadioButton blueTeam, redTeam;
    private ToggleButton readyButton;
    private String teamValue;
    private final String TEAM = "TEAM", READY = "READY", SPACECRAFT_TYPE = "SPACECRAFT TYPE", ADMIN = "ADMIN", START = "START", TEAM_RED_VALUE = "RED", TEAM_BLUE_VALUE = "BLUE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure);

        this.singleton = Singleton.getInstance();
        singleton.getNodeManager().register(Message.class, (id, serverMessage) -> {
            switch (serverMessage.getMessageType()) {
                case START:
                    startPadActivity();
                    break;
                case ADMIN:
                    System.out.println(serverMessage.getMessage());
                    System.out.println("Paquete de admin");
                    this.idServer = Integer.parseInt(serverMessage.getMessage());
                    break;
                default:
                    System.out.println("Option not found.");
            }
        });
        this.img_switcher = (ImageSwitcher) findViewById(R.id.img_switcher);

        this.textViewPlayerName = (TextView) findViewById(R.id.player_name);


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

        this.back = (ImageView) findViewById(R.id.back_arrow);
        this.back.setOnClickListener(this);
        this.next = (ImageView) findViewById(R.id.next_arrow);
        this.next.setOnClickListener(this);
        this.blueTeam = (RadioButton) findViewById(R.id.blue_team);
        this.blueTeam.setOnClickListener(this);
        this.redTeam = (RadioButton) findViewById(R.id.red_team);
        this.redTeam.setOnClickListener(this);
        this.readyButton = (ToggleButton) findViewById(R.id.ready_button);
        this.readyButton.setOnClickListener(this);
        setDefaultValues();
    }

    /**
     * Método que crea un dialogo para volver a la actividad principal.
     */
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
                Intent intent;
                intent = new Intent(ConfigureActivity.this, StartActivity.class);
                startActivity(intent);
                singleton.setNodeManager(null);
                ConfigureActivity.this.finish();
            }
        });
        dialog.show();
    }

    /**
     * Método que setea unos valores por defecto en la actividad del mando y en la aplicación del
     * escritorio para un jugador
     */
    private void setDefaultValues() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        playerName = extras.getString("PLAYER KEY");
        idServer = extras.getInt("ID NODE SERVER");
        Random rd = new Random();
        if (rd.nextBoolean()) {
            teamValue = TEAM_RED_VALUE;
            this.redTeam.setChecked(true);
        } else {
            teamValue = TEAM_BLUE_VALUE;
            this.blueTeam.setChecked(true);
        }
        Message team = new Message();
        Message ship = new Message();
        Message ready = new Message();
        team.setMessageType(this.TEAM);
        team.setMessage(this.teamValue);
        ship.setMessageType(SPACECRAFT_TYPE);
        ship.setMessage("0");
        ready.setMessageType(this.READY);
        ready.setMessage("");
        this.singleton.getNodeManager().send(this.idServer, team);
        this.singleton.getNodeManager().send(this.idServer, ship);
        this.singleton.getNodeManager().send(this.idServer, ready);

        textViewPlayerName.setText(playerName);
    }

    /**
     * Método que recoge unos valores e inicia el mando con unos valores predefinidos.
     */
    public void startPadActivity() {

        System.out.println(textViewPlayerName.getText().toString());
        Intent intent;
        intent = new Intent(this, PadActivity.class);
        int imgId = 0;
        switch (this.currentIndex) {
            case 0:
                if (this.teamValue.equals(TEAM_RED_VALUE)) {
                    imgId = R.drawable.nave_tipo1_roja;
                } else {
                    imgId = R.drawable.nave_tipo1_azul;
                }
                break;
            case 1:
                if (this.teamValue.equals(TEAM_RED_VALUE)) {
                    imgId = R.drawable.nave_tipo2_rojo;
                } else {
                    imgId = R.drawable.nave_tipo2_azul;
                }
                break;
            case 2:
                if (this.teamValue.equals(TEAM_RED_VALUE)) {
                    imgId = R.drawable.nave_tipo3_rojo;
                } else {
                    imgId = R.drawable.nave_tipo3_azul;
                }
                break;
        }
        intent.putExtra("SHIP", imgId);
        intent.putExtra("ID NODE SERVER", this.idServer);
        intent.putExtra("TEAM", this.teamValue);

        startActivity(intent);
        this.singleton.getNodeManager().unregister(Message.class);
        ConfigureActivity.this.finish();

    }

    /**
     * Método que sobreescribe la accion de ir hacia a tras de la actividad.
     */
    @Override
    public void onBackPressed() {
        exitGame();
    }

    /**
     * Método que sobreescribre la accion de hacer clic.
     * @param v vista sobre la que se hace clic.
     */
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
                Sound.notice(v, 0.9f, 0.9f);
                break;
            case R.id.next_arrow:
                this.img_switcher.setInAnimation(this, R.anim.from_left);
                this.img_switcher.setOutAnimation(this, R.anim.to_right);
                ++this.currentIndex;
                if (currentIndex == count) {
                    this.currentIndex = 0;
                }
                this.img_switcher.setImageResource(this.imgList[this.currentIndex]);
                Sound.notice(v, 0.9f, 0.9f);
                break;
            case R.id.blue_team:
                //PASAR IP DE LA ACTIVIDAD ANTERIOR
                Message message = new Message();
                message.setMessageType(TEAM);
                message.setMessage(TEAM_BLUE_VALUE);
                teamValue = TEAM_BLUE_VALUE;
                this.singleton.getNodeManager().send(idServer, message);
                Sound.notice(v, 0.9f, 0.9f);
                break;
            case R.id.red_team:
                Message redMessage = new Message();
                redMessage.setMessageType(TEAM);
                redMessage.setMessage(TEAM_RED_VALUE);
                this.teamValue = TEAM_RED_VALUE;
                this.singleton.getNodeManager().send(idServer, redMessage);
                Sound.notice(v, 0.9f, 0.9f);
                break;
            case R.id.ready_button:
                if (this.readyButton.isChecked()) {
                    Message shipType = new Message();
                    shipType.setMessageType(SPACECRAFT_TYPE);
                    shipType.setMessage(String.valueOf(this.currentIndex));
                    Message readyMessage = new Message();
                    readyMessage.setMessageType(READY);
                    readyMessage.setMessage("TRUE");
                    this.singleton.getNodeManager().send(idServer, shipType);
                    this.singleton.getNodeManager().send(idServer, readyMessage);
                    this.redTeam.setEnabled(false);
                    this.blueTeam.setEnabled(false);
                    this.back.setEnabled(false);
                    this.next.setEnabled(false);
                    this.readyButton.setTextColor(getResources().getColor(R.color.green));
                    Sound.menuSelect(ConfigureActivity.this, 0.9f, 0.9f);
                } else {
                    Message notReady = new Message();
                    notReady.setMessageType(READY);
                    notReady.setMessage("FALSE");
                    this.singleton.getNodeManager().send(idServer, notReady);
                    this.redTeam.setEnabled(true);
                    this.blueTeam.setEnabled(true);
                    this.back.setEnabled(true);
                    this.next.setEnabled(true);
                    this.readyButton.setTextColor(getResources().getColor(R.color.red));
                    Sound.menuSelect(ConfigureActivity.this, 0.9f, 0.9f);
                }
                break;
            default:
                System.out.println("Invalid option");
        }
    }

    /**
     * Método que sobreescribre la accion de detener la aplicacion.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (this.singleton.getMediaPlayer() != null) {
            this.singleton.getMediaPlayer().pause();
        }
    }

    /**
     * Método que sobreescribre la accion de reanudar la aplicacion.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (this.singleton.getMediaPlayer() != null) this.singleton.getMediaPlayer().start();
    }
}
