package com.travelguide.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
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
import com.travelguide.helpers.GoogleImageSearch;
import com.travelguide.helpers.ItemClickSupport;
import com.travelguide.helpers.NetworkAvailabilityCheck;
import com.travelguide.helpers.Preferences;
import com.travelguide.listener.OnTripPlanListener;
import com.travelguide.models.Day;
import com.travelguide.models.Place;
import com.travelguide.models.TripPlan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TripPlanDetailsFragment extends TripBaseFragment
        implements AddUpdatePlaceDetailsFragment.EditItemDialogListener {

    private static final String ARG_TRIP_PLAN_OBJECT_ID = "tripPlanObjectId";
    private static final String ARG_TRIP_PLAN_IMAGE_URL = "tripPlanImageUrl";

    private RecyclerView rvDayDetails;
    private RecyclerView rvPlaceDetails;
    private FloatingActionsMenu floatingActionsMenu;
    private ImageView ivPlace;
    private ImageView ivFavIcon;
    private TextView tvGroupType;
    private TextView tvTravelSeason;

    private String mTripPLanObjectId;
    private String mTripPlanImageUrl;
    private String mSelectedDayObjectId;

    private TripPlan mTripPlan;

    private List<Day> mDayList;
    private List<Place> mPlaceList;

    private PlaceAdapter mPlaceAdapter;
    private DayAdapter mDayAdapter;

    private ArrayList<String> imageUrlSet = new ArrayList<String>();
    private OnTripPlanListener listener;

    public static TripPlanDetailsFragment newInstance(String tripPlanObjectId) {
        TripPlanDetailsFragment fragment = new TripPlanDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TRIP_PLAN_OBJECT_ID, tripPlanObjectId);
        fragment.setArguments(args);
        return fragment;
    }

    public static TripPlanDetailsFragment newInstance(String tripPlanObjectId, String imageUrl) {
        TripPlanDetailsFragment fragment = new TripPlanDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TRIP_PLAN_OBJECT_ID, tripPlanObjectId);
        args.putString(ARG_TRIP_PLAN_IMAGE_URL, imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    public TripPlanDetailsFragment() {
        // Required empty public constructor
    }

    public void hideOrShowFAB() {
        if (floatingActionsMenu != null) {
            if (!Preferences.DEF_VALUE.equals(Preferences.readString(getContext(), Preferences.User.USER_OBJECT_ID))
                    && mTripPlan != null
                    && mTripPlan.getCreatedUserId().equals(Preferences.readString(getContext(), Preferences.User.USER_OBJECT_ID))) {
                floatingActionsMenu.setVisibility(View.VISIBLE);
            } else {
                floatingActionsMenu.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTripPLanObjectId = getArguments().getString(ARG_TRIP_PLAN_OBJECT_ID);
            mTripPlanImageUrl = getArguments().getString(ARG_TRIP_PLAN_IMAGE_URL);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_plan_details, container, false);
        setHasOptionsMenu(true);

        tvGroupType = (TextView) view.findViewById(R.id.tvGroupType);
        tvTravelSeason = (TextView) view.findViewById(R.id.tvTravelSeason);

        ivFavIcon = (ImageView) view.findViewById(R.id.ivFavorite);
        setupFavIconOnClickListener();

        ivPlace = (ImageView) view.findViewById(R.id.ivPlace);
        if (mTripPlanImageUrl != null)
            Glide.with(getContext())
                    .load(mTripPlanImageUrl)
                    .placeholder(R.drawable.city_placeholder)
                    .crossFade()
                    .into(ivPlace);

        floatingActionsMenu = (FloatingActionsMenu) view.findViewById(R.id.multiple_actions);

        //Setup FAB New Place
        final FloatingActionButton fabNewPlace = (FloatingActionButton) view.findViewById(R.id.fabNewPlace);
        fabNewPlace.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddUpdatePlaceDetailsFragment addUpdatePlace = AddUpdatePlaceDetailsFragment
                        .newInstance(TripPlanDetailsFragment.this, mTripPlan.getCityName());
                addUpdatePlace.show(getFragmentManager(), "add_update_place_details_fragment");
                floatingActionsMenu.collapseImmediately();
            }
        });

        final FloatingActionButton fabNewDay = (FloatingActionButton) view.findViewById(R.id.fabNewDay);
        fabNewDay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ParseUser user = ParseUser.getCurrentUser();
                final Day daysDetails = new Day();
                daysDetails.putCreatedUserId(user.getObjectId());

                Day day = mDayAdapter.get(mDayAdapter.getItemCount() - 1);

                daysDetails.putTravelDay(mDayAdapter.getItemCount() + 1);

                Calendar cal = Calendar.getInstance();
                cal.setTime(day.getTravelDate());
                cal.add(Calendar.DATE, 1);
                Date updatedStartDate = cal.getTime();

                daysDetails.putTravelDate(updatedStartDate);
                daysDetails.put("parent", ParseObject.createWithoutData("PlanDetails", mTripPLanObjectId));

                daysDetails.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            addTripPlanDay(daysDetails);
                        }
                    }
                });
                daysDetails.pinInBackground();

                floatingActionsMenu.collapseImmediately();
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
        rvDayDetails.setItemAnimator(new DefaultItemAnimator());

        ItemClickSupport.addTo(rvDayDetails).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View view) {
                Day day = mDayAdapter.get(position);
                selectDay(day, true);
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
        rvPlaceDetails.setItemAnimator(new DefaultItemAnimator());
        rvPlaceDetails.addItemDecoration(itemDecoration);

        ItemClickSupport.addTo(rvPlaceDetails).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                showFullScreenImages();
            }
        });

        return view;
    }

    private void showFullScreenImages() {
        if (listener != null && imageUrlSet != null && imageUrlSet.size() > 0)
            listener.onShowImageSlideShow(imageUrlSet);
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
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnTripPlanListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnTripPlanListener");
        }
    }

    @Override
    public void onFinishEditDialogControl(final String placeName, String travelTime) {
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
                    GoogleImageSearch googleImageSearch = new GoogleImageSearch();
                    googleImageSearch.fetchPlaceImage(placeName, placeDetails.getObjectId(), "CityDetails", new GoogleImageSearch.OnImageFetchListener() {
                        @Override
                        public void onImageFetched(String url) {
                            placeDetails.putPlaceImageUrl(url);
                            if (imageUrlSet == null)
                                imageUrlSet = new ArrayList<>();
                            imageUrlSet.add(url);
                            mPlaceAdapter.notifyDataSetChanged();
                        }
                    });
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

    private void selectDay(Day day, boolean notify) {
        for (Day d: mDayList){
            if (!d.getObjectId().equals(day.getObjectId())) {
                d.setSelected(false);
            }
        }
        day.setSelected(true);
        mSelectedDayObjectId = day.getObjectId();
        loadTripPlacesFromRemote();
        if (notify)
            mDayAdapter.notifyDataSetChanged();
    }

    private void populateTripPlanDays(List<Day> days) {
        mDayList.clear();
        mDayList.addAll(days);
        if (!mDayList.isEmpty()) {
            Day day = mDayList.get(0);
            selectDay(day, false);
        }
        mDayAdapter.notifyDataSetChanged();
    }

    private void addTripPlanDay(Day day) {
        mDayList.add(day);
        Day last = mDayList.get(mDayList.size() - 1);
        selectDay(last, false);
        mDayAdapter.notifyDataSetChanged();
    }

    private void populateTripPlanPlaces(List<Place> places) {
        imageUrlSet = new ArrayList<String>();
        for (Place place : places) {
            if (place.getPlaceImageUrl() != null)
                imageUrlSet.add(place.getPlaceImageUrl());
        }
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
                    Glide.with(getContext())
                            .load(tripPlan.getCityImageUrl())
                            .placeholder(R.drawable.city_placeholder)
                            .crossFade()
                            .into(new ImageViewTarget<GlideDrawable>(ivPlace) {
                                @Override
                                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                    super.onResourceReady(resource, glideAnimation);
                                    ivPlace.setColorFilter(Color.argb(145, 50, 50, 50));
                                }

                                @Override
                                protected void setResource(GlideDrawable resource) {
                                    ivPlace.setImageDrawable(resource);
                                }
                            });
                    mTripPlan = tripPlan;
                    hideOrShowFAB();
                    bindFavoriteIcon();
                    tvGroupType.setText(mTripPlan.getGroupType());
                    tvTravelSeason.setText(mTripPlan.getTravelSeason());
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void bindFavoriteIcon() {
        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) {
            ivFavIcon.setVisibility(View.GONE);
        } else {
            if (user.getObjectId().equals(mTripPlan.getCreatedUserId())) {
                ivFavIcon.setVisibility(View.GONE);
            } else {
                ivFavIcon.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setupFavIconOnClickListener() {
        ivFavIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                mTripPlan.setFavorite(v.isSelected());

                if (v.isSelected()) {
                    ObjectAnimator anim1 = ObjectAnimator.ofFloat(v, "scaleX", 0.8f);
                    ObjectAnimator anim2 = ObjectAnimator.ofFloat(v, "scaleY", 0.8f);
                    AnimatorSet set1 = new AnimatorSet();
                    set1.playTogether(anim1, anim2);
                    ObjectAnimator anim3 = ObjectAnimator.ofFloat(v, "scaleX", 1.0f);
                    ObjectAnimator anim4 = ObjectAnimator.ofFloat(v, "scaleY", 1.0f);
                    AnimatorSet set2 = new AnimatorSet();
                    set2.playTogether(anim3, anim4);
                    AnimatorSet set4 = new AnimatorSet();
                    set4.playSequentially(set1, set2);
                    set4.start();
                } else {
                    ObjectAnimator anim1 = ObjectAnimator.ofFloat(v, "scaleX", 0.8f);
                    ObjectAnimator anim2 = ObjectAnimator.ofFloat(v, "scaleY", 0.8f);
                    AnimatorSet set1 = new AnimatorSet();
                    set1.playTogether(anim1, anim2);
                    ObjectAnimator anim3 = ObjectAnimator.ofFloat(v, "scaleX", 1.0f);
                    ObjectAnimator anim4 = ObjectAnimator.ofFloat(v, "scaleY", 1.0f);
                    AnimatorSet set2 = new AnimatorSet();
                    set2.playTogether(anim3, anim4);
                    AnimatorSet set4 = new AnimatorSet();
                    set4.playSequentially(set1, set2);
                    set4.start();
                }
            }
        });
    }


}
