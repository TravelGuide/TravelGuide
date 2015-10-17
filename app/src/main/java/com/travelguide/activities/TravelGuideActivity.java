package com.travelguide.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.travelguide.R;
import com.travelguide.fragments.LoginFragment;
import com.travelguide.fragments.TripPlanDetailsFragment;

public class TravelGuideActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_guide);

        // create a fragment transaction
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // replace contents of FrameLayout with FirstFragment
        fragmentTransaction.replace(R.id.fragment_frame, new LoginFragment());
        // commit the transaction
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_travel_guide_activity, menu);
        return true;
    }

    /**
     * Those methods we should use for test purposes in order to access our
     * fragments directly, since we don't have a way to access them.
     */

    public void tripPlanDetailsFragment(MenuItem item) {
        // create a fragment transaction
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // replace contents of FrameLayout with FirstFragment
        fragmentTransaction.replace(R.id.fragment_frame, new TripPlanDetailsFragment());
        fragmentTransaction.addToBackStack(null);
        // commit the transaction
        fragmentTransaction.commit();
    }

    public void loginFragment(MenuItem item) {
        // create a fragment transaction
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // replace contents of FrameLayout with FirstFragment
        fragmentTransaction.replace(R.id.fragment_frame, new LoginFragment());
        fragmentTransaction.addToBackStack(null);
        // commit the transaction
        fragmentTransaction.commit();
    }
}
