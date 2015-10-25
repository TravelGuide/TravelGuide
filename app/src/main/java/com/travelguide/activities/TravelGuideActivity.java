package com.travelguide.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_guide);
        setContentFragment(new TripPlanListFragment());
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
    }

    public void onLoginClick(MenuItem item) {
        setContentFragment(new LoginFragment());
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
