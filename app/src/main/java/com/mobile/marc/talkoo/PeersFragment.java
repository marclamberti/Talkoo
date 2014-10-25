package com.mobile.marc.talkoo;

import android.app.Activity;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p />
 * Activities containing this fragment MUST implement the
 * interface.
 */
public class PeersFragment extends Fragment implements AbsListView.OnItemClickListener {

    public static final String TAG = "fragment1";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SECTION_NUMBER = "section_number";

    private int                 section_number;
    private PeersListener       listener_;
    private List<WifiP2pDevice> peers_ = new ArrayList<WifiP2pDevice>();
    private MenuItem            menu_item_action_refresh_ = null;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView list_view_;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter adapter_;

    public static PeersFragment newInstance(int section_number) {
        PeersFragment fragment = new PeersFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, section_number);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PeersFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            section_number = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        adapter_ = new PeersAdapter(getActivity(), R.layout.item_peer, peers_);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_peers, container, false);

        // Set the adapter
        list_view_ = (AbsListView) view.findViewById(android.R.id.list);
        list_view_.setAdapter(adapter_);

        // Set OnItemClickListener so we can be notified on item clicks
        list_view_.setOnItemClickListener(this);

        // Initiate peer discovery
        listener_.onDiscoveringPeersRequest();

        // Set empty view
        list_view_.setEmptyView(view.findViewById(R.id.empty_peers_list));

        // Hide the empty view
        list_view_.getEmptyView().setVisibility(View.INVISIBLE);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener_ = (PeersListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement OnFragmentInteractionListener");
        }
        ((NavigatorActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener_ = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (listener_ != null) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            if (listener_.onIsWifiConnected()) {
                listener_.onConnectToPeer((WifiP2pDevice) adapter_.getItem(position));
            } else {
                Toast.makeText(getActivity(), "Your wifi seems to be disabled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Called when a peer is available
     */
    public void addPeerToList(WifiP2pDevice peer) {
        stopRefreshActionBar();
        // Clear old peers add news and update the list view
        peers_.clear();
        peers_.add(peer);
        displayEmptyView();
        System.out.println("Peers found");
        ((PeersAdapter)adapter_).update();
        listener_.onDiscoveringPeersRequestDone();
    }

    /**
     * Remove all peers from the list
     */
    public void clearPeers() {
        peers_.clear();
        ((PeersAdapter)adapter_).update();
    }

    /**
     * Refresh peers list
     */
    public void refreshPeersList(MenuItem item) {
        menu_item_action_refresh_ = item;
        menu_item_action_refresh_.setActionView(R.layout.action_refresh_progress_bar);
        menu_item_action_refresh_.expandActionView();
        listener_.onDiscoveringPeersRequest();
    }

    /**
     * Refresh in action bar
     */
    void stopRefreshActionBar() {
        // Stop the action refresh in the action action bar
        if (menu_item_action_refresh_ != null) {
            menu_item_action_refresh_.collapseActionView();
            menu_item_action_refresh_.setActionView(null);
            menu_item_action_refresh_ = null;
        }
    }

    /**
     * Displaying the list's empty view
     */
    void displayEmptyView() {
        if (peers_.size() == 0) {
            list_view_.getEmptyView().setVisibility(View.VISIBLE);
        } else {
            list_view_.getEmptyView().setVisibility(View.INVISIBLE);
        }
    }

    /**
    * This interface must be implemented by activities that contain this
    * fragment to allow an interaction in this fragment to be communicated
    * to the activity and potentially other fragments contained in that
    * activity.
    * <p>
    * See the Android Training lesson <a href=
    * "http://developer.android.com/training/basics/fragments/communicating.html"
    * >Communicating with Other Fragments</a> for more information.
    */
    public interface PeersListener {
        public void onDiscoveringPeersRequest();
        public void onDiscoveringPeersRequestDone();
        public void onConnectToPeer(WifiP2pDevice peer);
        public boolean onIsWifiConnected();
    }

}
