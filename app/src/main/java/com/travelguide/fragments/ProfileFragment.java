package com.travelguide.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;
import com.travelguide.R;
import com.travelguide.adapters.TripPlanAdapter;
import com.travelguide.decorations.VerticalSpaceItemDecoration;
import com.travelguide.helpers.DeviceDimensionsHelper;
import com.travelguide.helpers.ItemClickSupport;
import com.travelguide.models.TripPlan;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

// TODO: Allow access to ProfileFragment only if user has logged in previously

/**
 * @author USPRKAN
 *
 * History:
 *   18-Oct-2015    kprav       Initial Version
 */
public class ProfileFragment extends Fragment {
    private static final String TAG = ProfileFragment.class.getSimpleName();

    private CircleImageView ivProfilePic;
    private ImageView ivCoverPic;
    private TextView tvName;
    private TextView tvEmail;

    private String userObjectId = null;
    private String name = null;
    private String email = null;
    private String profilePicUrl = null;
    private String coverPicUrl = null;

    private SharedPreferences userInfo;

    private OnFragmentInteractionListener mListener;
    private TripPlanAdapter mTripPlanAdapter;
    private List<TripPlan> mTripPlans;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSharedPreferences();
        if (userObjectId.equals("missing")) {
            Toast.makeText(getActivity(), "No logged in user! Cannot view profile!!", Toast.LENGTH_SHORT).show();
            onStop();
            onDestroy();
            onDetach();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ivCoverPic = (ImageView) view.findViewById(R.id.ivCoverPicInProfile);
        ivProfilePic = (CircleImageView) view.findViewById(R.id.ivProfilePicInProfile);
        tvName = (TextView) view.findViewById(R.id.tvNameInProfile);
        tvEmail = (TextView) view.findViewById(R.id.tvEmailInProfile);

        setHasOptionsMenu(true);


        tvName.setText(name);
        tvEmail.setText(email);
        Picasso.with(getContext()).load(profilePicUrl).into(ivProfilePic);
        Picasso.with(getContext()).load(coverPicUrl).resize(DeviceDimensionsHelper.getDisplayWidth(getActivity()), 0).into(ivCoverPic);

        RecyclerView rvTripPlans = (RecyclerView) view.findViewById(R.id.rvTripPlansInProfile);
        mTripPlans = new ArrayList<>();
        mTripPlanAdapter = new TripPlanAdapter(mTripPlans, getContext());
        // Attach the adapter to the recyclerview to populate items
        rvTripPlans.setAdapter(mTripPlanAdapter);
        // Set layout manager to position the items
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvTripPlans.setLayoutManager(layoutManager);

        RecyclerView.ItemDecoration itemDecoration = new VerticalSpaceItemDecoration(20, true, true);
        rvTripPlans.addItemDecoration(itemDecoration);

        ItemClickSupport.addTo(rvTripPlans).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                String tripPlanObjectId = mTripPlanAdapter.get(position).getObjectId();
                if (mListener != null) {
                    mListener.onTripPlanItemSelected(tripPlanObjectId);
                }
            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getFragmentManager().popBackStack();
                ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getSharedPreferences() {
        userInfo = getActivity().getSharedPreferences("userInfo", 0);
        userObjectId = userInfo.getString("userObjectId", "missing");
        name = userInfo.getString("name", "missing");
        email = userInfo.getString("email", "missing");
        profilePicUrl = userInfo.getString("profilePicUrl", "missing");
        coverPicUrl = userInfo.getString("coverPicUrl", "missing");
    }

    private void populateTripPlanList() {
        // Construct query to execute
        ParseQuery<TripPlan> query = ParseQuery.getQuery(TripPlan.class);
        query.whereEqualTo("createdUserId", userObjectId);
        query.findInBackground(new FindCallback<TripPlan>() {
            @Override
            public void done(List<TripPlan> tripPlans, ParseException e) {
                if (e == null) {
                    mTripPlans.clear();
                    mTripPlans.addAll(tripPlans);
                    mTripPlanAdapter.notifyDataSetChanged();
                    savingOnDatabase(tripPlans);
                } else {
                    Log.d(TAG, "Error: " + e.getMessage());
                }
            }
        });
    }

    private void savingOnDatabase(List<TripPlan> tripPlans) {
        for (TripPlan tp : tripPlans)
            tp.pinInBackground();
        //TODO Investigate why "ParseObject.saveAll(tripPlans);" not working.
//        try {
//            ParseObject.saveAll(tripPlans);
//        } catch (ParseException e1) {
//            e1.printStackTrace();
//        }
    }

    public interface OnFragmentInteractionListener {
        void onTripPlanItemSelected(String tripPlanObjectId);
    }

    @Override
    public void onResume() {
        super.onResume();
        populateTripPlanList();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
