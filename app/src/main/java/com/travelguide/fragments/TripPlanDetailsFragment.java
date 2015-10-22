package com.travelguide.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.travelguide.R;
import com.travelguide.adapters.DayAdapter;
import com.travelguide.models.Day;
import com.travelguide.models.Place;
import com.travelguide.models.TripPlan;

import java.util.ArrayList;
import java.util.List;

public class TripPlanDetailsFragment extends Fragment implements AddUpdatePlaceDetailsFragment.EditItemDialogListener {

    private static final String ARG_TRIP_PLAN_OBJECT_ID = "tripPlanObjectId";
    private String mTripPLanObjectId;
    private TextView tvPlanName;
    private List<Day> mDayDetails;
    RecyclerView rvDayDetails;
    FloatingActionButton floatingActionButton;
    public static String selectedDay;

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
            //loadPlanDetails();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_plan_details, container, false);
        tvPlanName = (TextView) view.findViewById(R.id.tvPlanName);
        //FAB
        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fablListItems);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onitemClicktoFragment("Add New Place", selectedDay, null);
            }
        });

        mDayDetails = new ArrayList<Day>();
        // Lookup the recyclerview in activity layout
        rvDayDetails = (RecyclerView) view.findViewById(R.id.rvContacts);
        // Create adapter passing in the sample user data
        //fetch data from parse-using inner query on plan and then day details
        ParseQuery innerQuery = new ParseQuery(TripPlan.class);
        innerQuery.whereEqualTo("objectId", mTripPLanObjectId);
        ParseQuery query = new ParseQuery(Day.class);
        query.orderByAscending("travelDay");
        query.whereMatchesQuery("parent", innerQuery);
        query.findInBackground(new FindCallback<Day>() {
            @Override
            public void done(List<Day> list, ParseException e) {
                if (e == null) {
                    resetDayDetails(list);
                } else {
                    Log.d("ERROR", "Data not fetched");
                }
            }
        });
        //List<Day> days = new ArrayList<>();
        //days.add(new Day());
        //days.add(new Day());
        //DayAdapter adapter = new DayAdapter(days);
        // Attach the adapter to the recyclerview to populate items
        //rvDayDetails.setAdapter(adapter);
        // Set layout manager to position the items
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvDayDetails.setLayoutManager(layoutManager);
        //Toast.makeText(getContext(), "objectId: " + mTripPLanObjectId, Toast.LENGTH_LONG).show();
        //TODO Load PlanDetail from Parse locally
        return view;
    }

    public void resetDayDetails(List<Day> dayDetails) {
        mDayDetails.clear();
        mDayDetails.addAll(dayDetails);
        DayAdapter adapter = new DayAdapter(mDayDetails);
        rvDayDetails.setAdapter(adapter);
    }

    @Override
    public void onFinishEditDialogcontrol(String placeName, String travelTime, String dateObjectId) {
        ParseUser user = ParseUser.getCurrentUser();
        Place placeDetails = new Place();
        placeDetails.putCreatedUserId(user.getObjectId());
        placeDetails.putPlanName(placeName);
        placeDetails.putVisitingTime(Integer.parseInt(travelTime));
        placeDetails.putPlaceImageUrl("http://www.travelmanly.com/wp-content/uploads/2012/02/NewYorkCity2.jpg");
        placeDetails.put("parent", ParseObject.createWithoutData("DayDetails", dateObjectId));
        placeDetails.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                //dayidnew = daysDetailss.getObjectId();
            }
        });
    }

    //Calling the Interface Method from EditItemFragment.java
    public void onitemClicktoFragment(String title, String parseObjectId, String date) {
        android.support.v4.app.FragmentManager fragmentManager = getChildFragmentManager();
        AddUpdatePlaceDetailsFragment addUpdatePlace = AddUpdatePlaceDetailsFragment.newInstance(title, parseObjectId, null);
        addUpdatePlace.show(fragmentManager, "edit_item_dialog_fragment");
    }

    public static void onItemClicked(String objectId) {
        selectedDay = objectId;

    }

}
