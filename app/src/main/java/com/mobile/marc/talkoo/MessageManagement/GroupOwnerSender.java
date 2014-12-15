//# CSIT 6000B    # Jordy Ngenze Domingos       20243311        jndomingos@ust.hk
//# CSIT 6000B    # Marc Lamberti               20243622        mlamberti@ust.hk

package com.mobile.marc.talkoo.MessageManagement;

import android.app.ActivityManager;
import android.content.Context;
import android.os.AsyncTask;

import com.mobile.marc.talkoo.LoginActivity;
import com.mobile.marc.talkoo.MessageManagement.Thread.ServerThread;
import com.mobile.marc.talkoo.Models.Message;
import com.mobile.marc.talkoo.RoomActivity;

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
            socket.connect(new InetSocketAddress(address, SERVER_PORT));
            OutputStream outputStream = socket.getOutputStream();
            new ObjectOutputStream(outputStream).writeObject(message);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Display the message on the sender before sending it
     * and send it to the clients
     * @param message
     * @return
     */
    @Override
    protected Message doInBackground(Message... message) {
        publishProgress(message);
        ArrayList<InetAddress> listClients = ServerThread.clients;

        for (InetAddress address : listClients){
            if (message[0].getSenderAddress()!= null && address.getHostAddress().equals(message[0].getSenderAddress().getHostAddress())) {
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
            RoomActivity.updateMessages(values[0], !values[0].isOwner());
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