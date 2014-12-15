//# CSIT 6000B    # Jordy Ngenze Domingos       20243311        jndomingos@ust.hk
//# CSIT 6000B    # Marc Lamberti               20243622        mlamberti@ust.hk

package com.mobile.marc.talkoo.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.mobile.marc.talkoo.R;

public class  FragmentContainerActivity extends Activity implements HomeFragment.HomeListener {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setId(R.id.container_id);

        setContentView(frameLayout, params);
    }

    public void addFragment(Fragment fragment, String tag) {
        getFragmentManager().beginTransaction().add(R.id.container_id, fragment, tag).commit();
    }
}
