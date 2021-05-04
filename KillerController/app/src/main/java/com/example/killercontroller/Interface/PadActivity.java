package com.example.killercontroller.Interface;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.killercontroller.Communication.Channel;
import com.example.killercontroller.Communication.Client;
import com.example.killercontroller.Data.Singleton;
import com.example.killercontroller.R;

public class PadActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private TextView left, right, shoot, move;
    private Channel channel;
    private ImageView shipPad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pad);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        this.shipPad = (ImageView) findViewById(R.id.ship_pad);
        this.left = (TextView)findViewById(R.id.left);
        this.left.setOnClickListener(this);
        this.left.setOnLongClickListener(this);
        this.right = (TextView)findViewById(R.id.right);
        this.right.setOnClickListener(this);
        this.right.setOnLongClickListener(this);
        this.move = (TextView)findViewById(R.id.move);
        this.move.setOnClickListener(this);
        this.move.setOnLongClickListener(this);
        this.shoot = (TextView)findViewById(R.id.shoot);
        this.shoot.setOnClickListener(this);
        this.shoot.setOnLongClickListener(this);

        this.channel = new Channel();
        Client client = new Client(channel, "192.168.0.26");

        Singleton singleton = Singleton.getInstance();
        singleton.setShipAnimation(this.shipPad);
    }

    @Override
    public void onClick(View v) {
        Vibrator vibe = (Vibrator) PadActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
        switch (v.getId()){
            case R.id.left:
                Toast.makeText(this,"Left",Toast.LENGTH_SHORT).show();
                vibe.vibrate(80);
               // this.channel.send();
                break;
            case R.id.right:
                Toast.makeText(this,"Right",Toast.LENGTH_SHORT).show();
                vibe.vibrate(80);
               // this.channel.send();
                break;
            case R.id.move:
                Toast.makeText(this,"Moving",Toast.LENGTH_SHORT).show();
                vibe.vibrate(80);
               // this.channel.send();;
                break;
            case R.id.shoot:
                Toast.makeText(this,"Shooting",Toast.LENGTH_SHORT).show();
                vibe.vibrate(80);
               // this.channel.send();
                break;
            default:
                System.out.println("Option not found");
        }
    }

    @Override
    public boolean onLongClick(View v) {
        Toast.makeText(this,"LongPress",Toast.LENGTH_SHORT).show();
        Vibrator vibe = (Vibrator) PadActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(80);
        return false;
    }
}