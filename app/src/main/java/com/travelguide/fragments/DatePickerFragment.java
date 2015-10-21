package com.travelguide.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by htammare on 10/20/2015.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    DatePickerDialogFragmentEvents dpdfe;
    //Interface created for communicating this dialog fragment events to called fragment
    public interface DatePickerDialogFragmentEvents {
        void onDateSelected(String date,Integer month,String monthName,String season);
    }
    public void setDatePickerDialogFragmentEvents(DatePickerDialogFragmentEvents dpdfe) {
        this.dpdfe = dpdfe;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }
    public void onDateSet(DatePicker view, int year, int month, int day) {
        String monthName = "";
        String season = "";
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yyyy");
        String formattedDate = sdf.format(c.getTime());
        switch (month) {
            case 0: monthName = "Jan";season = "Winter";break;
            case 1: monthName = "Feb";season = "Winter";break;
            case 2: monthName = "Mar";season = "Spring ";break;
            case 3: monthName = "Apr";season = "Spring";break;
            case 4: monthName = "May";season = "Spring";break;
            case 5: monthName = "Jun";season = "Spring";break;
            case 6: monthName = "Jul";season = "Summer";break;
            case 7: monthName = "Aug";season = "Summer";break;
            case 8: monthName = "Sep";season = "Summer";break;
            case 9: monthName = "Oct";season = "Fall";break;
            case 10: monthName = "Nov";season = "Fall";break;
            case 11: monthName = "Dec";season = "Winter";break;
        }
        dpdfe.onDateSelected(formattedDate,month,monthName,season); //Changed
        getDialog().dismiss();
    }
}