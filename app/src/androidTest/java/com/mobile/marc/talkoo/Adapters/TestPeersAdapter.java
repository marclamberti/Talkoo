//# CSIT 6000B    # Jordy Ngenze Domingos       20243311        jndomingos@ust.hk
//# CSIT 6000B    # Marc Lamberti               20243622        mlamberti@ust.hk

package com.mobile.marc.talkoo.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.net.wifi.p2p.WifiP2pDevice;
import android.test.InstrumentationTestCase;
import android.test.mock.MockContext;
import android.widget.TextView;
import android.view.View;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

import com.mobile.marc.talkoo.R;

public class TestPeersAdapter extends InstrumentationTestCase {
    private List<WifiP2pDevice> peers_;
    private Context context_;
    private static int list_size_ = 1;
    private static int view_id_ = 1;
    private PeersAdapter adapter_;

    @Override
    protected void setUp() throws Exception {
        context_ = new MyMockContext();
        peers_ = new ArrayList<WifiP2pDevice>();

        for (int i = 0; i < list_size_; ++i) {
            WifiP2pDevice device = new WifiP2pDevice();
            device.deviceName = "Test";
            peers_.add(device);
        }
        adapter_ = new PeersAdapter(context_, view_id_, peers_);
    }

    /*
    **
     */
    public void testCount() throws Exception {
        assertEquals(adapter_.getCount(), list_size_);
    }

    /*
    **
    */
    public void testView() throws Exception {
        View view = adapter_.getView(0, null, null);
        TextView name = (TextView)view.findViewById(R.id.item_peer_name);
        assertEquals("Test", name.getText());
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