//# CSIT 6000B    # Jordy Ngenze Domingos       20243311        jndomingos@ust.hk
//# CSIT 6000B    # Marc Lamberti               20243622        mlamberti@ust.hk

package com.mobile.marc.talkoo.MessageManagement;

import android.app.ActivityManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.mobile.marc.talkoo.LoginActivity;
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
            OutputStream outputStream = socket.getOutputStream();
            new ObjectOutputStream(outputStream).writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null && socket.isConnected()) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     *  Update the sender message view before sending it.
     */
    @Override
    protected Message doInBackground(Message... msg) {
        publishProgress(msg);
        sendMessageToServer(msg[0]);
        return msg[0];
    }

    @Override
    protected void onProgressUpdate(Message... msg) {
        super.onProgressUpdate(msg);
        if(isActivityRunning(LoginActivity.class)){
            RoomActivity.updateMessages(msg[0], true);
        }
    }

    @Override
    protected void onPostExecute(Message result) {
        super.onPostExecute(result);
    }

    @SuppressWarnings("rawtypes")
    public Boolean isActivityRunning(Class activityClass) {
        ActivityManager activityManager =
                (ActivityManager) context_.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks =
                activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (activityClass.getCanonicalName().equalsIgnoreCase(task.baseActivity.getClassName()))
               return true;
        }
        return false;
    }
}
