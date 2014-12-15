//# CSIT 6000B    # Jordy Ngenze Domingos       20243311        jndomingos@ust.hk
//# CSIT 6000B    # Marc Lamberti               20243622        mlamberti@ust.hk

package com.mobile.marc.talkoo;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.mobile.marc.talkoo.Adapters.RoomAdapter;
import com.mobile.marc.talkoo.BroadcastReceiver.WifiDirectBroadcastReceiver;
import com.mobile.marc.talkoo.MessageManagement.ClientSender;
import com.mobile.marc.talkoo.MessageManagement.GroupOwnerSender;
import com.mobile.marc.talkoo.Models.Message;
import com.mobile.marc.talkoo.BroadcastReceiver.WifiDirectBroadcastReceiver.WifiDirectBroadcastListener;
import com.mobile.marc.talkoo.Services.DataService;

import java.util.ArrayList;
import java.util.List;

public class RoomActivity extends Activity implements WifiDirectBroadcastListener {

    static private RoomAdapter              room_adapter_;
    static private List<Message>            messages_;
    private WifiP2pManager                  manager_;
    private WifiP2pManager.Channel          channel_;
    private WifiDirectBroadcastReceiver     receiver_;
    private IntentFilter                    intent_filter_;
    public boolean                          wifi_enabled;
    private String                          login_;
    private static ListView                 message_list_view_;

    public static final String              RESULT_EXTRA = "RESULT";

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

        // Start Data service
        startService(new Intent(this, DataService.class));
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
        message_list_view_ = (ListView)findViewById(R.id.message_list_view);
        message_list_view_.setAdapter(room_adapter_);
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
                endRoom();
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

    /**
     * Call when a user want to be disconnected from the group by pressing back
     */
    private void endRoom() {
        disconnectFromGroup();
        if (NavigatorActivity.server != null) {
            NavigatorActivity.server.interrupt();
        }
        manager_.cancelConnect(channel_, null);
        Intent returnIntent = new Intent();
        returnIntent.putExtra(RESULT_EXTRA, "Disconnected");
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    /**
     * Close the group formed by Wifi Direct between peers
     */
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

    /**
     * Used when the use click on the send button
     * @param view
     */
    public void sendMessageEvent(View view) {
        EditText room_message_edit_text = (EditText)findViewById(R.id.room_edit_message);
        if (room_message_edit_text != null && room_message_edit_text.getText().length() != 0) {
            Message message = new Message(Message.MESSAGE_TEXT,
                    room_message_edit_text.getText().toString(), null, login_, 0);
            if (NavigatorActivity.isOwner) {
                new GroupOwnerSender(this, true).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, message);
            } else if (NavigatorActivity.isClient) {
                new ClientSender(this, NavigatorActivity.owner_address).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, message);
            }
            room_message_edit_text.setText("");
        }
    }

    /**
     * Used to update the message list
     * It is called from async task when we receive a message
     */
    static public void updateMessages(Message message, boolean owner) {
        message.setOwner(owner);
        messages_.add(message);
        room_adapter_.notifyDataSetChanged();
        message_list_view_.setSelection(messages_.size() - 1);
        System.out.println("updateMessages");
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

    @Override
    /**
     * Called when the peers are disconnected
     * Close the group, stop the server and send back some information to the Navigator Activity
     * to indicate the disconnection
     */
    public void onDisconnected() {
        Toast.makeText(RoomActivity.this, "Your have been disconnected", Toast.LENGTH_LONG).show();
        disconnectFromGroup();
        if (NavigatorActivity.server != null) {
            NavigatorActivity.server.interrupt();
        }
        Intent returnIntent = new Intent();
        returnIntent.putExtra(RESULT_EXTRA, "Disconnected");
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onPeersChangedAction() {
        // ...
    }

}
