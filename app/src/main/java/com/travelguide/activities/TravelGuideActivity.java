package com.travelguide.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.parse.ParseFacebookUtils;
import com.travelguide.R;
import com.travelguide.foursquare.constants.FoursquareConstants;
import com.travelguide.fragments.LoginFragment;
import com.travelguide.fragments.NewTripFragment;
import com.travelguide.fragments.ProfileFragment;
import com.travelguide.fragments.SearchListFragment;
import com.travelguide.fragments.TripPlanDetailsFragment;
import com.travelguide.fragments.TripPlanListFragment;
import com.travelguide.listener.OnTripPlanListener;

public class TravelGuideActivity extends AppCompatActivity implements
        OnTripPlanListener,
        FragmentManager.OnBackStackChangedListener,
        ProfileFragment.OnFragmentInteractionListener {

    private DrawerLayout mDrawer;
    private Toolbar toolbar;


    private MaterialDialog settingsDialog;
    private LinearLayout llSettingsDialogLayout;
    private Spinner spnGroup;
    private Spinner spnSeason;

    private String city;
    private String group;
    private String season;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_guide);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Find our drawer view
        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.setDrawerListener(drawerToggle);

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        FoursquareConstants.setClientIdAndSecret(
                getApplicationContext().getResources().getString(R.string.foursquare_client_id),
                getApplicationContext().getResources().getString(R.string.foursquare_client_secret));
        buildSettingsDialog();
        city = "Any";
        group = "Any";
        season = "Any";
        setContentFragment(new TripPlanListFragment());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    // Make sure this is the method with just `Bundle` as the signature
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        drawerToggle.syncState();
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.login_fragment:
                new LoginFragment().show(getSupportFragmentManager(), "Login_with_Facebook");
                break;
            case R.id.profile_fragment:
                setContentFragment(new ProfileFragment());
                break;
            case R.id.settings_fragment:
                showSettingsDialog();
                break;
        }

        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
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
                        setContentFragment(SearchListFragment.newInstance(city, group, season));
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        settingsDialog.dismiss();
                    }
                })
                .build();
        llSettingsDialogLayout = (LinearLayout) settingsDialog.getCustomView();
        spnGroup = (Spinner) llSettingsDialogLayout.findViewById(R.id.spnGroup);
        spnSeason = (Spinner) llSettingsDialogLayout.findViewById(R.id.spnSeason);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("ResultCode", Integer.toString(resultCode));
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_travel_guide_activity, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (TextUtils.isEmpty(query))
                    query = "Any";
                city = query.trim();
                searchItem.collapseActionView();
                setContentFragment(SearchListFragment.newInstance(city, group, season));
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
        if (!(getSupportFragmentManager().getBackStackEntryCount() > 0)) {
            finish();
        }
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

    private void setContentFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void setDisplayHomeAsUpEnabled(boolean showHomeAsUp) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(showHomeAsUp);
        }
    }

    @Override
    public void onBackStackChanged() {
        final View.OnClickListener originalToolbarListener = drawerToggle.getToolbarNavigationClickListener();

        boolean canBack = getSupportFragmentManager().getBackStackEntryCount() > 1;
        if (canBack) {
            drawerToggle.setDrawerIndicatorEnabled(false);
            setDisplayHomeAsUpEnabled(true);
            drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSupportFragmentManager().popBackStack();
                }
            });
        } else {
            setDisplayHomeAsUpEnabled(false);
            drawerToggle.setDrawerIndicatorEnabled(true);
            drawerToggle.setToolbarNavigationClickListener(originalToolbarListener);
        }
    }
}
