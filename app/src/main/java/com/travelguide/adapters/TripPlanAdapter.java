package com.travelguide.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.travelguide.R;
import com.travelguide.models.TripPlan;

import java.util.List;

public class TripPlanAdapter extends RecyclerView.Adapter<TripPlanAdapter.ViewHolder> {

    private List<TripPlan> mTripPlans;

    public TripPlanAdapter(List<TripPlan> mTripPlans) {
        this.mTripPlans = mTripPlans;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_trip_plan, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TripPlan tripPlan = mTripPlans.get(position);

        holder.tvPlanName.setText("Trip to NYC" + position);
    }

    @Override
    public int getItemCount() {
        return mTripPlans.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlanName;

        public ViewHolder(View itemView) {
            super(itemView);

            tvPlanName = (TextView) itemView.findViewById(R.id.tvPlanName);
        }
    }
}
