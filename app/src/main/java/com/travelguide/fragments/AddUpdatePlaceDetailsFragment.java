package com.travelguide.fragments;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.travelguide.R;
import com.travelguide.helpers.NearbyPlacesFoursquare;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by htammare on 10/21/2015.
 */
public class AddUpdatePlaceDetailsFragment extends DialogFragment implements View.OnClickListener {

    AutoCompleteTextView placeName;
    EditText travelTime;
    Button cancelbutton;
    Button savebutton;
    private ArrayList<String> placeNames;


    private EditItemDialogListener listener;

    public interface EditItemDialogListener extends Serializable {
        void onFinishEditDialogControl(String placeName, String travelTime);
    }

    public AddUpdatePlaceDetailsFragment() {
        //blank cosntructor needed for for dialog fragment
    }

    public static AddUpdatePlaceDetailsFragment newInstance(String title, String placeName, String time, EditItemDialogListener listener,String CityName) {
        AddUpdatePlaceDetailsFragment editnamemaster = new AddUpdatePlaceDetailsFragment();
        Bundle bund = new Bundle();
        bund.putString("title", title);
        bund.putString("time", time);
        bund.putString("placeName", placeName);
        bund.putSerializable("listener", listener);
        bund.putString("cityName", CityName);
        editnamemaster.setArguments(bund);
        return editnamemaster;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_update_place_details_fragment, container);
        placeName = (AutoCompleteTextView) view.findViewById(R.id.et_PlaceName);
        travelTime = (EditText) view.findViewById(R.id.et_TravelTime);
        cancelbutton = (Button) view.findViewById(R.id.cancelbutton);
        savebutton = (Button) view.findViewById(R.id.savebutton);
        cancelbutton.setOnClickListener(this);
        savebutton.setOnClickListener(this);
        String title = null;
        String Date = null;
        title = getArguments().getString("title", "Add New Place");
        listener = (EditItemDialogListener) getArguments().getSerializable("listener");
        getDialog().setTitle("          " + title);
        placeName.requestFocus();
        placeNames = new ArrayList<String>();
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, placeNames);
        placeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start >= 0) {
                    getPlaceDetails();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view == view.findViewById(R.id.cancelbutton)) {
            getDialog().cancel();
        } else {
            getDialog().dismiss();
            if (listener != null) {
                listener.onFinishEditDialogControl(placeName.getText().toString(), travelTime.getText().toString());
            }
        }
    }

    private void getPlaceDetails() {
        Double latitude = null;
        Double longitude = null;
        Geocoder gc = new Geocoder(getContext(), Locale.ENGLISH);
        List<Address> address;
        if (placeNames.size() == 0) {
            try {
                address = gc.getFromLocationName(getArguments().getString("cityName", "NYC"), 1);
                if (address.size() > 0) {
                    placeNames.clear();
                    //Log.e("Lat", Double.toString(address.get(0).getLatitude()));
                    //Log.e("Long", Double.toString(address.get(0).getLongitude()));
                    latitude = address.get(0).getLatitude();
                    longitude = address.get(0).getLongitude();
                    NearbyPlacesFoursquare.getNearbyPlaces(getActivity(), latitude, longitude,
                            new NearbyPlacesFoursquare.OnPlacesFetchListener() {
                                @Override
                                public void onPlacesFetched(TreeMap<String, Location> places) {
                                    for (Map.Entry<String, Location> place : places.entrySet()) {
                                        //Log.d("Place", place.getValue().getLatitude() + "\t" + place.getValue().getLongitude() + "\t" + place.getKey());
                                        placeNames.add(place.getKey());
                                    }
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, placeNames);
                                    placeName.setAdapter(adapter);
                                    placeName.showDropDown();
                                }
                            });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
