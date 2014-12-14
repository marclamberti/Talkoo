//# CSIT 6000B    # Jordy Ngenze Domingos       20243311        jndomingos@ust.hk
//# CSIT 6000B    # Marc Lamberti               20243622        mlamberti@ust.hk

package com.mobile.marc.talkoo.BroadcastReceiver;

import android.content.Context;
import android.content.res.Resources;
import android.test.InstrumentationTestCase;
import android.test.mock.MockContext;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

public class TestBroadcastReceiver extends InstrumentationTestCase {

    private WifiDirectBroadcastReceiver receiver_;
    private MockWifiDirectBroadcastListener listener_;
    private MyMockContext context_;
    private Intent intent_;

    @Override
    protected void setUp() throws Exception {
        context_ = new MyMockContext();
        listener_ = new MockWifiDirectBroadcastListener();
        receiver_ = new WifiDirectBroadcastReceiver();
        receiver_.listener(listener_);
    }

    public void testOnReceiveWifiEnable() {
        intent_ = new Intent(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intent_.putExtra(WifiP2pManager.EXTRA_WIFI_STATE, WifiP2pManager.WIFI_P2P_STATE_ENABLED);
        receiver_.onReceive(context_, intent_);
        assertTrue(listener_.getOnIsWifiEnable());
    }

    public class MockIntent extends Intent {
        public int getIntExtra(String name, int defaultValue) {
            return WifiP2pManager.WIFI_P2P_STATE_ENABLED;
        }
    }

    private class MockWifiDirectBroadcastListener implements WifiDirectBroadcastReceiver.WifiDirectBroadcastListener {

        private boolean onIsWifiEnabled_ = false;
        private boolean onDeviceConnectedToPeers_ = false;
        private boolean onDisconnected_ = false;
        private boolean onPeersChangedAction_ = false;

        public boolean getOnIsWifiEnable() {
            return onIsWifiEnabled_;
        }

        public boolean getOnDeviceConnectedToPeers() {
            return onDeviceConnectedToPeers_;
        }

        public boolean getOnDisconnected() {
            return onDisconnected_;
        }

        public boolean getOPeersChangedAction() {
            return onPeersChangedAction_;
        }

        public void onIsWifiEnabled(boolean wifi) {
            onIsWifiEnabled_ = true;
        }

        public void onDeviceConnectedToPeers() {
            onDeviceConnectedToPeers_ = true;
        }

        public void onDisconnected() {
            onDisconnected_ = true;
        }

        public void onPeersChangedAction() {
            onPeersChangedAction_ = true;
        }
    }

    private class MyMockContext extends MockContext {

        @Override
        public Resources getResources() {
            return getInstrumentation().getTargetContext().getResources();
        }

        @Override
        public Resources.Theme getTheme() {
            return getInstrumentation().getTargetContext().getTheme();
        }

        @Override
        public Object getSystemService(String name) {
            if (Context.LAYOUT_INFLATER_SERVICE.equals(name)) {
                return getInstrumentation().getTargetContext().getSystemService(name);
            }
            return super.getSystemService(name);
        }
    }

}