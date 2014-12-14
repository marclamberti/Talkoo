//# CSIT 6000B    # Jordy Ngenze Domingos       20243311        jndomingos@ust.hk
//# CSIT 6000B    # Marc Lamberti               20243622        mlamberti@ust.hk

package com.mobile.marc.talkoo.Adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.net.wifi.p2p.WifiP2pDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mobile.marc.talkoo.R;

import java.util.List;
import java.util.Objects;

/**
 * PeersAdapter, used to describe the process of converting the Java object to a view.
 * This approach doesn't use view caching.
 */
public class PeersAdapter extends ArrayAdapter<WifiP2pDevice> {

    private List<WifiP2pDevice> peers_;

    public PeersAdapter(Context context, int text_view_resource_id, List<WifiP2pDevice> peers) {
        super(context, text_view_resource_id, peers);
        peers_ = peers;
    }

    /**
     * Get the status from peer.status() and transforms it from int to string
     * @param status
     * @return status
     */
    private static String getPeerStatus(int status) {
        switch (status) {
            case WifiP2pDevice.CONNECTED:
                return "Connected";
            case WifiP2pDevice.AVAILABLE:
                return "Available";
            case WifiP2pDevice.INVITED:
                return "Invited";
            case WifiP2pDevice.FAILED:
                return "Failed";
            case WifiP2pDevice.UNAVAILABLE:
                return "Unavailable";
            default:
                return "Unknown";
        }
    }

    /**
     * Method returning the actual view used as a row within the listView
     * at a position
     */
    @Override
    public View getView(int position, View convert_view, ViewGroup parent) {
        View    view = convert_view;
        // Check if there is an existing view, otherwise inflate the view
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_peer, parent, false);
        }

        // Update the row
        WifiP2pDevice   peer = getItem(position);
        if (peer != null) {
            TextView    name = (TextView)view.findViewById(R.id.item_peer_name);
            TextView    details = (TextView)view.findViewById(R.id.item_peer_details);
            name.setText(peer.deviceName);
            details.setText(getPeerStatus(peer.status));
        }
        return view;
    }

    @Override
    public int getCount() {
        return peers_.size();
    }

    public void update() {
        this.notifyDataSetChanged();
    }
}
