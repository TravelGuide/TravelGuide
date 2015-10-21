package com.travelguide.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.travelguide.R;

/**
 * Created by htammare on 10/21/2015.
 */
public class AddUpdatePlaceDetailsFragment extends DialogFragment implements View.OnClickListener {

    public interface EditItemDialogListener {
        void onFinishEditDialogcontrol(String placeName, String travelTime, String dateObjectId);
    }

    EditText placeName;
    EditText travelTime;
    Button cancelbutton;
    Button savebutton;

    public AddUpdatePlaceDetailsFragment() {
        //blank cosntructor needed for for dialog fragment
    }

    public static AddUpdatePlaceDetailsFragment newInstance(String title, String parseObjectId, String date) {
        AddUpdatePlaceDetailsFragment editnamemaster = new AddUpdatePlaceDetailsFragment();
        Bundle bund = new Bundle();
        bund.putString("title", title);
        bund.putString("date", date);
        bund.putString("parseObjectId", parseObjectId);
        editnamemaster.setArguments(bund);
        return editnamemaster;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_update_place_details_fragment, container);
        placeName = (EditText) view.findViewById(R.id.et_PlaceName);
        travelTime = (EditText) view.findViewById(R.id.et_TravelTime);
        cancelbutton = (Button) view.findViewById(R.id.cancelbutton);
        savebutton = (Button) view.findViewById(R.id.savebutton);
        cancelbutton.setOnClickListener(this);
        savebutton.setOnClickListener(this);
        String title = null;
        String Date = null;
        title = getArguments().getString("title", "Add New Place");
        getDialog().setTitle("          " + title);
        placeName.requestFocus();
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view == view.findViewById(R.id.cancelbutton)) {
            getDialog().cancel();
        } else {
            EditItemDialogListener listener = (EditItemDialogListener) getActivity();
            listener.onFinishEditDialogcontrol(placeName.getText().toString(), travelTime.getText().toString(), getArguments().getString("parseObjectId"));
            getDialog().dismiss();
        }
    }
}
