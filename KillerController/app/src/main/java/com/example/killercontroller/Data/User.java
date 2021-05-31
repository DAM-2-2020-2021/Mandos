package com.example.killercontroller.Data;

import eu.cifpfbmoll.netlib.annotation.PacketAttribute;
import eu.cifpfbmoll.netlib.annotation.PacketType;

@PacketType("USER")
public class User {
    @PacketAttribute
    public String name = "";
    @PacketAttribute
    public String ip = "";
}
