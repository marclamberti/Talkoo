package com.mobile.marc.talkoo.Fragments;

import android.test.ActivityUnitTestCase;

import com.mobile.marc.talkoo.R;

/**
 * Created by Marc on 15/12/14.
 */
public class TestHomeFragment extends ActivityUnitTestCase<FragmentContainerActivity> {

    private HomeFragment home_fragment;
    private int text_view_id;

    public TestHomeFragment() {
        super(FragmentContainerActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        //home_fragment = new HomeFragment.newInstance(1);
        //getActivity().addFragment(home_fragment, HomeFragment.class.getSimpleName());
    }

    public void testLayout() {
        text_view_id = R.id.home_text_view;
        //assertNotNull(home_fragment.getView().findViewById(text_view_id));
        //TextView text_view = (FlatTextView)home_fragment.getView().findViewById(R.id.home_text_view);
        //assertNotNull("Edit text not allowed to be null", text_view);
    }
}
