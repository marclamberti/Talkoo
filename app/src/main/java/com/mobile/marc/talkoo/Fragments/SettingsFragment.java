//# CSIT 6000B    # Jordy Ngenze Domingos       20243311        jndomingos@ust.hk
//# CSIT 6000B    # Marc Lamberti               20243622        mlamberti@ust.hk

package com.mobile.marc.talkoo.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobile.marc.talkoo.NavigatorActivity;
import com.mobile.marc.talkoo.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.SettingsListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class SettingsFragment extends Fragment {
    public static final String TAG = "fragment2";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SECTION_NUMBER = "section_number";

    private int section_number;

    private SettingsListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param section_number Parameter 1.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(int section_number) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, section_number);
        fragment.setArguments(args);
        return fragment;
    }
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            section_number = getArguments().getInt(ARG_SECTION_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.onSettingsFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (SettingsListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        ((NavigatorActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface SettingsListener {
    }

}
