package com.travelguide.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.travelguide.R;

import java.util.ArrayList;

/**
 * @author kprav
 *
 * History:
 *   11/03/2015     kprav       Initial Version
 */
public class FullscreenFragment extends Fragment {

    private ArrayList<String> imageUrlSet;

    public static FullscreenFragment newInstance(ArrayList<String> imageUrlSet) {
        FullscreenFragment fragment = new FullscreenFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("imageUrlSet", imageUrlSet);
        fragment.setArguments(args);
        return fragment;
    }

    public FullscreenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageUrlSet = getArguments().getStringArrayList("imageUrlSet");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fullscreen, container, false);
        FullscreenPagerAdapter fullscreenPagerAdapter = new FullscreenPagerAdapter(getContext(), imageUrlSet);
        ViewPager fullscreenViewPager = (ViewPager) view.findViewById(R.id.viewPagerFullScreen);
        fullscreenViewPager.setAdapter(fullscreenPagerAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
