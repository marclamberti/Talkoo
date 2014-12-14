//# CSIT 6000B    # Jordy Ngenze Domingos       20243311        jndomingos@ust.hk
//# CSIT 6000B    # Marc Lamberti               20243622        mlamberti@ust.hk

package com.mobile.marc.talkoo.Services;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.DnsSdTxtRecordListener;
import android.net.wifi.p2p.WifiP2pManager.DnsSdServiceResponseListener;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.widget.Toast;

import com.mobile.marc.talkoo.Fragments.PeersFragment;
import com.mobile.marc.talkoo.NavigatorActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * This class allow us to discover the services of nearby devices directly
 * without being connected to the network. We cal also advertise the services
 * running on our device.
 * These capabilities help to communicate between apps.
 */
public class WifiDirectLocalService implements DnsSdTxtRecordListener, DnsSdServiceResponseListener {

    public static final String INSTANCE_NAME = "_talkoo";
    private static final int SERVER_PORT = 4445;

    private WifiP2pManager                  manager_;
    private Channel                         channel_;
    private NavigatorActivity               activity_;
    private String                          login_;
    private WifiP2pDnsSdServiceInfo         service_info_;
    public final HashMap<String, String>    peers = new HashMap<String, String>();

    public WifiDirectLocalService(WifiP2pManager manager, Channel channel, NavigatorActivity activity, String login) {
        manager_ = manager;
        channel_ = channel;
        activity_ = activity;
        login_ = login;

        startRegistration();
    }

    /**
     * Register a local service for service discovery.
     * Once it is registered, the framework automatically responds to service discovery requests
     * from peers
     */
    private void startRegistration() {
        // Create a string map containing information about this device
        Map device = new HashMap();
        device.put("peer_listen_port", String.valueOf(SERVER_PORT));
        device.put("peer_name", login_); // Put the login there
        device.put("peer_available", "visible");

        // Service information, instance name, service type, and the information about this device
        service_info_ = WifiP2pDnsSdServiceInfo.newInstance(INSTANCE_NAME, "_presence._tcp", device);

        // Add the local service
        manager_.addLocalService(channel_, service_info_, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                System.out.println("Local service registered");
                manager_.setDnsSdResponseListeners(channel_, WifiDirectLocalService.this, WifiDirectLocalService.this);
            }

            @Override
            public void onFailure(int i) {
                System.out.println("Local service registration failed: " + errorCode(i));
            }
        });
    }

    /**
     * Discover nearby services
     * We set up a  WifiP2pManager.DnsSdTxtRecordListener to listen for incoming peers.
     * These peers can be optionally be broadcast by other devices. When one comes in
     * we copy the information such as device address and name, then we add them to the HashMap
     * A service listener is also set up to receive the description and connection information
     */
    @Override
    public void onDnsSdTxtRecordAvailable(String full_domain, Map peer, WifiP2pDevice device) {
        System.out.println("DnsSdTxtRecord available - " + peer.toString());
        // If the DnsSdTxtRecord doesn't belong to talkoo we do nothing
        if (!peer.containsKey("peer_name") || !peer.containsKey("peer_listen_port")) {
            return;
        }
        peers.put(device.deviceAddress, peer.get("peer_name").toString());
    }

    @Override
    public void onDnsSdServiceAvailable(String instance_name, String registration_type, WifiP2pDevice peer) {
        System.out.println("onDnsSdServiceAvailable: " + instance_name);
        // Check if the peer comes from talkoo
        if (!instance_name.equals(INSTANCE_NAME)) {
            return;
        }
        // Change the device name with its actual login
        if (peers.containsKey(peer.deviceAddress)) {
            peer.deviceName = peers.get(peer.deviceAddress);
        }

        // Add to the peer list fragment
        PeersFragment fragment = (PeersFragment)activity_.getFragmentManager().findFragmentByTag(PeersFragment.TAG);
        if (fragment != null) {
            fragment.addPeerToList(peer);
        }
        System.out.println("onBonjourServiceAvailable " + instance_name);
    }

    public void discoveryRequest() {
        WifiP2pDnsSdServiceRequest request = WifiP2pDnsSdServiceRequest.newInstance();
        manager_.addServiceRequest(channel_, request, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                discoveryServices();
            }

            @Override
            public void onFailure(int i) {
                System.out.println("Add service request failed: " + errorCode(i));
                PeersFragment fragment = (PeersFragment)activity_.getFragmentManager().findFragmentByTag(PeersFragment.TAG);
                if (fragment != null) {
                    fragment.stopRefreshActionBar();
                }
            }
        });
    }

    private void discoveryServices() {
        manager_.discoverServices(channel_, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                System.out.println("Discovery services succeed");
            }

            @Override
            public void onFailure(int i) {
                stopDiscoveryRequest();
                System.out.println("Discovery services failed: " + errorCode(i));
                PeersFragment fragment = (PeersFragment)activity_.getFragmentManager().findFragmentByTag(PeersFragment.TAG);
                if (fragment != null) {
                    fragment.stopRefreshActionBar();
                    activity_.onErrorFromLocalService("Discovery services failed: " + errorCode(i));
                }
            }
        });
    }

    public void removeLocalService() {
        System.out.println("Local service removed");
        stopDiscoveryRequest();
        manager_.removeLocalService(channel_, service_info_, null);
    }

    public void stopDiscoveryRequest() {
        manager_.stopPeerDiscovery(channel_, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                System.out.println("Stop peer discovery succeed");
                clearServiceRequest();
            }
            @Override
            public void onFailure(int i) {
                // failure
                System.out.println("Stop peer discovery fail");
            }
        });
    }

    public void clearServiceRequest() {
        manager_.clearServiceRequests(channel_, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // success
                System.out.println("clear service request succeed");
            }

            @Override
            public void onFailure(int i) {
                // failure
                System.out.println("fail service request succeed");
            }
        });
    }

    private String errorCode(int i) {
        System.out.println("code: " + i);
        switch (i){
            case WifiP2pManager.P2P_UNSUPPORTED:
                return "P2P is not supported";
            case WifiP2pManager.BUSY:
                return "BUSY";
            case WifiP2pManager.ERROR:
                return "ERROR";
            case WifiP2pManager.NO_SERVICE_REQUESTS:
                return "NO SERVICE REQUESTS";
            default:
                return "Unknown";
        }
    }

    public interface LocalServiceListener {
        public void onErrorFromLocalService(String error);
    }
}
