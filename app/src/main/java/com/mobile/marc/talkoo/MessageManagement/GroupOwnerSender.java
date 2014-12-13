package com.mobile.marc.talkoo.MessageManagement;

import android.app.ActivityManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.mobile.marc.talkoo.NavigatorActivity;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GroupOwnerSender extends AsyncTask<Message, Message, Message> {
    private static final String TAG = "GroupOwnerSender";
    private Context context_;
    private static final int CLIENT_PORT = 4446;
    private boolean isMine;

    public GroupOwnerSender(Context context, boolean mine){
        context_ = context;
        isMine = mine;
    }

    protected Void sendMessageToClient(InetAddress addr, Message message) {
        try {
            Socket socket = new Socket();
            socket.setReuseAddress(true);
            socket.bind(null);
            Log.v(TAG,"Connect to client: " + addr.getHostAddress());
            socket.connect(new InetSocketAddress(addr, CLIENT_PORT));
            Log.v(TAG, "doInBackground: connect to "+ addr.getHostAddress() +" succeeded");

            OutputStream outputStream = socket.getOutputStream();

            new ObjectOutputStream(outputStream).writeObject(message);

            Log.v(TAG, "doInBackground: write to "+ addr.getHostAddress() +" succeeded");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Erreur d'envoie du message");
        }
    }

    @Override
    protected Message doInBackground(Message... message) {
        Log.v(TAG, "doInBackground");

        //Display le message on the sender before sending it
        publishProgress(message);

        //Send the message to clients
        ArrayList<InetAddress> listClients = ServerInit.clients;
        for(InetAddress addr : listClients){

            if(message[0].getSenderAddress()!=null && addr.getHostAddress().equals(message[0].
                    getSenderAddress().getHostAddress())){
                return message[0];
            }
            sendMessageToClient(addr, message[0]);
        }
        return message[0];
    }

    @Override
    protected void onProgressUpdate(Message... values) {
        super.onProgressUpdate(values);

       // if(isActivityRunning(NavigatorActivity.class)){
         //   ChatActivity.refreshList(values[0], isMine);
        //}
    }

    @Override
    protected void onPostExecute(Message result) {
        Log.v(TAG, "onPostExecute");
        super.onPostExecute(result);
    }


//    @SuppressWarnings("rawtypes")
//    public Boolean isActivityRunning(Class activityClass)
//    {
//        ActivityManager activityManager = (ActivityManager) context_.getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

//        for (ActivityManager.RunningTaskInfo task : tasks) {
//            if (activityClass.getCanonicalName().equalsIgnoreCase(task.baseActivity.getClassName()))
//                return true;
//        }

//        return false;
//    }
}