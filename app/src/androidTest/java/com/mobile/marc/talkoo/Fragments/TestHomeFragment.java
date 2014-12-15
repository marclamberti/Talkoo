//# CSIT 6000B    # Jordy Ngenze Domingos       20243311        jndomingos@ust.hk
//# CSIT 6000B    # Marc Lamberti               20243622        mlamberti@ust.hk

package com.mobile.marc.talkoo.Fragments;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.view.View;

import com.mobile.marc.talkoo.FragmentContainerActivity;
import com.mobile.marc.talkoo.LoginActivity;
import com.mobile.marc.talkoo.R;

public class TestHomeFragment extends ActivityUnitTestCase<FragmentContainerActivity> {

    private FragmentContainerActivity activity;
    private HomeFragment home_fragment;
    private View fragment_view;
    private int text_view_id;

    public TestHomeFragment() {
        super(FragmentContainerActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Intent intent = new Intent(getInstrumentation().getTargetContext(), FragmentContainerActivity.class);
        startActivity(intent, null, null);
        getActivity().addFragment(HomeFragment.newInstance(1), HomeFragment.class.getSimpleName());
    }

    public void testLayout() {
        home_fragment = (HomeFragment)getActivity().getFragmentManager().findFragmentByTag(HomeFragment.class.getSimpleName());
        assertNotNull(home_fragment);
        fragment_view = home_fragment.getView();
        assertNotNull(fragment_view);
        text_view_id = R.id.home_text_view;
        assertNotNull(fragment_view.findViewById(text_view_id));
        //TextView text_view = (FlatTextView)home_fragment.getView().findViewById(R.id.home_text_view);
        //assertNotNull("Edit text not allowed to be null", text_view);
    }
}
