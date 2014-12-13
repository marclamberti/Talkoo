//# CSIT 6000B    # Jordy Ngenze Domingos       20243311        jndomingos@ust.hk
//# CSIT 6000B    # Marc Lamberti               20243311        mlamberti@ust.hk

package com.mobile.marc.talkoo;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.Fragment;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import com.mobile.marc.talkoo.BroadcastReceiver.WifiDirectBroadcastReceiver;
import com.mobile.marc.talkoo.Fragments.FragmentFactory;
import com.mobile.marc.talkoo.Fragments.NavigationDrawerFragment;
import com.mobile.marc.talkoo.Fragments.PeersFragment;
import com.mobile.marc.talkoo.Fragments.PeersFragment.PeersListener;
import com.mobile.marc.talkoo.Fragments.SettingsFragment.SettingsListener;
import com.mobile.marc.talkoo.Fragments.HomeFragment.HomeListener;
import com.mobile.marc.talkoo.Fragments.NavigationDrawerFragment.NavigationDrawerCallbacks;
import com.mobile.marc.talkoo.BroadcastReceiver.WifiDirectBroadcastReceiver.WifiDirectBroadcastListener;
import com.mobile.marc.talkoo.Fragments.RoomFragment.RoomListener;
import com.mobile.marc.talkoo.Services.WifiDirectLocalService;

/**
 * TODO: Detect when a connection is refused
 * TODO: Actualize the list on device periodically
 */
public class NavigatorActivity extends FragmentActivity implements HomeListener, PeersListener, SettingsListener,
        NavigationDrawerCallbacks, WifiDirectBroadcastListener, RoomListener {

    /**
     * Progress dialog
     */
    private ProgressDialog  progress_dialog_ = null;

    /**
     * Wifi P2P
     */
    private WifiP2pManager          manager_;
    private Channel                 channel_;
    private BroadcastReceiver       receiver_;
    private IntentFilter            intent_filter_;
    public  boolean                 discovering_peers_progress = false;
    private WifiDirectLocalService  service_;
    public boolean                  wifiEnabled;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment navigation_drawer_fragment_;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence title_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigator);

        navigation_drawer_fragment_ = (NavigationDrawerFragment)getFragmentManager().findFragmentById(R.id.navigation_drawer);
        title_ = getTitle();

        // Set up the drawer.
        navigation_drawer_fragment_.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        // Get the intent and the login parameter
        Intent intent = getIntent();
        String login = intent.getStringExtra(LoginActivity.EXTRA_LOGIN);

        initWifiDirect(login);
    }

    @Override
    protected void onResume() {
        super.onResume();
        receiver_ = new WifiDirectBroadcastReceiver(manager_, this);
        registerReceiver(receiver_, intent_filter_);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver_);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        service_.removeLocalService();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        goToFragment(position, null);
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                title_ = getString(R.string.home_title);
                break;
            case 1:
                title_ = getString(R.string.peers_title);
                break;
            case 2:
                title_ = getString(R.string.settings_title);
                break;
            case 3:
                title_ = getString(R.string.room_title);
                break;
        }
    }

    private Fragment goToFragment(int id, Parcelable object) {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment        fragment = FragmentFactory.newInstance(id, object);
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment, "fragment" + id)
                .commit();
        return fragment;
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(title_);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!navigation_drawer_fragment_.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.

            // Display the refresh icon in the action bar only if it is the peer's fragment
            PeersFragment peers_fragment = (PeersFragment)getFragmentManager().findFragmentByTag(PeersFragment.TAG);
            if (peers_fragment != null && peers_fragment.isVisible()) {
                getMenuInflater().inflate(R.menu.home, menu);
                if (discovering_peers_progress) {
                    actionMenuItemSelected(menu.findItem(R.id.action_refresh));
                }
            }
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        actionMenuItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    private void actionMenuItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_refresh:
                ((PeersFragment)getFragmentManager().findFragmentByTag(PeersFragment.TAG)).refreshPeersList(item);
                break;
        }
    }

    /**
     * Wifi P2P functions
     */
    private void initWifiDirect(String login) {
        manager_ = (WifiP2pManager)getSystemService(Context.WIFI_P2P_SERVICE);
        channel_ = manager_.initialize(this, getMainLooper(), null);

        // Intent filter
        intent_filter_ = new IntentFilter();
        intent_filter_.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intent_filter_.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intent_filter_.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intent_filter_.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        // Init local service
        service_ = new WifiDirectLocalService(manager_, channel_, this, login);
    }

    private String errorCode(int code) {
        switch (code) {
            case WifiP2pManager.BUSY:
                return "BUSY";
            case WifiP2pManager.ERROR:
                return "ERROR";
            case WifiP2pManager.NO_SERVICE_REQUESTS:
                return "NO SERVICE REQUESTS";
            case WifiP2pManager.P2P_UNSUPPORTED:
                return "WIFI P2P UNSUPPORTED";
            default:
                return "Unknown";
        }
    }

    public void dismissProgressDialog() {
        if (progress_dialog_ != null && progress_dialog_.isShowing()) {
            progress_dialog_.dismiss();
        }
    }

    /**
     * Methods implemented from PeersListener
     */

    @Override
    public void onDiscoveringPeersRequest() {
        discovering_peers_progress = true;
        service_.discoveryRequest();
    }

    @Override
    public void onDiscoveringPeersRequestDone() {
        discovering_peers_progress = false;
    }

    @Override
    public boolean onIsWifiConnected() {
        return wifiEnabled;
    }

    @Override
    public void onConnectToPeer(WifiP2pDevice peer) {
        WifiP2pConfig   config = new WifiP2pConfig();
        config.deviceAddress = peer.deviceAddress;
        config.wps.setup = WpsInfo.PBC;
        dismissProgressDialog();

        // Progress dialog waiting for a connection
        // If the user press back, we cancel the connection
        progress_dialog_ = ProgressDialog.show(NavigatorActivity.this, "Press back to cancel", "Connection to: " + peer.deviceName, true, true, new DialogInterface.OnCancelListener(){
            @Override
            public void onCancel(DialogInterface dialog) {
                manager_.cancelConnect(channel_, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(NavigatorActivity.this, "Connection aborted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int i) {
                        Toast.makeText(NavigatorActivity.this, "Connect abort request failed: " + errorCode(i), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Connection to the peer
        manager_.connect(channel_, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // We will be notified by the broadcast that we are connected
            }
            @Override
            public void onFailure(int i) {
                dismissProgressDialog();
                Toast.makeText(NavigatorActivity.this, "Connection failed: " + errorCode(i), Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Methods implemented from WifiDirectBroadcastListener
     */

    @Override
    public void onIsWifiEnabled(boolean wifi) {
        wifiEnabled = wifi;
    }

    @Override
    public void onDeviceConnectedToPeers() {
        dismissProgressDialog();
        manager_.requestConnectionInfo(channel_, new WifiP2pManager.ConnectionInfoListener() {
            /**
             *  onConnectionInfoAvailable() callback will notify you when the state of the connection changes.
             *  In cases where multiple devices are going to be connected to a single device (like a game with 3 or more players, or a chat app),
             *  one device will be designated the "group owner".
             *  Callback from requestConnectionInfo
             */
            @Override
            public void onConnectionInfoAvailable(final WifiP2pInfo info) {
                // After group negotiation, we can determine the group owner
                if (info.groupFormed && info.isGroupOwner) {
                    // Do whatever tasks are specific to the group owner.
                    // One common case is creating a server thread and accepting
                    // incoming connections.
                } else if (info.groupFormed) {
                    // The other device acts as the client. In this case,
                    // you'll want to create a client thread that connects to the group
                    // owner.
                }

            }
        });
    }

    /**
     * Methods implemented from OnRoomListener
     */
}
