package com.example.killercontroller.Communication;

import eu.cifpfbmoll.netlib.annotation.PacketAttribute;
import eu.cifpfbmoll.netlib.annotation.PacketType;

@PacketType("INFO")
public class Message {

    @PacketAttribute
    private String messageType = "";

    @PacketAttribute
    private String message = "";

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
