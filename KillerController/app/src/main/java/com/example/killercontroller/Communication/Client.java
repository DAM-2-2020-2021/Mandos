package com.example.killercontroller.Communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Client implements Runnable {

    private String ip;
    private int port = 9999;
    private Channel channel;
    private boolean running = true;
    private int minSleep = 200;
    private int maxSleep = 400;

    /**
     * Constructor que recibe el channel por el cual se comunicará con el otro equipo la ip a la que tiene que
     * conectarse.
     *
     * @param channel objeto a través del cual enviará la información
     * @param ip      ip del equipo remoto a la que se conectará
     */

    public Client(Channel channel, String ip) {
        this.channel = channel;
        this.ip = ip;
        Thread clientThread = new Thread(this);
        clientThread.start();
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


    /**
     * Método encargado de iniciar la conexión con el server
     */
    private void startConnection() {
        Socket socket = null;
        try {
            if (!this.channel.isOk()) {
                socket = new Socket(ip, port);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                String greeting = "BALLTASK";
                out.writeUTF(greeting);
                DataInputStream in = new DataInputStream(socket.getInputStream());
                String response = in.readUTF();
                System.out.println(response);
                if (response.equals("OK")) {
                    this.channel.setSocket(socket);
                    System.out.println("Conexion establecida");
                }
            }
        } catch (Exception e) {
            System.out.println("Setting socket ");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (this.running) {
            startConnection();
        }
    }
}

