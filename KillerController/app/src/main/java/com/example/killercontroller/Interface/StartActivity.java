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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.killercontroller.Communication.Message;
import com.example.killercontroller.Utiilities.Singleton;
import com.example.killercontroller.Utiilities.Sound;
import com.example.killercontroller.R;

import java.util.List;

import eu.cifpfbmoll.netlib.node.NodeManager;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    private Singleton singleton;
    private TableLayout table;
    private TextView startTextView;
    private Button playButton;
    private String ip;
    private int myAdminId = 0;
    private EditText name;
    private Dialog connectDialog,playerDialog;
    private boolean adminseted = false, nicknameAck = false;
    private final String NICKNAME = "NICKNAME", ADMIN = "ADMIN", NICKNAMEACK = "NICKNAMEACK";

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

    /**
     * Método para enviar el nombre a la apliación de escritorio
     * @param message paquete con el nombre
     */
    private void sendName(Message message) {
        singleton.getNodeManager().send(this.myAdminId, message);
    }

    /**
     * Método para crear la animacion del subtitulo
     * @param subtitle textview a animar.
     */
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

    /**
     * Método para animar el titulo de la app.
     */
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

    /**
     * Método para mostrar la pantalla de cargando.
     */
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

        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        System.out.println(ip);
        singleton.setNodeManager(new NodeManager(ip));
        String subnet = this.singleton.getNodeManager().getSubnet(ip);
        List<String> ips = singleton.getNodeManager().getIpsForSubnet(subnet);
        singleton.getNodeManager().register(Message.class, (id, serverMessage) -> {
            switch (serverMessage.getMessageType()) {
                case NICKNAMEACK:
                    this.nicknameAck = true;
                    System.out.println("ack received");
                    startConfigureActivity(name.getText().toString());
                    break;
                case ADMIN:
                    this.myAdminId = Integer.parseInt(serverMessage.getMessage());
                    System.out.println("recibe el paquete");
                    connectDialog.dismiss();
                    if (!this.adminseted) {
                        setPlayerName();
                        this.adminseted = true;
                        showToast();
                    }
                    break;
                default:
                    System.out.println("Paquete inesperado." + serverMessage.getMessageType() + " " + serverMessage.getMessage());
            }
        });
        this.singleton.getNodeManager().startScan(ips);
        connectDialog.setCancelable(false);
        connectDialog.show();

    }

    /**
     * Método que crea un dialogo personalizado para solicitar el nombre al jugador.
     */
    public void setPlayerName() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playerDialog = new Dialog(StartActivity.this);
                playerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                playerDialog.setContentView(R.layout.dialog_name);
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;
                playerDialog.getWindow().setLayout(width, height);
                playerDialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
                name = playerDialog.findViewById(R.id.name_user);
                playButton = (Button) playerDialog.findViewById(R.id.play_btn);
                playButton.setOnClickListener(StartActivity.this);

                WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
                ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
                System.out.println(ip);

                playerDialog.setCancelable(false);
                playerDialog.show();
            }
        });

    }

    /**
     * Método para iniciar la actividad de configurar la nave y el mando.
     * @param playerName nombre del jugador que se pasará a la siguiente actividad.
     */
    public void startConfigureActivity(String playerName) {
        playerDialog.dismiss();
        Intent intent;
        intent = new Intent(this, ConfigureActivity.class);
        intent.putExtra("PLAYER KEY", playerName);
        intent.putExtra("ID NODE SERVER", this.myAdminId);
        startActivity(intent);
        this.finish();
        this.singleton.getNodeManager().unregister(Message.class);
    }

    /**
     * Método usado para mostrar mensajes toast en el momento que se recibe un paquete.
     */
    private void showToast(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(StartActivity.this.getBaseContext(),"Connected",Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Método que sobreescribe la accion de ir hacer clic de la actividad.
     */
    @Override
    public void onClick(View v) {
        System.out.println(v.getId());
        switch (v.getId()) {
            case R.id.start:
                Sound.notice(v, 0.9f, 0.9f);
                setLoadingScreen();
                break;
            case R.id.play_btn:
                Sound.notice(v, 0.9f, 0.9f);
                this.playButton.setEnabled(false);
                int tries = 0;
                Message message = new Message();
                System.out.println(name.getText().toString());
                message.setMessageType(NICKNAME);
                if (name.getText().toString().equals("")) {
                    name.setText("Player");
                }
                System.out.println(nicknameAck);
                message.setMessage(name.getText().toString());
                while (!nicknameAck && tries <= 5) {
                    sendName(message);
                    tries += 1;
                    System.out.println("PAQUETE NICK ENVIADO");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    /**
     * Método que sobreescribe la accion de detener la aplicación de la actividad.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (this.singleton.getMediaPlayer() != null) {
            this.singleton.getMediaPlayer().pause();
        }
    }

    /**
     * Método que sobreescribe la accion de reanudar la aplicación de la actividad.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Singleton singleton = Singleton.getInstance();
        singleton.setMediaPlayer(MediaPlayer.create(this, R.raw.musica_menu));
        singleton.getMediaPlayer().setLooping(true);
        singleton.getMediaPlayer().setVolume(0.2f, 0.2f);
        singleton.getMediaPlayer().start();
    }

}