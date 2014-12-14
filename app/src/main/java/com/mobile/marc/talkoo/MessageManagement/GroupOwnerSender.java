package com.mobile.marc.talkoo.MessageManagement;

import android.app.ActivityManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.mobile.marc.talkoo.LoginActivity;
import com.mobile.marc.talkoo.Models.Message;
import com.mobile.marc.talkoo.NavigatorActivity;
import com.mobile.marc.talkoo.RoomActivity;
import com.mobile.marc.talkoo.MessageManagement.Thread.ServerInit;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GroupOwnerSender extends AsyncTask<Message, Message, Message> {
    private static final String     TAG = "GroupOwnerSender";
    private Context                 context_;
    private static final int        SERVER_PORT = 4447;
    private boolean                 is_owner_;
    private ArrayList<Socket>       sockets;

    public GroupOwnerSender(Context context, boolean owner){
        context_ = context;
        is_owner_ = owner;
    }

    protected Void sendMessageToClient(InetAddress address, Message message) {
        try {
            Socket socket = new Socket();
            socket.setReuseAddress(true);
            socket.bind(null);
            Log.v(TAG,"Connect to client: " + address.getHostAddress());
            socket.connect(new InetSocketAddress(address, SERVER_PORT));
            Log.v(TAG, "doInBackground: connect to "+ address.getHostAddress() +" succeeded");

            OutputStream outputStream = socket.getOutputStream();

            new ObjectOutputStream(outputStream).writeObject(message);

            Log.v(TAG, "doInBackground: write to "+ address.getHostAddress() +" succeeded");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Message sending failed");
        }
        return null;
    }

    @Override
    protected Message doInBackground(Message... message) {
        Log.v(TAG, "doInBackground");

        //Display le message on the sender before sending it
        publishProgress(message);

        //Send the message to clients
        ArrayList<InetAddress> listClients = ServerInit.clients;

        for (InetAddress address : listClients){
            if (message[0].getSenderAddress()!= null &&
                    address.getHostAddress().equals(message[0].getSenderAddress().getHostAddress())){
                return message[0];
            }
            sendMessageToClient(address, message[0]);
        }
        return message[0];
    }

    @Override
    protected void onProgressUpdate(Message... values) {
        super.onProgressUpdate(values);
        if (isActivityRunning(LoginActivity.class)) {
            RoomActivity.updateMessages(values[0], false);
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