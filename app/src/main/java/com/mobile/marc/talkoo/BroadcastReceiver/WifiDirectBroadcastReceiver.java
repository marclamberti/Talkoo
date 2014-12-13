//# CSIT 6000B    # Jordy Ngenze Domingos       20243311        jndomingos@ust.hk
//# CSIT 6000B    # Marc Lamberti               20243311        mlamberti@ust.hk

package com.mobile.marc.talkoo.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import java.nio.channels.Channel;

/**
 * Created by Marc on 21/10/14.
 */
public class WifiDirectBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "WifiDirectBroadcastReceiver";
    private static WifiDirectBroadcastReceiver instance_;

    // Used for getting intent
    private WifiP2pManager  manager_;
    // Activity dealing with intent
    private WifiDirectBroadcastListener   listener_;

    public WifiDirectBroadcastReceiver() {
        super();
    }

    /**
     * Singleton pattern used to avoid multiple instances of it
     * @return
     */
    public static WifiDirectBroadcastReceiver createInstance(){
        if(instance_ != null) {
            return instance_;
        }
        instance_ = new WifiDirectBroadcastReceiver();
        return instance_;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String  action = intent.getAction();

        /**
         * Check if wifi is enabled and notify appropriate activity
         */
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            Log.v(TAG, "WIFI_P2P_STATE_CHANGED_ACTION");
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (listener_ != null) {
                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                    listener_.onIsWifiEnabled(true);
                } else if (state == WifiP2pManager.WIFI_P2P_STATE_DISABLED) {
                    listener_.onIsWifiEnabled(false);
                }
            }
        }

        /**
         * The peers list has changed (not used)
         */
        else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            Log.v(TAG, "WIFI_P2P_PEERS_CHANGED_ACTION");
        }

        /**
         * Respond to a new connection or disconnection
         */
        else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

            Log.v(TAG, "WIFI_P2P_CONNECTION_CHANGED_ACTION");

            if (manager_ == null || listener_ == null) {
                return;
            }
            // Check if we are connected with a peer
            NetworkInfo network_info = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            if (network_info.isConnected()) {
                System.out.println("Connection ok");
                listener_.onDeviceConnectedToPeers();
            }
        }

        /**
         * Handle the wifi state's device when it changes
         */
        else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            Log.v(TAG, "WIFI_P2P_THIS_DEVICE_CHANGED_ACTION");
        }
    }

    /**
     * Setters
     */
    public void manager(WifiP2pManager manager) {
        manager_ = manager;
    }

    public void listener(WifiDirectBroadcastListener listener) {
        listener_ = listener;
    }

    /**
     * Interface
     */
    public interface WifiDirectBroadcastListener {
        public void onIsWifiEnabled(boolean wifi);
        public void onDeviceConnectedToPeers();
    }
}
