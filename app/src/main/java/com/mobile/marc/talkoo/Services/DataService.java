//# CSIT 6000B    # Jordy Ngenze Domingos       20243311        jndomingos@ust.hk
//# CSIT 6000B    # Marc Lamberti               20243622        mlamberti@ust.hk

package com.mobile.marc.talkoo.Services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;


import com.mobile.marc.talkoo.BroadcastReceiver.WifiDirectBroadcastReceiver;
import com.mobile.marc.talkoo.NavigatorActivity;
import com.mobile.marc.talkoo.MessageManagement.Receiver;

/**
 * Created by Marc on 14/12/14.
 */
public class DataService extends Service {
    /*
    ** For testing purpose.
     */
    public static boolean client_created = false;
    public static boolean server_created = false;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId /*, boolean test*/) {
        System.out.println("onStartCommand");
        //Start the AsyncTask for the server to receive messages
        if (NavigatorActivity.isOwner) {
            System.out.println("Start the AsyncTask for the server to receive messages");
            new Receiver(getApplicationContext(), true)
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
            server_created = true;
        }
        else if (NavigatorActivity.isClient) {
            System.out.println("Start the AsyncTask for the client to receive messages");
            new Receiver(getApplicationContext(), false)
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
            client_created = true;
        }
        return START_STICKY;
    }
}
