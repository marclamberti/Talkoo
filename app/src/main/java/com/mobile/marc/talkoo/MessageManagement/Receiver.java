//# CSIT 6000B    # Jordy Ngenze Domingos       20243311        jndomingos@ust.hk
//# CSIT 6000B    # Marc Lamberti               20243311        mlamberti@ust.hk

package com.mobile.marc.talkoo.MessageManagement;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import android.app.ActivityManager;
import android.content.Context;

import com.mobile.marc.talkoo.NavigatorActivity;

/*
** Handle the messages received by the group owner.
 */
public class Receiver extends NotificationHandler {
    private static final String TAG = "Receiver";
    private static final int SERVER_PORT = 4242;
    private static final int CLIENT_PORT = 4243;
    private boolean is_server_;
    private Context context_;
    private ServerSocket server_socket_;

    /**
     *  Constructor.
     */
    public Receiver(Context context, boolean is_server) {
        context_ = context;
        is_server_ = is_server;
    }

    private Message extractMessageFromSocket(Socket socket) {
        try {
            InputStream inputStream = socket.getInputStream();
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
            if (is_server_)
                server_socket_ = new ServerSocket(SERVER_PORT);
            else
                server_socket_ = new ServerSocket(CLIENT_PORT);

            while (true) {
                Socket socket = server_socket_.accept();
//
                Message client_message = extractMessageFromSocket(socket);
                if (client_message == null)
                    continue;

                //Add the InetAdress of the sender to the message
                if (is_server_) {
                    InetAddress sender_address = socket.getInetAddress();
                    client_message.setSenderAddress(sender_address);
                }
                socket.close();
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

        //if (is_server_) {}
        // new SendMessageServer(mContext, false).executeOnExecutor(THREAD_POOL_EXECUTOR, values);
        //else {
        //if(isActivityRunning(NavigatorActivity.class)){
          //  ChatActivity.refreshList(values[0], false);
        //}
    }

    /*
    ** http://stackoverflow.com/questions/5446565/android-how-do-i-check-if-activity-is-running
    */
    public Boolean isActivityRunning(Class activityClass) {
        ActivityManager activityManager =
                (ActivityManager) context_.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (activityClass.getCanonicalName().equalsIgnoreCase(task.baseActivity.getClassName()))
                return true;
        }

        return false;
    }
}
