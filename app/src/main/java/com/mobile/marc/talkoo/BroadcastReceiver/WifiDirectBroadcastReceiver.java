//# CSIT 6000B    # Jordy Ngenze Domingos       20243311        jndomingos@ust.hk
//# CSIT 6000B    # Marc Lamberti               20243311        mlamberti@ust.hk

package com.mobile.marc.talkoo.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

/**
 * Created by Marc on 21/10/14.
 */
public class WifiDirectBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "WifiDirectBroadcastReceiver";

    // Used for getting intent
    private WifiP2pManager      manager_;
    // Activity dealing with intent
    private WifiDirectBroadcastListener   listener_;

    public WifiDirectBroadcastReceiver(WifiP2pManager manager, WifiDirectBroadcastListener listener) {
        super();
        manager_ = manager;
        listener_ = listener;
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
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                listener_.onIsWifiEnabled(true);
            }
            else if (state == WifiP2pManager.WIFI_P2P_STATE_DISABLED) {
                listener_.onIsWifiEnabled(false);
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

            if (manager_ == null) {
                return;
            }
            // Check if we are connected with a peer
            NetworkInfo network_info = (NetworkInfo)intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            if (network_info.isConnected()) {
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
     * Interface
     */
    public interface WifiDirectBroadcastListener {
        public void onIsWifiEnabled(boolean wifi);
        public void onDeviceConnectedToPeers();
    }
}
