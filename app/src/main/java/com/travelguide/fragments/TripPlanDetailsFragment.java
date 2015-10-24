package com.travelguide.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.travelguide.adapters.PlaceAdapter;
import com.travelguide.helpers.ItemClickSupport;
import com.travelguide.models.Day;
import com.travelguide.models.Place;
import com.travelguide.models.TripPlan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TripPlanDetailsFragment extends Fragment
        implements AddUpdatePlaceDetailsFragment.EditItemDialogListener {

    private static final String ARG_TRIP_PLAN_OBJECT_ID = "tripPlanObjectId";

    private TextView tvPlanName;
    private RecyclerView rvDayDetails;
    private RecyclerView rvPlaceDetails;

    private String mTripPLanObjectId;
    private String mSelectedDayObjectId;
    private List<Day> mDayDetails;
    private List<Place> mPlaceDetails;

    private PlaceAdapter mPlaceAdapter;
    private DayAdapter mDayAdapter;

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
        setHasOptionsMenu(true);
        //FAB
        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fablListItems);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddUpdatePlaceDetailsFragment addUpdatePlace = AddUpdatePlaceDetailsFragment
                        .newInstance("Add New Place", null, null, TripPlanDetailsFragment.this);
                addUpdatePlace.show(getFragmentManager(), "add_update_place_details_fragment");
            }
        });
        mDayDetails = new ArrayList<Day>();

        Day day = new Day();
        day.putTravelDate(Calendar.getInstance().getTime());
        day.putPlanName("Plan Name");
        day.putTravelDay(1);

        mDayDetails.add(day);

        mPlaceDetails = new ArrayList<Place>();


        // Lookup the recyclerview in activity layout
        rvDayDetails = (RecyclerView) view.findViewById(R.id.rvContacts);
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

        ItemClickSupport.addTo(rvDayDetails).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                mSelectedDayObjectId = mDayAdapter.get(position).getObjectId();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvDayDetails.setLayoutManager(layoutManager);

        rvPlaceDetails = (RecyclerView) view.findViewById(R.id.rvPlaces);
        mPlaceAdapter = new PlaceAdapter(mPlaceDetails);
        rvPlaceDetails.setAdapter(mPlaceAdapter);
        rvPlaceDetails.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    public void resetDayDetails(List<Day> dayDetails) {
        mDayDetails.clear();
        mDayDetails.addAll(dayDetails);
        mDayAdapter = new DayAdapter(mDayDetails);
        rvDayDetails.setAdapter(mDayAdapter);
    }

    public void resetPlaceDetails(List<Place> placeDetails) {
        mPlaceDetails.clear();
        mPlaceDetails.addAll(placeDetails);
        //rvPlaceDetails.setLayoutManager(new LinearLayoutManager(this));
        mPlaceAdapter.notifyDataSetChanged();
        mPlaceAdapter = new PlaceAdapter(mPlaceDetails);
        rvPlaceDetails.setAdapter(mPlaceAdapter);

    }

    private void onFinishEditAddingNewPlace(String placeName, String travelTime) {
        ParseUser user = ParseUser.getCurrentUser();
        Place placeDetails = new Place();
        placeDetails.putCreatedUserId(user.getObjectId());
        placeDetails.putPlaceName(placeName);
        placeDetails.putVisitingTime(travelTime);
        placeDetails.putPlaceImageUrl("http://www.travelmanly.com/wp-content/uploads/2012/02/NewYorkCity2.jpg");
        placeDetails.put("parent", ParseObject.createWithoutData("DayDetails", mSelectedDayObjectId));
        placeDetails.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    onDateClicked(mSelectedDayObjectId);
                }
            }
        });
    }

    //Method to load places data on click
    public void onDateClicked(String travelDateObjectId) {
        mSelectedDayObjectId = travelDateObjectId;
        ParseQuery innerQuery = new ParseQuery(Day.class);
        innerQuery.whereEqualTo("objectId", travelDateObjectId);
        ParseQuery query = new ParseQuery(Place.class);
        query.whereMatchesQuery("parent", innerQuery);
        query.findInBackground(new FindCallback<Place>() {
            @Override
            public void done(List<Place> list, ParseException e) {
                if (e == null) {
                    resetPlaceDetails(list);
                } else {
                    Log.d("ERROR", "Data not fetched");
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getFragmentManager().popBackStack();
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFinishEditDialogcontrol(String placeName, String travelTime) {
        onFinishEditAddingNewPlace("placeName", "travelTime");
    }
}
