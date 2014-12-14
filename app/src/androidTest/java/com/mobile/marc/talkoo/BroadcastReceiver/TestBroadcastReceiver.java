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


    @Override
    protected void setUp() throws Exception {
    }




    private class MockWifiDirectBroadcastListener implements WifiDirectBroadcastReceiver.WifiDirectBroadcastListener {
        public void onIsWifiEnabled(boolean wifi) {
        }

        public void onDeviceConnectedToPeers() {

        }

        public void onDisconnected() {

        }

        public void onPeersChangedAction() {

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