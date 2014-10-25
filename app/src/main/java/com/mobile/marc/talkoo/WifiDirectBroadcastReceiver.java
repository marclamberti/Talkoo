package com.mobile.marc.talkoo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.util.Log;

/**
 * Created by Marc on 21/10/14.
 */
public class WifiDirectBroadcastReceiver extends BroadcastReceiver {
    // Used for getting intent
    private WifiP2pManager      manager_;
    // Channel used for connecting the application to the Wifi P2P framework
    private Channel             channel_;
    // Activity dealing with intent
    private WifiDirectBroadcastListener   listener_;

    public WifiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel, WifiDirectBroadcastListener listener) {
        super();
        manager_ = manager;
        channel_ = channel;
        listener_ = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String  action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check if wifi is enabled and notify appropriate activity
            System.out.println("WIFI_P2P_STATE_CHANGED_ACTION");
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                listener_.onIsWifiEnabled(true);
            }
            else if (state == WifiP2pManager.WIFI_P2P_STATE_DISABLED) {
                listener_.onIsWifiEnabled(false);
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
            System.out.println("WIFI_P2P_PEERS_CHANGED_ACTION");
            //activity_.onDiscoveringPeersRequest();
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to a new connection or disconnection
            System.out.println("WIFI_P2P_CONNECTION_CHANGED_ACTION");

            if (manager_ == null) {
                return;
            }
            // Check if we are connected with a peer
            NetworkInfo network_info = (NetworkInfo)intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            if (network_info.isConnected()) {
                listener_.onDeviceConnectedToPeers();
                System.out.println("Connected");
                // start chat there
            } else {
                listener_.onDeviceDisconnectedToPeers();
                System.out.println("Disconnected");
                // reset data there
            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
            System.out.println("WIFI_P2P_THIS_DEVICE_CHANGED_ACTION");
        }
    }

    public interface WifiDirectBroadcastListener {
        public void onIsWifiEnabled(boolean wifi);
        public void onDeviceConnectedToPeers();
        public void onDeviceDisconnectedToPeers();
    }
}
