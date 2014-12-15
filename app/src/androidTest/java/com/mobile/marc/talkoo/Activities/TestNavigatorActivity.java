package com.mobile.marc.talkoo.Activities;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mobile.marc.talkoo.Adapters.PeersAdapter;
import com.mobile.marc.talkoo.Fragments.HomeFragment;
import com.mobile.marc.talkoo.Fragments.NavigationDrawerFragment;
import com.mobile.marc.talkoo.Fragments.PeersFragment;
import com.mobile.marc.talkoo.Fragments.SettingsFragment;
import com.mobile.marc.talkoo.NavigatorActivity;
import com.mobile.marc.talkoo.R;

/**
 * Created by Marc on 15/12/14.
 */
public class TestNavigatorActivity extends ActivityInstrumentationTestCase2<NavigatorActivity> {

    HomeFragment home_fragment;
    PeersFragment peersFragment;
    SettingsFragment settingsFragment;

    public TestNavigatorActivity() {
        super(NavigatorActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testNavigator() {
        // Check Navigation Bar with Home
        getActivity().onNavigationDrawerItemSelected(0);
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                getActivity().getFragmentManager().executePendingTransactions();
            }
        });
        home_fragment = (HomeFragment)getActivity().getFragmentManager().findFragmentByTag(HomeFragment.TAG);
        assertTrue("The home fragment is not displayed", home_fragment.isVisible());

        // Check Navigation Bar with Peers
        getActivity().onNavigationDrawerItemSelected(1);
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                getActivity().getFragmentManager().executePendingTransactions();
            }
        });
        peersFragment = (PeersFragment)getActivity().getFragmentManager().findFragmentByTag(PeersFragment.TAG);
        assertTrue("The peer fragment is not displayed", peersFragment.isVisible());

        // Check Navigation Bar with Settings
        getActivity().onNavigationDrawerItemSelected(2);
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                getActivity().getFragmentManager().executePendingTransactions();
            }
        });
        settingsFragment = (SettingsFragment)getActivity().getFragmentManager().findFragmentByTag(SettingsFragment.TAG);
        assertTrue("The setting fragment is not displayed", settingsFragment.isVisible());
    }

    /**
     * Test Home Fragment
     */
    public void testHomeFragment() {
        getActivity().onNavigationDrawerItemSelected(0);
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                getActivity().getFragmentManager().executePendingTransactions();
            }
        });
        home_fragment = (HomeFragment)getActivity().getFragmentManager().findFragmentByTag(HomeFragment.TAG);
        int text_view_id = R.id.home_text_view;
        assertNotNull(home_fragment);
        View view = home_fragment.getView();
        assertNotNull(view);
        assertNotNull(home_fragment.getView().findViewById(text_view_id));
        TextView textView = (TextView)view.findViewById(R.id.home_text_view);
        assertNotNull(textView);
        String text = textView.getText().toString();
        assertEquals("The text in the text view is incorrect", "Welcome in Talkoo, we are happy to see you", text);
    }

    /**
     * Test Peers Fragment
     */
    public void testPeerFragment() {
        getActivity().onNavigationDrawerItemSelected(1);
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                getActivity().getFragmentManager().executePendingTransactions();
            }
        });
        peersFragment = (PeersFragment) getActivity().getFragmentManager().findFragmentByTag(PeersFragment.TAG);
        View peerFragmentView = peersFragment.getView();
        assertNotNull(peerFragmentView);
        AbsListView listView = (AbsListView)peersFragment.getView().findViewById(android.R.id.list);
        assertNotNull(listView);
        PeersAdapter adapter = (PeersAdapter)listView.getAdapter();
        assertNotNull(adapter);
        assertEquals(adapter.getCount(), 0);
        View peerEmptyView = listView.getEmptyView();
        //assertEquals("The empty view should be displayed", peerEmptyView.getVisibility(), true);
    }
}
