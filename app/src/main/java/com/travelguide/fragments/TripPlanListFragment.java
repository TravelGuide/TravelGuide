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
import com.travelguide.adapters.TripPlanAdapter;
import com.travelguide.decorations.VerticalSpaceItemDecoration;
import com.travelguide.models.TripPlan;

import java.util.ArrayList;
import java.util.List;

public class TripPlanListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_plan_list, container, false);

        RecyclerView rvTripPlans = (RecyclerView) view.findViewById(R.id.rvTripPlans);

        List<TripPlan> tripPlans = new ArrayList<>();
        for (int i = 0; i< 50; i++) {
            tripPlans.add(new TripPlan());
        }

        TripPlanAdapter adapter = new TripPlanAdapter(tripPlans);
        // Attach the adapter to the recyclerview to populate items
        rvTripPlans.setAdapter(adapter);
        // Set layout manager to position the items
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvTripPlans.setLayoutManager(layoutManager);

        RecyclerView.ItemDecoration itemDecoration = new VerticalSpaceItemDecoration(20);
        rvTripPlans.addItemDecoration(itemDecoration);

        return view;
    }
}
