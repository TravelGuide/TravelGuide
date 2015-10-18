package com.travelguide.fragments;

import android.content.Context;
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
import com.travelguide.adapters.TripPlanAdapter;
import com.travelguide.decorations.VerticalSpaceItemDecoration;
import com.travelguide.models.TripPlan;
import com.travelguide.utils.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

public class TripPlanListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private TripPlanAdapter tripPlanAdapter;

    //TODO Load Plan from Parse remotelly

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_plan_list, container, false);

        RecyclerView rvTripPlans = (RecyclerView) view.findViewById(R.id.rvTripPlans);

        List<TripPlan> tripPlans = new ArrayList<>();
        for (int i = 0; i< 50; i++) {
            tripPlans.add(new TripPlan());
        }

        tripPlanAdapter = new TripPlanAdapter(tripPlans);
        // Attach the adapter to the recyclerview to populate items
        rvTripPlans.setAdapter(tripPlanAdapter);
        // Set layout manager to position the items
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvTripPlans.setLayoutManager(layoutManager);

        RecyclerView.ItemDecoration itemDecoration = new VerticalSpaceItemDecoration(20);
        rvTripPlans.addItemDecoration(itemDecoration);

        ItemClickSupport.addTo(rvTripPlans).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                String name = tripPlanAdapter.get(position).getPlanName();
                Toast.makeText(getContext(), name + " was clicked!", Toast.LENGTH_SHORT).show();
                if (mListener != null){
                    mListener.onTripPlanItemSelected("15234");
                }
            }
        });

        return view;
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

    public interface OnFragmentInteractionListener {
        void onTripPlanItemSelected(String tripPlanObjectId);
    }
}
