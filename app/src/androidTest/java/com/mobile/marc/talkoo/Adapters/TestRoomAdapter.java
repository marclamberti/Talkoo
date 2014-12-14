//# CSIT 6000B    # Jordy Ngenze Domingos       20243311        jndomingos@ust.hk
//# CSIT 6000B    # Marc Lamberti               20243622        mlamberti@ust.hk

package com.mobile.marc.talkoo.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.net.wifi.p2p.WifiP2pDevice;
import android.test.InstrumentationTestCase;
import android.test.mock.MockContext;
import android.view.View;
import android.widget.TextView;

import com.mobile.marc.talkoo.Models.Message;
import com.mobile.marc.talkoo.R;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class TestRoomAdapter extends InstrumentationTestCase {
    private List<Message> messages_;
    private Context context_;
    private static int list_size_ = 1;
    private RoomAdapter adapter_;
    private String message_text_;

    @Override
    protected void setUp() throws Exception {
        context_ = new MyMockContext();
        messages_ = new ArrayList<Message>();
        message_text_ = new String("Bonjour les amis.");

        for (int i = 0; i < list_size_; ++i) {
            String group_name = "Group 6";
            int id = 0;
            InetAddress address = InetAddress.getByName("127.0.0.1");
            Message message = new Message(Message.MESSAGE_TEXT, message_text_,
                    address, group_name, id);

            messages_.add(message);
        }
        adapter_ = new RoomAdapter(messages_, context_);
    }

    public void testCount() throws Exception {
        assertEquals(adapter_.getCount(), list_size_);
    }

    public void testView() throws Exception {

        View view = adapter_.getView(0, null, null);
        TextView messages_text_view = (TextView)view.findViewById(R.id.room_message_text);
        assertEquals(message_text_, messages_text_view.getText());
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