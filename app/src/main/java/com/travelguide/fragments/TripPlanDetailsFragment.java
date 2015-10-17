package com.travelguide.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.travelguide.R;
import com.travelguide.adapters.DayAdapter;
import com.travelguide.models.Day;

import java.util.ArrayList;
import java.util.List;

public class TripPlanDetailsFragment extends Fragment {

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

        //
//        // Construct query to execute
//        ParseQuery<TripPlan> query = ParseQuery.getQuery(TripPlan.class);
//        // Configure limit and sort order
//        // Execute query to fetch all messages from Parse asynchronously
//        // This is equivalent to a SELECT query with SQL
//        query.findInBackground(new FindCallback<TripPlan>() {
//            public void done(List<TripPlan> messages, ParseException e) {
//                if (e == null) {
//                    Log.d("message", messages.toString());
//                } else {
//                    Log.d("message", "Error: " + e.getMessage());
//                }
//            }
//        });

        return view;
    }
}
