package com.travelguide.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.travelguide.R;
import com.travelguide.fragments.NewTripFragment;

public class NewTavelPlanActivity extends AppCompatActivity  {
    TextView startDate;
    TextView endDate;

    String clickedDate;
    String travellerType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tavel_plan);
        newTripFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_tavel_plan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void newTripFragment() {
        // create a fragment transaction
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // replace contents of FrameLayout with FirstFragment
        fragmentTransaction.replace(R.id.fl_placeholder, new NewTripFragment());
        fragmentTransaction.addToBackStack(null);
        // commit the transaction
        fragmentTransaction.commit();
    }
}
