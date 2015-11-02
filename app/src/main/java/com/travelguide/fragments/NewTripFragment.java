package com.travelguide.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.travelguide.R;
import com.travelguide.adapters.DateSetAdapter;
import com.travelguide.helpers.GoogleImageSearch;
import com.travelguide.listener.OnTripPlanListener;
import com.travelguide.models.Day;
import com.travelguide.models.TripPlan;
import com.travelguide.viewmodel.TripDateViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.text.TextUtils.isEmpty;
import static com.travelguide.helpers.DateUtils.daysDifference;
import static com.travelguide.helpers.DateUtils.parse;

/**
 * Created by htammare on 10/20/2015.
 */
public class NewTripFragment extends Fragment {

    private static final String TAG = NewTripFragment.class.getSimpleName();

    private TextView startDate;
    private TextView endDate;
    private EditText planName;
    private AutoCompleteTextView destination;
    private ProgressDialog progressDialog;

    private TripDateViewModel startDateViewModel;
    private TripDateViewModel endDateViewModel;

    private OnTripPlanListener mListener;

    private String travellerType;
    private String parseNewTripObjectId;
    private Integer totalTravelDays;
    private ArrayList<String> cityNames;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_new_plan_creation, container, false);

        cityNames = new ArrayList<String>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, cityNames);
        planName = (EditText) view.findViewById(R.id.tv_PlanName);
        destination = (AutoCompleteTextView) view.findViewById(R.id.actv_PlaceName);
        destination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start >= 2) {
                    fetchCityNames();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        startDate = (TextView) view.findViewById(R.id.tv_StartDate);
        startDate.setOnClickListener(startDateOnClickListener());

        endDate = (TextView) view.findViewById(R.id.tv_EndDate);
        endDate.setOnClickListener(endDateOnClickListener());

        RadioGroup rgGroupType = (RadioGroup) view.findViewById(R.id.rgGroupType);
        rgGroupType.setOnCheckedChangeListener(groupTypeOnCheckedChangeListener());

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnTripPlanListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnTripPlanListener");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_plan_create, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getFragmentManager().popBackStack();
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                return true;
            case R.id.action_done:
                done();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @NonNull
    private RadioGroup.OnCheckedChangeListener groupTypeOnCheckedChangeListener() {
        return new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbMale:
                        travellerType = getString(R.string.male);
                        break;
                    case R.id.rbFemale:
                        travellerType = getString(R.string.female);
                        break;
                    case R.id.rbCouple:
                        travellerType = getString(R.string.couple);
                        break;
                    case R.id.rbFamily:
                        travellerType = getString(R.string.family);
                        break;
                }
                Log.d(TAG, "travellerType: " + travellerType);
            }
        };
    }

    private View.OnClickListener endDateOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(endDateSelected());
                datePickerFragment.show(getFragmentManager(), "datePicker");
            }
        };
    }

    private DateSetAdapter endDateSelected() {
        return new DateSetAdapter() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                endDateViewModel = new TripDateViewModel(year, monthOfYear, dayOfMonth);
                endDate.setText(endDateViewModel.getFormattedDate());
                Log.d(TAG, "endDateSelected: " + endDateViewModel);
            }
        };
    }

    private View.OnClickListener startDateOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(startDateSelected());
                datePickerFragment.show(getFragmentManager(), "datePicker");
            }
        };
    }

    private DateSetAdapter startDateSelected() {
        return new DateSetAdapter() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                startDateViewModel = new TripDateViewModel(year, monthOfYear, dayOfMonth);
                startDate.setText(startDateViewModel.getFormattedDate());
                Log.d(TAG, "startDateSelected: " + startDateViewModel);
            }
        };
    }

    private void done() {
        boolean valid = true;

        if (isEmpty(planName.getText().toString().trim())) {
            planName.setError(getString(R.string.plan_name_is_required));
            valid = false;
        }

        if (isEmpty(destination.getText().toString().trim())) {
            destination.setError(getString(R.string.destination_is_required));
            valid = false;
        }

        if (startDateViewModel == null) {
            startDate.setError(getString(R.string.plan_start_date_is_required));
            valid = false;
        }

        if (valid) {
            saveAndStartDetailsFragment();
        }
    }

    private void saveAndStartDetailsFragment() {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle(R.string.creating_plan);
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.setCancelable(false);
            progressDialog.show();

            ParseUser user = ParseUser.getCurrentUser();

            final TripPlan planDetails = new TripPlan();
            planDetails.putCreatedUserId(user.getObjectId());
            planDetails.putCreatedUserName(user.getUsername());
            planDetails.putPlanName(planName.getText().toString());
            planDetails.putCityName(destination.getText().toString());
            planDetails.putTravelMonthNumber(startDateViewModel.getMonthNumber() + 1);
        planDetails.putTravelMonth(startDateViewModel.getMonthName());
        planDetails.putTravelSeason(startDateViewModel.getSeasonName());

        planDetails.putTripBeginDate(startDateViewModel.getParsedDate());

        if (endDateViewModel != null) {
            planDetails.putTripEndDate(endDateViewModel.getParsedDate());
        }

        planDetails.putTripNotes("");
        planDetails.putTripCost(0);
        planDetails.putGroupType(travellerType);
        if (endDate.getText().toString().equals("")) {
            totalTravelDays = 1;
            planDetails.putTripTime(totalTravelDays);
        } else {
            totalTravelDays = daysDifference(parse(startDate.getText().toString()), parse(endDate.getText().toString()));
            planDetails.putTripTime(totalTravelDays);
        }
        planDetails.puCityImageURL("http://thenextweb.com/wp-content/blogs.dir/1/files/2013/09/nyc.jpg");
        planDetails.putEnabledFlag(false);

        planDetails.saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    parseNewTripObjectId = planDetails.getObjectId();
                    Log.d(TAG, "The object id is: " + parseNewTripObjectId);
                    GoogleImageSearch googleImageSearch = new GoogleImageSearch();
                    googleImageSearch.fetchPlaceImage(destination.getText().toString(), parseNewTripObjectId);
                    saveDayDetails(parseNewTripObjectId, totalTravelDays, planName.getText().toString(), parse(startDate.getText().toString()));
                }
            }
        });
    }

    private void saveDayDetails(String parsePlanID, int totalTravelDays, String planName, Date startDate) {
        ParseUser user = ParseUser.getCurrentUser();
        Integer trackCount = 0;
        if (totalTravelDays != 1) {
            totalTravelDays = totalTravelDays + 1;
        }
        Date updatedStartDate = null;
        List<Day> dayList = new ArrayList<Day>();

        for (int i = 0; i < totalTravelDays; i++) {
            if (trackCount == 0) {
                updatedStartDate = startDate;
            } else {
                Calendar cal = Calendar.getInstance();
                cal.setTime(updatedStartDate);
                cal.add(Calendar.DATE, 1);
                updatedStartDate = cal.getTime();
            }
            trackCount = trackCount + 1;
            Day daysDetails = new Day();
            daysDetails.putCreatedUserId(user.getObjectId());
            daysDetails.putPlanName(planName);
            daysDetails.putTravelDay(trackCount);
            daysDetails.putTravelDate(updatedStartDate);
            daysDetails.put("parent", ParseObject.createWithoutData("PlanDetails", parsePlanID));
            dayList.add(daysDetails);
        }
        ParseObject.saveAllInBackground(dayList, new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    progressDialog.dismiss();
                    getFragmentManager().popBackStack();
                    if (mListener != null) {
                        mListener.onTripPlanCreated(parseNewTripObjectId);
                    }
                }
            }
        });
    }

    private void fetchCityNames() {
        cityNames.clear();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("CityDetails");
        query.whereEqualTo("CountryCode", "US");
        query.whereEqualTo("TargetType", "City");
        // query.whereContains("CanonicalName", destination.getText().toString());
        query.whereStartsWith("CanonicalName", destination.getText().toString());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (int i = 0; i < list.size(); i++) {
                    cityNames.add(list.get(i).getString("CanonicalName").trim());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, cityNames);
                destination.setAdapter(adapter);
                destination.showDropDown();
            }
        });
    }

}
