//# CSIT 6000B    # Jordy Ngenze Domingos       20243311        jndomingos@ust.hk
//# CSIT 6000B    # Marc Lamberti               20243622        mlamberti@ust.hk

package com.mobile.marc.talkoo.Fragments;

import android.app.Fragment;
import android.os.Parcelable;

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
            default:
                fragment = HomeFragment.newInstance(sectionNumber);
        }
        return fragment;
    }
}
