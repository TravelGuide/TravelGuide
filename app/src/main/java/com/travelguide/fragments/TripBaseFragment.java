package com.travelguide.fragments;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

public abstract class TripBaseFragment extends Fragment {

    protected void setTitle(String title) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
    }
}
