//# CSIT 6000B    # Jordy Ngenze Domingos       20243311        jndomingos@ust.hk
//# CSIT 6000B    # Marc Lamberti               20243311        mlamberti@ust.hk

package com.mobile.marc.talkoo.Fragments;

import android.app.Fragment;
import android.os.Parcelable;

import com.mobile.marc.talkoo.Fragments.HomeFragment;
import com.mobile.marc.talkoo.Fragments.PeersFragment;
import com.mobile.marc.talkoo.Fragments.RoomFragment;
import com.mobile.marc.talkoo.Fragments.SettingsFragment;

/**
 * Created by Marc on 21/10/14.
 */
public class FragmentFactory {
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Fragment newInstance(int sectionNumber, Parcelable object) {
        Fragment fragment = null;
        switch (sectionNumber){
            case 0:
                fragment = HomeFragment.newInstance(sectionNumber);
                break;
            case 1:
                fragment = PeersFragment.newInstance(sectionNumber);
                break;
            case 2:
                fragment = SettingsFragment.newInstance(sectionNumber);
                break;
            case 3:
                fragment = RoomFragment.newInstance(sectionNumber);
                break;
            default:
                fragment = HomeFragment.newInstance(sectionNumber);
        }
        return fragment;
    }
}
