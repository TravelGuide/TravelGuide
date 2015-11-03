package com.travelguide.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.bumptech.glide.Glide;
import com.parse.ParseObject;
import com.squareup.picasso.Picasso;
import com.travelguide.R;
import com.travelguide.adapters.TripPlanAdapter;
import com.travelguide.adapters.TripPlanPagerAdapter;
import com.travelguide.helpers.DeviceDimensionsHelper;
import com.travelguide.helpers.ItemClickSupport;
import com.travelguide.models.TripPlan;
import com.travelguide.ui.RotationPageTransformer;
import com.xgc1986.parallaxPagerTransformer.ParallaxPagerTransformer;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author USPRKAN
 *
 * History:
 *   18-Oct-2015    kprav       Initial Version
 */
public class ProfileFragment extends Fragment {
    private static final String TAG = ProfileFragment.class.getSimpleName();

    private View view;
    private String userObjectId = null;
    private SharedPreferences userInfo;
    private OnFragmentInteractionListener mListener;

    private ViewPager vpPager;
    private TripPlanPagerAdapter viewPagerAdapter;
    private TabLayout tabsStrip;

    // private PagerSlidingTabStrip tabsStrip;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSharedPreferences();
        if (userObjectId.equals("missing")) {
            Toast.makeText(getActivity(), "No logged in user! Cannot view profile!!", Toast.LENGTH_SHORT).show();
            onStop();
            onDestroy();
            onDetach();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        setHasOptionsMenu(true);
        loadFragments(view);
        return view;
    }

    private void loadFragments(View view) {
        vpPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPagerAdapter = new TripPlanPagerAdapter(getChildFragmentManager());
        vpPager.setAdapter(viewPagerAdapter);
        tabsStrip = (TabLayout) view.findViewById(R.id.tabs);
        tabsStrip.setupWithViewPager(vpPager);
        setupParallaxPagerTransformerForViewPager();
        // tabsStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        // tabsStrip.setViewPager(vpPager);
        // tabsStrip.setViewPager(vpPager);
        // setupPageChangeListener();
    }

    private void setupParallaxPagerTransformerForViewPager() {
        ParallaxPagerTransformer pt = new ParallaxPagerTransformer(R.id.rvTripPlansInProfile);
        pt.setBorder(20);
        vpPager.setPageTransformer(false, pt);
    }

    private void setupRotationPageTransformerForViewPager() {
        vpPager.setPageTransformer(true, new RotationPageTransformer(165));
        vpPager.setOffscreenPageLimit(1);
        vpPager.setPageMargin(10);
        vpPager.setClipChildren(false);
    }

    // private void setupPageChangeListener() {
    //     tabsStrip.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
    //         @Override
    //         public void onPageSelected(final int position) {
    //             Log.d(TAG, "PageChanged");
    //         }
    //     });
    // }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getFragmentManager().popBackStack();
                ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getSharedPreferences() {
        userInfo = getActivity().getSharedPreferences("userInfo", 0);
        userObjectId = userInfo.getString("userObjectId", "missing");
    }

    public interface OnFragmentInteractionListener {
        void onTripPlanItemSelected(String tripPlanObjectId);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (view != null)
            loadFragments(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
        mListener = null;
    }

    public void addItemClickSupport(RecyclerView rvTripPlans, final TripPlanAdapter mTripPlanAdapter) {
        ItemClickSupport.addTo(rvTripPlans).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                String tripPlanObjectId = mTripPlanAdapter.get(position).getObjectId();
                if (mListener != null) {
                    mListener.onTripPlanItemSelected(tripPlanObjectId);
                }
            }
        });
    }
}
