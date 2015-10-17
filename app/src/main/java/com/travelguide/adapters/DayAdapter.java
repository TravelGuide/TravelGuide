package com.travelguide.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.travelguide.R;
import com.travelguide.models.Day;

import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {

    private List<Day> mDays;

    public DayAdapter(List<Day> days) {
        this.mDays = days;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_day, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Day day = mDays.get(position);

        holder.tvDay.setText("18");
        holder.tvMonth.setText("Oct");
    }

    @Override
    public int getItemCount() {
        return mDays.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvDay;
        TextView tvMonth;

        public ViewHolder(View itemView) {
            super(itemView);

            tvDay = (TextView) itemView.findViewById(R.id.tvDay);
            tvMonth = (TextView) itemView.findViewById(R.id.tvMonth);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition(); // gets item position
            Log.d("DayAdapter", "position: " + position);
        }
    }
}
