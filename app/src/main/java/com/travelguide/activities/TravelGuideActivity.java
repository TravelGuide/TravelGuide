package com.travelguide.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ParseFacebookUtils;
import com.travelguide.R;
import com.travelguide.fragments.LoginFragment;
import com.travelguide.fragments.ProfileFragment;
import com.travelguide.fragments.TripPlanDetailsFragment;
import com.travelguide.fragments.TripPlanListFragment;

public class TravelGuideActivity extends AppCompatActivity implements
        TripPlanListFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener {

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
    public void onTripPlanItemSelected(String tripPlanObjectId) {
        Toast.makeText(this, tripPlanObjectId + " was clicked!", Toast.LENGTH_SHORT).show();
        // Opening PlanDetailsFragment
        TripPlanDetailsFragment fragment = TripPlanDetailsFragment.newInstance(tripPlanObjectId);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.fragment_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        // commit the transaction
        fragmentTransaction.commit();
    }

    /**
     * Those methods we should use for test purposes in order to access our
     * fragments directly, since we don't have a way to access them.
     */

    public void loginFragment(MenuItem item) {
        // create a fragment transaction
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // replace contents of FrameLayout with FirstFragment
        fragmentTransaction.replace(R.id.fragment_frame, new LoginFragment());
        fragmentTransaction.addToBackStack(null);
        // commit the transaction
        fragmentTransaction.commit();
    }

    public void tripPlanListFragment(MenuItem item) {
        // create a fragment transaction
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // replace contents of FrameLayout with FirstFragment
        fragmentTransaction.replace(R.id.fragment_frame, new TripPlanListFragment());
        fragmentTransaction.addToBackStack(null);
        // commit the transaction
        fragmentTransaction.commit();
    }

    public void profileFragment(MenuItem item) {
        // create a fragment transaction
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // replace contents of FrameLayout with FirstFragment
        fragmentTransaction.replace(R.id.fragment_frame, new ProfileFragment());
        fragmentTransaction.addToBackStack(null);
        // commit the transaction
        fragmentTransaction.commit();
    }
    public void newTravelPlanActivity(MenuItem item) {
        Intent intent = new Intent(this,NewTavelPlanActivity.class);
        startActivity(intent);
    }
}
