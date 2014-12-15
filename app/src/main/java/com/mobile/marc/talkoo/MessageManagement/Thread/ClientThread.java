//# CSIT 6000B    # Jordy Ngenze Domingos       20243311        jndomingos@ust.hk
//# CSIT 6000B    # Marc Lamberti               20243622        mlamberti@ust.hk

package com.mobile.marc.talkoo.MessageManagement.Thread;

import android.util.Log;
import android.widget.Toast;

import com.mobile.marc.talkoo.NavigatorActivity;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientThread extends Thread {
    private static final int SERVER_PORT = 4444;
    private InetAddress server_address_;

    public ClientThread(InetAddress server_address){
        server_address_ = server_address;
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
                    //System.out.println("TRY CONNECTING...");
                    socket.connect(new InetSocketAddress(server_address_, SERVER_PORT));
                    //System.out.println("Connected...");
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
