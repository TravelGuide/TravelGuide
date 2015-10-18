package com.travelguide.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
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

        holder.tvTotalTravelTime.setText("3 days");
        holder.tvGroupType.setText("Family");
        holder.tvTravelSeason.setText("Winter");
        holder.tvTravelMonth.setText("Jan");
        holder.tvPlanName.setText("Trip to NYC " + position);
        holder.ratingBar.setRating(2);
        holder.tvReviewCount.setText("1500 reviews");
    }

    @Override
    public int getItemCount() {
        return mTripPlans.size();
    }

    public TripPlan get(int position) {
        return mTripPlans.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTotalTravelTime;
        TextView tvGroupType;
        TextView tvTravelSeason;
        TextView tvTravelMonth;
        ImageView ivPlace;
        TextView tvPlanName;
        RatingBar ratingBar;
        TextView tvReviewCount;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTotalTravelTime = (TextView) itemView.findViewById(R.id.tvTotalTravelTime);
            tvGroupType = (TextView) itemView.findViewById(R.id.tvGroupType);
            tvTravelSeason = (TextView) itemView.findViewById(R.id.tvTravelSeason);
            tvTravelMonth = (TextView) itemView.findViewById(R.id.tvTravelMonth);
            ivPlace = (ImageView) itemView.findViewById(R.id.ivPlace);
            tvPlanName = (TextView) itemView.findViewById(R.id.tvPlanName);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            tvReviewCount = (TextView) itemView.findViewById(R.id.tvReviewCount);
        }
    }
}
