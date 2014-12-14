package com.mobile.marc.talkoo.MessageManagement.Thread;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientInit extends Thread {
    private static final int SERVER_PORT = 4444;
    private InetAddress server_address_;

    public ClientInit(InetAddress server_address){
        server_address_ = server_address;
    }

    @Override
    public void run() {
        Socket socket = new Socket();
        try {
            socket.bind(null);
            socket.connect(new InetSocketAddress(server_address_, SERVER_PORT));
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
