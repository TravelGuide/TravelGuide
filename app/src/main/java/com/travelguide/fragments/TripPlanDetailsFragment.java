package com.travelguide.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.travelguide.R;
import com.travelguide.adapters.DayAdapter;
import com.travelguide.models.Day;
import com.travelguide.models.TripPlan;

import java.util.ArrayList;
import java.util.List;

public class TripPlanDetailsFragment extends Fragment {

    private static final String ARG_TRIP_PLAN_OBJECT_ID = "tripPlanObjectId";

    private String mTripPLanObjectId;

    private TextView tvPlanName;

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
            loadPlanDetails();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_plan_details, container, false);

        tvPlanName = (TextView) view.findViewById(R.id.tvPlanName);

        getActivity().setTitle(R.string.travel);
        setHasOptionsMenu(true);

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

    private void loadPlanDetails() {
        ParseQuery<TripPlan> query = ParseQuery.getQuery(TripPlan.class);
        query.fromLocalDatastore();
        query.getInBackground(mTripPLanObjectId, new GetCallback<TripPlan>() {
            @Override
            public void done(TripPlan tripPlan, ParseException e) {
                if (e == null) {
                    tvPlanName.setText(tripPlan.getPlanName());
                }
            }
        });
    }
}
