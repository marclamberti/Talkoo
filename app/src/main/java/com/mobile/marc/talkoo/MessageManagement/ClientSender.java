package com.mobile.marc.talkoo.MessageManagement;

import android.app.ActivityManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.mobile.marc.talkoo.Models.Message;
import com.mobile.marc.talkoo.NavigatorActivity;
import com.mobile.marc.talkoo.RoomActivity;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

public class ClientSender extends AsyncTask<Message, Message, Message> {
    private static final String TAG = "ClientSender";
    private Context context_;
    private static final int SERVER_PORT = 4445;
    private InetAddress server_address_;

    public ClientSender(Context context, InetAddress server_address){
        context_ = context;
        server_address_ = server_address;
    }

    protected Void sendMessageToServer(Message message) {
        Socket socket = new Socket();
        try {
            socket.setReuseAddress(true);
            socket.bind(null);
            socket.connect(new InetSocketAddress(server_address_, SERVER_PORT));
            Log.v(TAG, "doInBackground: connect succeeded");

            OutputStream outputStream = socket.getOutputStream();

            new ObjectOutputStream(outputStream).writeObject(message);

            Log.v(TAG, "doInBackground: send message succeeded");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Message sending failed");
        } finally {
            if (socket != null) {
                if (socket.isConnected()) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    @Override
    protected Message doInBackground(Message... msg) {
        Log.v(TAG, "doInBackground");

        //Display le message on the sender before sending it
        publishProgress(msg);

        //Send the message
        sendMessageToServer(msg[0]);

        return msg[0];
    }

    @Override
    protected void onProgressUpdate(Message... msg) {
        super.onProgressUpdate(msg);
        System.out.println("onProgressUpdate Client 1");
        if(isActivityRunning(RoomActivity.class)){
            System.out.println("onProgressUpdate Client 2");
            RoomActivity.updateMessages(msg[0], true);
        }
    }

    @Override
    protected void onPostExecute(Message result) {
        Log.v(TAG, "onPostExecute");
        super.onPostExecute(result);
    }

    @SuppressWarnings("rawtypes")
    public Boolean isActivityRunning(Class activityClass) {
        ActivityManager activityManager = (ActivityManager) context_.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (activityClass.getCanonicalName().equalsIgnoreCase(task.baseActivity.getClassName()))
               return true;
        }
        return false;
    }
}
