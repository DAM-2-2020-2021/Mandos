package com.example.killercontroller.Communication;

import java.io.*;
import java.net.Socket;


public class Channel implements Runnable {

    private Socket socket;
    private boolean ok = false;
    private Thread channelThread;

    private HealthChannel healthChannel;

    /**
     * Constructor que recibe un balltask para poder pasarselo a las pelotas que se crean en esta clase y
     * para poder añadir y eliminar pelotas del arrayList
     *

     */
    public Channel() {
    }

    // GETTERS Y SETTERS
    // ----------------------------------------------------------
    public boolean isOk() {
        return ok;
    }

    public synchronized void setOk(boolean ok) {
        this.ok = ok;
    }

    public Socket getSocket() {
        return socket;
    }

    /**
     * Método para asignar un socket al channel, si esté ya está setteado no le asigna ningún valor nuevo.
     *
     * @param socket socket por donde se comunicarán los dos programas.
     */
    public synchronized void setSocket(Socket socket) {
        if (!this.ok) {
            this.ok = true;
            this.socket = socket;
            this.channelThread = new Thread(this);
            this.channelThread.start();
            this.healthChannel = new HealthChannel(this);
        }
    }

    /**
     * Método para crear una pelota nueva a partir de la info de un string separado por comas.
     *
     * @param ballInfo información relevante de una pelota.
     * @return la nueva pelota.
     */
   /* private Ball createBall(String ballInfo) {
        String[] info = ballInfo.split(",");
        Ball ball = new Ball();
        ball.setBallTask(this.ballTask);
        ball.setSize(Integer.parseInt(info[1]));
        ball.setOutSide(true);
        if(Integer.parseInt(info[4]) > this.ballTask.getHeight()){
            ball.setCordY(this.ballTask.getHeight() - ball.getSize());
        }else{
            ball.setCordY(Integer.parseInt(info[4]));
        }
        if(this.ballTask.getDirection().equals("izquierda")){
            ball.setCordX(1 + ball.getSize());
            ball.setVelX(1);
        }else if(this.ballTask.getDirection().equals("derecha")){
            ball.setCordX(this.ballTask.getViewer().getWidth() - (ball.getSize() + 1));
            ball.setVelX(-1);
        }

        ball.setVelY(Integer.parseInt(info[3]));
        ball.setSleepTime(10);
        ball.setChannel(this);
        ball.setColor(new Color(Integer.parseInt(info[6]), Integer.parseInt(info[7]), Integer.parseInt(info[8])));
        ball.setRect(new Rectangle(ball.getSize(), ball.getSize()));
        ball.getRect().setBounds(ball.getCordX(), ball.getCordY(), ball.getSize(), ball.getSize());
        ball.setBallThread(new Thread(ball));
        ball.getBallThread().start();
        return ball;
    }*/

    /**
     * Método que envia una comprobación de estado para el healthChannel.
     * @param message mensaje que se enviará.
     */
    public void sendACK(String message) {
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(this.socket.getOutputStream());
            out.writeUTF(message);

        } catch (IOException e) {
            this.ok = false;
            e.printStackTrace();
        }
    }

    /**
     * Método para enviar la información relevante de una pelota a través del canal. Envia un string separado por comas
     * con los valores que pueden variar sobre la pelota.
     *
     */
    public void send() {
        try {
            DataOutputStream writer = new DataOutputStream((this.socket.getOutputStream()));
            String ballInfo = "Pad" + "," + "Esto es una prueba";
            writer.writeUTF(ballInfo);
        } catch (IOException e) {
            System.out.println("Connection reset " + e.getMessage() );
            e.printStackTrace();
            this.ok = false;
        }
    }

    /**
     * Método que usamos para recibir la información de la pelota, y de los ack
     * del healthChannel, crear una nueva y añadirla a la lista del balltask.
     */
    public void receiveInfo() {
        DataInputStream reader = null;
        try {
            reader = new DataInputStream(this.socket.getInputStream());
            String received = null;
            received = reader.readUTF();

            if (received.equals("channel ok?")) {
                DataOutputStream out = new DataOutputStream(this.socket.getOutputStream());
                out.writeUTF("ok");
            } else if (received.equals("ok")) {
                this.healthChannel.setHealth(true);
            } else if (received == null) {
                System.out.println("Content is null");
                this.ok = false;
            }

        } catch (IOException e) {
            System.out.println("Connection reset");
            this.ok = false;
        }
    }

    public void run() {
        while (this.ok) {
            try {
                receiveInfo();
            } catch (Exception e) {
                System.out.println("Recibiendo info" + e);
            }
        }
    }
}