package com.travelguide.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.travelguide.R;
import com.travelguide.models.Day;
import com.travelguide.models.TripPlan;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by htammare on 10/20/2015.
 */
public class NewTripFragment extends Fragment implements DatePickerFragment.DatePickerDialogFragmentEvents {
    TextView startDate;
    TextView endDate;
    EditText planName;
    EditText destination;
    String clickedDate;
    String travellerType;
    View topLevelView;
    Button male;
    Button female;
    Button couple;
    Button family;
    Button save;
    Integer sDateMonth;
    String sSeason;
    String sMonth;
    ProgressDialog progressDialog;
    Integer totalTravelDays;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        topLevelView = inflater.inflate(R.layout.fragment_new_plan_creation, container, false);
        planName = (EditText) topLevelView.findViewById(R.id.tv_PlanName);
        destination = (EditText) topLevelView.findViewById(R.id.et_PlaceName);
        startDate = (TextView) topLevelView.findViewById(R.id.tv_StartDate);
        endDate = (TextView) topLevelView.findViewById(R.id.tv_EndDate);
        save = (Button) topLevelView.findViewById(R.id.btn_CreatePlan);
        travellerButtons();
        datesButtons();
        saveButton();
        return topLevelView;
    }

    public void travellerButtons() {
        male = (Button) topLevelView.findViewById(R.id.btn_Male);
        female = (Button) topLevelView.findViewById(R.id.btn_Female);
        couple = (Button) topLevelView.findViewById(R.id.btn_Couple);
        family = (Button) topLevelView.findViewById(R.id.btn_Family);
        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //View maleIcon = v.findViewById(R.id.btn_Male);
                male.setBackground(getResources().getDrawable(R.drawable.button_pressed));
                female.setBackground(getResources().getDrawable(R.drawable.edit_text_style));
                couple.setBackground(getResources().getDrawable(R.drawable.edit_text_style));
                family.setBackground(getResources().getDrawable(R.drawable.edit_text_style));
                travellerType = "Male";
            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                female.setBackground(getResources().getDrawable(R.drawable.button_pressed));
                male.setBackground(getResources().getDrawable(R.drawable.edit_text_style));
                couple.setBackground(getResources().getDrawable(R.drawable.edit_text_style));
                family.setBackground(getResources().getDrawable(R.drawable.edit_text_style));
                travellerType = "Female";
            }
        });
        couple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                couple.setBackground(getResources().getDrawable(R.drawable.button_pressed));
                female.setBackground(getResources().getDrawable(R.drawable.edit_text_style));
                male.setBackground(getResources().getDrawable(R.drawable.edit_text_style));
                family.setBackground(getResources().getDrawable(R.drawable.edit_text_style));
                travellerType = "Couple";
            }
        });
        family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                family.setBackground(getResources().getDrawable(R.drawable.button_pressed));
                couple.setBackground(getResources().getDrawable(R.drawable.edit_text_style));
                female.setBackground(getResources().getDrawable(R.drawable.edit_text_style));
                travellerType = "Family";
            }
        });
    }

    public void datesButtons() {
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.setDatePickerDialogFragmentEvents(NewTripFragment.this);
                newFragment.show(getFragmentManager(), "datePicker");
                clickedDate = "SD";
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.setDatePickerDialogFragmentEvents(NewTripFragment.this);
                newFragment.show(getFragmentManager(), "datePicker");
                clickedDate = "ED";
            }
        });
    }

    @Override
    public void onDateSelected(String date, Integer month, String monthName, String season) {
        if (clickedDate.equalsIgnoreCase("SD")) {
            startDate.setText(date);
            sDateMonth = month;
            sSeason = season;
            sMonth = monthName;

        } else {
            endDate.setText(date);
        }
        clickedDate = "";
    }

    public void saveButton() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (planName.getText().toString().trim().equals("")) {
                    planName.setError("Plan name is required!");
                } else if (destination.getText().toString().trim().equals("")) {
                    destination.setError("Destination is required!");
                } else if (startDate.getText().toString().trim().equals("")) {
                    startDate.setError("Plan start date is required!");
                } else {
                    saveAndStartDetailsFragment();
                }
            }
        });
    }

    public void saveAndStartDetailsFragment() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Creating Plan...");
        progressDialog.setMessage("Please wait.");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ParseUser user = ParseUser.getCurrentUser();
        final TripPlan planDetails = new TripPlan();
        planDetails.putCreatedUserId(user.getObjectId());
        planDetails.putCreatedUserName(user.getUsername());
        planDetails.putPlanName(planName.getText().toString());
        planDetails.putCityName(destination.getText().toString());
        planDetails.putTravelMonthNumber(sDateMonth + 1);
        planDetails.putTravelMonth(sMonth);
        planDetails.putTravelSeason(sSeason);
        planDetails.putTripBeginDate(simpelDateFormat(startDate.getText().toString()));
        if (!endDate.getText().toString().equals("")) {
            planDetails.putTripEndDate(simpelDateFormat(endDate.getText().toString()));
        }
        planDetails.putTripNotes("");
        planDetails.putTripCost(0);
        planDetails.putGroupType(travellerType);
        if (endDate.getText().toString().equals("")) {
            totalTravelDays = 1;
            planDetails.putTripTime(totalTravelDays);
        } else {
            totalTravelDays = daysDifference(simpelDateFormat(startDate.getText().toString()), simpelDateFormat(endDate.getText().toString()));
            planDetails.putTripTime(totalTravelDays);
        }
        planDetails.puCityImageURL("http://thenextweb.com/wp-content/blogs.dir/1/files/2013/09/nyc.jpg");
        planDetails.putEnabledFlag(false);
        planDetails.saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    String parsePlanID = planDetails.getObjectId();
                    Log.d("TAG", "The object id is: " + parsePlanID);
                    saveDayDetails(parsePlanID, totalTravelDays, planName.getText().toString(), simpelDateFormat(startDate.getText().toString()));
                    progressDialog.dismiss();
                }
            }
        });
    }

    public Date simpelDateFormat(String date) {
        Date dateFormated = null;
        DateFormat format = new SimpleDateFormat("MMM/dd/yyyy", Locale.ENGLISH);
        try {
            dateFormated = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormated;
    }

    public int daysDifference(Date startDate, Date endDate) {
        long diff = endDate.getTime() - startDate.getTime();
        int noofDays = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        return noofDays;
    }

    public Boolean successCheck = false;

    public Boolean saveDayDetails(String parsePlanID, int totalTravelDays, String planName, Date startDate) {
        ParseUser user = ParseUser.getCurrentUser();
        Integer trackCount = 0;
        if (totalTravelDays != 1) {
            totalTravelDays = totalTravelDays + 1;
        }
        Date updatedStartDate = null;
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
            daysDetails.saveInBackground(new SaveCallback() {
                @Override
                public void done(com.parse.ParseException e) {
                    if (e == null) {
                        //String parsePlanID = planDetails.getObjectId();
                        successCheck = true;
                    }
                }
            });
        }
        return successCheck;
    }
}
