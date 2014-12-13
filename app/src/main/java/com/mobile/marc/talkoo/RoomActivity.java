//# CSIT 6000B    # Jordy Ngenze Domingos       20243311        jndomingos@ust.hk
//# CSIT 6000B    # Marc Lamberti               20243311        mlamberti@ust.hk

package com.mobile.marc.talkoo;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.marc.talkoo.Adapters.RoomAdapter;
import com.mobile.marc.talkoo.BroadcastReceiver.WifiDirectBroadcastReceiver;
import com.mobile.marc.talkoo.MessageManagement.Message;
import com.mobile.marc.talkoo.Services.WifiDirectLocalService;
import com.mobile.marc.talkoo.BroadcastReceiver.WifiDirectBroadcastReceiver.WifiDirectBroadcastListener;
import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.List;

// TODO: Check, when a client discover a peer with a given name, the peer discovered goes back to home and choose another name then find peers.
// TODO: When he find a peer and try to connect it, when they are connected, the original client does not show the room activity because he has
// TODO: kept the previous login at the first discovery
public class RoomActivity extends Activity implements WifiDirectBroadcastListener {

    private RoomAdapter                     room_adapter_;
    private List<Message>                   messages_;
    private WifiP2pManager                  manager_;
    private WifiP2pManager.Channel          channel_;
    private WifiDirectBroadcastReceiver     receiver_;
    private IntentFilter                    intent_filter_;
    public boolean                          wifi_enabled;
    private String                          login_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Intent intent = getIntent();
        login_ = intent.getStringExtra(LoginActivity.EXTRA_LOGIN);

        // Disable the arrow to go back in the action bar
        ActionBar   actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        // Wifi direct initialization
        initWifiDirect();

        // Adapter initialization
        initAdapter();
    }

    /**
     * Wifi P2P initialization
     */
    private void initWifiDirect() {
        manager_ = (WifiP2pManager)getSystemService(Context.WIFI_P2P_SERVICE);
        channel_ = manager_.initialize(this, getMainLooper(), null);

        // Broadcast
        receiver_ = WifiDirectBroadcastReceiver.createInstance();
        receiver_.manager(manager_);
        receiver_.listener(this);

        // Intent filter
        intent_filter_ = new IntentFilter();
        intent_filter_.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intent_filter_.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intent_filter_.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intent_filter_.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    /**
     * Adapter initialization
     */
    private void initAdapter() {
        messages_ = new ArrayList<Message>();
        room_adapter_ = new RoomAdapter(messages_, this);
        ListView message_list_view = (ListView)findViewById(R.id.message_list_view);
        message_list_view.setAdapter(room_adapter_);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_room, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(receiver_, intent_filter_);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver_);
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You are going to be disconnected of the group. If you are the owner, everybody will be also disconnected. Are you sure to leave?")
               .setTitle("Close room");
        builder.setPositiveButton(R.string.room_alert_dialog_disconnect_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                disconnectFromGroup();
                finish();
                // TODO: If server or client, it would be stop as well
            }
        });
        builder.setNegativeButton(R.string.room_alert_dialog_disconnect_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    // Disconnect from the group
    private void disconnectFromGroup() {
        manager_.removeGroup(channel_, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                System.out.println("Disconnection succeed");
            }

            @Override
            public void onFailure(int i) {
                System.out.println("Disconnection failed");
            }
        });
    }

    // OnClick event on send button
    public void sendMessageEvent(View view) {
        EditText room_message_edit_text = (EditText)findViewById(R.id.room_edit_message);
        if (room_message_edit_text == null) {
            System.out.println("Null");
        }
        if (room_message_edit_text != null && room_message_edit_text.getText().length() != 0) {
            Toast.makeText(this, "message: " + room_message_edit_text.getText(), Toast.LENGTH_SHORT).show();
            // Send message here
            System.out.println(room_message_edit_text.getText());
        }
    }

    /**
     * Used to update the message list
     * It is called from async task when we receive a message
     */
    public void updateMessages(Message message, boolean owner) {
        message.setOwner(owner);

        /*
        messages_.add(message);
        room_adapter_.notifyDataSetChanged();
        ListView message_list_view = (ListView)findViewById(R.id.message_list_view);
        message_list_view.setSelection(messages_.size() - 1);
         */
    }

    /**
     * WifiDirectBroadcastListener's interface implementation
     */
    @Override
    public void onIsWifiEnabled(boolean wifi) {
        wifi_enabled = wifi;
    }

    @Override
    public void onDeviceConnectedToPeers() {
    }
}
