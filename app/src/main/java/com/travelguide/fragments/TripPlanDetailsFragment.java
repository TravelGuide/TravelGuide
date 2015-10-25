package com.travelguide.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.travelguide.R;
import com.travelguide.adapters.DayAdapter;
import com.travelguide.adapters.PlaceAdapter;
import com.travelguide.decorations.DividerItemDecoration;
import com.travelguide.helpers.ItemClickSupport;
import com.travelguide.helpers.NetworkAvailabilityCheck;
import com.travelguide.models.Day;
import com.travelguide.models.Place;
import com.travelguide.models.TripPlan;

import java.util.ArrayList;
import java.util.List;

public class TripPlanDetailsFragment extends TripBaseFragment
        implements AddUpdatePlaceDetailsFragment.EditItemDialogListener {

    private static final String ARG_TRIP_PLAN_OBJECT_ID = "tripPlanObjectId";

    private RecyclerView rvDayDetails;
    private RecyclerView rvPlaceDetails;

    private String mTripPLanObjectId;
    private String mSelectedDayObjectId;

    private List<Day> mDayList;
    private List<Place> mPlaceList;

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
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_plan_details, container, false);
        setHasOptionsMenu(true);

        //Setup FAB New Place
        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fabNewPlace);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddUpdatePlaceDetailsFragment addUpdatePlace = AddUpdatePlaceDetailsFragment
                        .newInstance("Add New Place", null, null, TripPlanDetailsFragment.this);
                addUpdatePlace.show(getFragmentManager(), "add_update_place_details_fragment");
            }
        });

        //Setup RecyclerView Days
        LinearLayoutManager layoutManagerDay = new LinearLayoutManager(getContext());
        layoutManagerDay.setOrientation(LinearLayoutManager.HORIZONTAL);

        mDayList = new ArrayList<>();
        mDayAdapter = new DayAdapter(mDayList);

        rvDayDetails = (RecyclerView) view.findViewById(R.id.rvContacts);
        rvDayDetails.setLayoutManager(layoutManagerDay);
        rvDayDetails.setAdapter(mDayAdapter);

        ItemClickSupport.addTo(rvDayDetails).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                mSelectedDayObjectId = mDayAdapter.get(position).getObjectId();
                Toast.makeText(getContext(), "mSelectedDayObjectId: " + mSelectedDayObjectId,
                        Toast.LENGTH_LONG).show();
                loadTripPlacesFromRemote();
            }
        });

        //Setup RecyclerView Places
        LinearLayoutManager layoutManagerPlace = new LinearLayoutManager(getContext());
        layoutManagerPlace.setOrientation(LinearLayoutManager.VERTICAL);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL_LIST);

        mPlaceList = new ArrayList<>();
        mPlaceAdapter = new PlaceAdapter(mPlaceList, getContext());

        rvPlaceDetails = (RecyclerView) view.findViewById(R.id.rvPlaces);
        rvPlaceDetails.setLayoutManager(layoutManagerPlace);
        rvPlaceDetails.setAdapter(mPlaceAdapter);
        rvPlaceDetails.addItemDecoration(itemDecoration);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        loadTripPlanFromDatabase();

        if (NetworkAvailabilityCheck.networkAvailable(getActivity())) {
            loadTripDaysFromRemote();
        }
    }

    @Override
    public void onFinishEditDialogControl(String placeName, String travelTime) {
        ParseUser user = ParseUser.getCurrentUser();
        final Place placeDetails = new Place();
        placeDetails.putCreatedUserId(user.getObjectId());
        placeDetails.putPlaceName(placeName);
        placeDetails.putVisitingTime(travelTime);
        placeDetails.putPlaceImageUrl("http://www.travelmanly.com/wp-content/uploads/2012/02/NewYorkCity2.jpg");
        placeDetails.put("parent", ParseObject.createWithoutData("DayDetails", mSelectedDayObjectId));
        placeDetails.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    addTripPlanPlace(placeDetails);
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

    private void loadTripDaysFromRemote() {
        ParseQuery<TripPlan> innerQuery = ParseQuery.getQuery(TripPlan.class);
        innerQuery.whereEqualTo("objectId", mTripPLanObjectId);

        ParseQuery<Day> query = ParseQuery.getQuery(Day.class);
        query.orderByAscending("travelDay");
        query.whereMatchesQuery("parent", innerQuery);
        query.findInBackground(new FindCallback<Day>() {
            @Override
            public void done(List<Day> days, ParseException e) {
                if (e == null) {
                    populateTripPlanDays(days);
                } else {
                    Log.d("ERROR", "Data not fetched");
                }
            }
        });
    }

    private void loadTripPlacesFromRemote() {
        ParseQuery<Day> innerQuery = ParseQuery.getQuery(Day.class);
        innerQuery.whereEqualTo("objectId", mSelectedDayObjectId);

        ParseQuery<Place> query = ParseQuery.getQuery(Place.class);
        query.whereMatchesQuery("parent", innerQuery);
        query.findInBackground(new FindCallback<Place>() {
            @Override
            public void done(List<Place> places, ParseException e) {
                if (e == null) {
                    populateTripPlanPlaces(places);
                } else {
                    Log.d("ERROR", "Data not fetched");
                }
            }
        });
    }

    private void populateTripPlanDays(List<Day> days) {
        mDayList.clear();
        mDayList.addAll(days);
        mDayAdapter.notifyDataSetChanged();
    }

    private void addTripPlanDay(Day day) {
        //TODO To be implemented, plus button on RecyclerView Days
    }

    private void populateTripPlanPlaces(List<Place> places) {
        mPlaceList.clear();
        mPlaceList.addAll(places);
        mPlaceAdapter.notifyDataSetChanged();
    }

    private void addTripPlanPlace(Place place) {
        mPlaceList.add(place);
        mPlaceAdapter.notifyDataSetChanged();
    }

    private void loadTripPlanFromDatabase() {
        ParseQuery<TripPlan> query = ParseQuery.getQuery(TripPlan.class);
        query.fromLocalDatastore();
        query.getInBackground(mTripPLanObjectId, new GetCallback<TripPlan>() {
            @Override
            public void done(TripPlan tripPlan, ParseException e) {
                if (e == null) {
                    setTitle(tripPlan.getPlanName());
                }
            }
        });
    }
}
