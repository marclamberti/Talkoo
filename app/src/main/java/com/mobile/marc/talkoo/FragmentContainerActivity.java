//# CSIT 6000B    # Jordy Ngenze Domingos       20243311        jndomingos@ust.hk
//# CSIT 6000B    # Marc Lamberti               20243622        mlamberti@ust.hk

package com.mobile.marc.talkoo;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.mobile.marc.talkoo.Fragments.HomeFragment;
import com.mobile.marc.talkoo.R;

/**
 * This class is just used to do the unit test with the Fragment
 */
public class  FragmentContainerActivity extends FragmentActivity implements HomeFragment.HomeListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setId(R.id.container_id);

        setContentView(frameLayout, params);
    }

    public void addFragment(Fragment fragment, String tag) {
        getFragmentManager().beginTransaction().replace(R.id.container_id, fragment, tag).commit();
    }
}
