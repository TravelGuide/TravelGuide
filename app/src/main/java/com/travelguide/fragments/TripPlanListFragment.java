package com.travelguide.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.travelguide.R;
import com.travelguide.adapters.TripPlanAdapter;
import com.travelguide.decorations.VerticalSpaceItemDecoration;
import com.travelguide.models.TripPlan;
import com.travelguide.utils.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

public class TripPlanListFragment extends Fragment {

    private static final String TAG = TripPlanListFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;
    private TripPlanAdapter mTripPlanAdapter;
    private List<TripPlan> mTripPlans;

    //TODO Load Plan from Parse remotelly

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_plan_list, container, false);

        RecyclerView rvTripPlans = (RecyclerView) view.findViewById(R.id.rvTripPlans);
        mTripPlans = new ArrayList<>();
        mTripPlanAdapter = new TripPlanAdapter(mTripPlans);
        // Attach the adapter to the recyclerview to populate items
        rvTripPlans.setAdapter(mTripPlanAdapter);
        // Set layout manager to position the items
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvTripPlans.setLayoutManager(layoutManager);

        RecyclerView.ItemDecoration itemDecoration = new VerticalSpaceItemDecoration(20);
        rvTripPlans.addItemDecoration(itemDecoration);

        ItemClickSupport.addTo(rvTripPlans).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                String tripPlanObjectId = mTripPlanAdapter.get(position).getObjectId();
                if (mListener != null){
                    mListener.onTripPlanItemSelected(tripPlanObjectId);
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        populateTripPlanList();
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void populateTripPlanList() {
        // Construct query to execute
        ParseQuery<TripPlan> query = ParseQuery.getQuery(TripPlan.class);
        query.findInBackground(new FindCallback<TripPlan>() {
            @Override
            public void done(List<TripPlan> tripPlans, ParseException e) {
                if (e == null) {
                    mTripPlans.clear();
                    mTripPlans.addAll(tripPlans);
                    mTripPlanAdapter.notifyDataSetChanged();
                    savingOnDatabase(tripPlans);
                } else {
                    Log.d(TAG, "Error: " + e.getMessage());
                }
            }
        });
    }

    private void savingOnDatabase(List<TripPlan> tripPlans) {
        for (TripPlan tp: tripPlans)
            tp.pinInBackground();
        //TODO Investigate why "ParseObject.saveAll(tripPlans);" not working.
//        try {
//            ParseObject.saveAll(tripPlans);
//        } catch (ParseException e1) {
//            e1.printStackTrace();
//        }
    }

    public interface OnFragmentInteractionListener {
        void onTripPlanItemSelected(String tripPlanObjectId);
    }
}
