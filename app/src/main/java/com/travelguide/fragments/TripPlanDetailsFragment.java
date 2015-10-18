package com.travelguide.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.travelguide.R;
import com.travelguide.adapters.DayAdapter;
import com.travelguide.models.Day;

import java.util.ArrayList;
import java.util.List;

public class TripPlanDetailsFragment extends Fragment {

    private static final String ARG_TRIP_PLAN_OBJECT_ID = "tripPlanObjectId";

    private String mTripPLanObjectId;

    public static TripPlanDetailsFragment newInstance(String tripPlanObjectId) {
        TripPlanDetailsFragment fragment = new TripPlanDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TRIP_PLAN_OBJECT_ID, tripPlanObjectId);
        fragment.setArguments(args);
        return fragment;
    }

    public TripPlanDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTripPLanObjectId = getArguments().getString(ARG_TRIP_PLAN_OBJECT_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_plan_details, container, false);

        // Lookup the recyclerview in activity layout
        RecyclerView rvContacts = (RecyclerView) view.findViewById(R.id.rvContacts);
        // Create adapter passing in the sample user data
        List<Day> days = new ArrayList<>();
        days.add(new Day());
        days.add(new Day());
        days.add(new Day());
        days.add(new Day());
        days.add(new Day());
        days.add(new Day());
        days.add(new Day());
        days.add(new Day());
        days.add(new Day());
        DayAdapter adapter = new DayAdapter(days);
        // Attach the adapter to the recyclerview to populate items
        rvContacts.setAdapter(adapter);
        // Set layout manager to position the items
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvContacts.setLayoutManager(layoutManager);

        Toast.makeText(getContext(), "objectId: " + mTripPLanObjectId, Toast.LENGTH_LONG).show();

        //TODO Load PlanDetail from Parse locally

        return view;
    }
}
