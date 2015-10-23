package com.travelguide.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.travelguide.R;
import com.travelguide.fragments.TripPlanDetailsFragment;
import com.travelguide.models.Day;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {
    private List<Day> mDays;
    public static ViewHolder.DayClickedListener listener;

    public DayAdapter(List<Day> days,TripPlanDetailsFragment context) {
        this.mDays = days;
        this.listener = context.dayClickedListener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_day, parent, false);

        /*
        DayAdapter.ViewHolder vhs = new ViewHolder(contactView, new DayAdapter.ViewHolder.DayClickedListner() {
            @Override
            public void onDayClicked(View caller) {
                //  NEED TO CALL FRAGMENT "onDayClicked" rather than this
            }
        });
        */

        DayAdapter.ViewHolder vh = new ViewHolder(contactView, listener);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String monthName = "";
        String season;
        Date travelDate = mDays.get(position).getTravelDate();
        Integer months = mDays.get(position).getTravelDay();
        //Use calender to get month and day details
        Calendar cal = Calendar.getInstance();
        cal.setTime(travelDate);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
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
        holder.tvDay.setText(Integer.toString(day));
        holder.tvMonth.setText(monthName);
        holder.tvObjectId.setText(mDays.get(position).getObjectId());
    }

    @Override
    public int getItemCount() {
        return mDays.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvDay;
        TextView tvMonth;
        TextView tvObjectId;
        RelativeLayout relativeLayout;
        //public DayClickedListner dayClickedListner;
        public ViewHolder(View itemView,DayClickedListener dayListener) {
            super(itemView);
            listener = dayListener;
            tvDay = (TextView) itemView.findViewById(R.id.tvDay);
            tvMonth = (TextView) itemView.findViewById(R.id.tvMonth);
            tvObjectId = (TextView) itemView.findViewById(R.id.tv_objectID);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int position = getLayoutPosition(); // gets item position
            Log.d("DayAdapter", "position: " + position);
            listener.onDayClicked(v);
        }

        public interface DayClickedListener {
            void onDayClicked(View caller);
        }

    }


}
