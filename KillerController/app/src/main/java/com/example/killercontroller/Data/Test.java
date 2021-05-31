package com.example.killercontroller.Data;

import eu.cifpfbmoll.netlib.annotation.PacketAttribute;
import eu.cifpfbmoll.netlib.annotation.PacketType;

@PacketType("TEST")
public class Test {
    @PacketAttribute
    public String testing;
}
