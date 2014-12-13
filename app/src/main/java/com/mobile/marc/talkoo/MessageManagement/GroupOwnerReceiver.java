//# CSIT 6000B    # Jordy Ngenze Domingos       20243311        jndomingos@ust.hk
//# CSIT 6000B    # Marc Lamberti               20243311        mlamberti@ust.hk

package com.mobile.marc.talkoo.MessageManagement;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import android.content.Context;

/*
** Handle the messages received by the group owner.
 */
public class GroupOwnerReceiver extends NotificationHandler {
    private static final String TAG = "GroupOwnerReceiver";
    private static final int SERVER_PORT = 4242;
    private Context context_;
    private ServerSocket server_socket_;

    /**
     *  Constructor.
     */
    public GroupOwnerReceiver(Context context) {
        context_ = context;
    }

    private Message extractMessageFromClientSocket(Socket client_socket) {
        try {
            InputStream inputStream = client_socket.getInputStream();
            ObjectInputStream objectIS = new ObjectInputStream(inputStream);
            return (Message) objectIS.readObject();
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    /*
    ** Basic server implementation that waits for requests to come in over the network and triggers
    ** an UI update the information received.
     */
    private Void runServer() {
        try {
            server_socket_ = new ServerSocket(SERVER_PORT);
            while (true) {
                Socket client_socket = server_socket_.accept();
//
                Message client_message = extractMessageFromClientSocket(client_socket);
                if (client_message == null)
                    continue;

                //Add the InetAdress of the sender to the message
                InetAddress sender_address = client_socket.getInetAddress();
                client_message.setSenderAddress(sender_address);

                client_socket.close();
                publishProgress(client_message);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    protected Void doInBackground(Void... params) {
        runServer();
        return null;
    }

    @Override
    protected void onCancelled() {
        try {
            server_socket_.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        super.onCancelled();
    }

    @Override
    protected void onProgressUpdate(Message... values) {
        super.onProgressUpdate(values);
        Notify(context_, values[0]);

        //If the message contains a video or an audio, we saved this file to the external storage
        int type = values[0].getMessageType();
        if (type == Message.MESSAGE_AUDIO || type == Message.MESSAGE_VIDEO
                || type == Message.MESSAGE_FILE) {
            values[0].saveByteArrayToFile(context_);
        }

       // new SendMessageServer(mContext, false).executeOnExecutor(THREAD_POOL_EXECUTOR, values);
    }
}
