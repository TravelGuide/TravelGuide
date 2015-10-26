package com.travelguide.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.parse.ParseFacebookUtils;
import com.travelguide.R;
import com.travelguide.fragments.LoginFragment;
import com.travelguide.fragments.NewTripFragment;
import com.travelguide.fragments.ProfileFragment;
import com.travelguide.fragments.TripPlanDetailsFragment;
import com.travelguide.fragments.TripPlanListFragment;
import com.travelguide.listener.OnTripPlanListener;

public class TravelGuideActivity extends AppCompatActivity implements
        OnTripPlanListener,
        ProfileFragment.OnFragmentInteractionListener {

    private MaterialDialog settingsDialog;
    private LinearLayout llsettingsDialogLayout;
    private Spinner spnGroup;
    private Spinner spnSeason;

    private String city;
    private String group;
    private String season;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_guide);
        buildSettingsDialog();
        city = "Any";
        group = "Any";
        season = "Any";
        // setContentFragment(new TripPlanListFragment());
        setContentFragment(TripPlanListFragment.newInstance(city, group, season));
    }

    public void buildSettingsDialog() {
        settingsDialog = new MaterialDialog.Builder(this)
                .title(R.string.label_settings)
                .customView(R.layout.dialog_settings, true)
                .positiveText(R.string.label_save_button)
                .negativeText(R.string.label_cancel_button)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        setContentFragment(TripPlanListFragment.newInstance(city, group, season));
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        settingsDialog.dismiss();
                    }
                })
                .build();
        llsettingsDialogLayout = (LinearLayout) settingsDialog.getCustomView();
        spnGroup = (Spinner) llsettingsDialogLayout.findViewById(R.id.spnGroup);
        spnSeason = (Spinner) llsettingsDialogLayout.findViewById(R.id.spnSeason);
        setupSpinnerGroup();
        setupSpinnerSeason();
    }

    private void setupSpinnerGroup() {
        spnGroup.setSelection(getSpinnerIndex(spnGroup, group));
        spnGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                group = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do Nothing
            }
        });
    }

    private void setupSpinnerSeason() {
        spnSeason.setSelection(getSpinnerIndex(spnSeason, season));
        spnSeason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                season = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do Nothing
            }
        });
    }

    // Get the position of an Spinner item
    private int getSpinnerIndex(Spinner spinner, String myString) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
            }
        }
        return index;
    }

    public void showSettingsDialog() {
        settingsDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisplayHomeAsUpEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("ResultCode", Integer.toString(resultCode));
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_travel_guide_activity, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null)
                    query = query.trim().toLowerCase();
                if (query == null || query.trim().equals(""))
                    query = "Any";
                city = query;
                Log.d("CITY", query);
                setContentFragment(TripPlanListFragment.newInstance(city, group, season));
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onTripPlanItemSelected(String tripPlanObjectId) {
        TripPlanDetailsFragment fragment = TripPlanDetailsFragment.newInstance(tripPlanObjectId);
        setContentFragment(fragment);
    }

    @Override
    public void onTripPlanNew() {
        setContentFragment(new NewTripFragment());
    }

    @Override
    public void onTripPlanCreated(String tripPlanObjectId) {
        //Opening details passing ID of new item
        TripPlanDetailsFragment fragment = TripPlanDetailsFragment.newInstance(tripPlanObjectId);
        setContentFragment(fragment);
    }

    public void onSettingsClick(MenuItem item) {
        Toast.makeText(this, "Open settings", Toast.LENGTH_SHORT).show();
        showSettingsDialog();
    }

    public void onLoginClick(MenuItem item) {
        new LoginFragment().show(getSupportFragmentManager(), "Login_with_Facebook");
    }

    public void onProfileClick(MenuItem item) {
        setContentFragment(new ProfileFragment());
    }

    private void setContentFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        setDisplayHomeAsUpEnabled(true);
    }

    private void setDisplayHomeAsUpEnabled(boolean showHomeAsUp) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(showHomeAsUp);
        }
    }
}
