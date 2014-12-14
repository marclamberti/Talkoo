package com.mobile.marc.talkoo.MessageManagement.Thread;

import android.util.Log;
import android.widget.Toast;

import com.mobile.marc.talkoo.NavigatorActivity;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientInit extends Thread {
    private static final int SERVER_PORT = 4444;
    private InetAddress server_address_;

    public ClientInit(InetAddress serverAddress){
        server_address_ = serverAddress;
    }

    @Override
    public void run() {
        int count = 0;
        int maxTries = 3;
        Socket socket = new Socket();
        try {
            socket.bind(null);
            while (!socket.isConnected()) {
                try {
                    System.out.println("TRY CONNECTING...");
                    socket.connect(new InetSocketAddress(server_address_, SERVER_PORT));
                    System.out.println("Connected...");
                } catch (IOException e) {
                    e.printStackTrace();
                    if (++count == maxTries) {
                        throw e;
                    }
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
