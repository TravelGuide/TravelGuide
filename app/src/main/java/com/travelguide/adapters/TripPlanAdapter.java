package com.travelguide.adapters;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.travelguide.R;
import com.travelguide.models.TripPlan;

import java.util.List;

public class TripPlanAdapter extends RecyclerView.Adapter<TripPlanAdapter.ViewHolder> {

    private final Context mContext;
    private final List<TripPlan> mTripPlans;

    public TripPlanAdapter(List<TripPlan> mTripPlans, Context context) {
        this.mTripPlans = mTripPlans;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View contactView = inflater.inflate(R.layout.item_trip_plan, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TripPlan tripPlan = mTripPlans.get(position);

        holder.tvPlanName.setText(tripPlan.getPlanName());

        String totalTravelTime = mContext.getResources()
                .getQuantityString(R.plurals.days, tripPlan.getTripTime(), tripPlan.getTripTime());

        holder.tvTotalTravelTime.setText(totalTravelTime);
        holder.tvGroupType.setText(tripPlan.getGroupType());
        holder.tvTravelSeason.setText(tripPlan.getTravelSeason());
        holder.tvTravelMonth.setText(tripPlan.getTravelMonth());

        holder.ivPlace.setImageResource(R.drawable.city_placeholder);

        Picasso.with(mContext)
                .load(tripPlan.getCityImageUrl())
                .placeholder(R.drawable.city_placeholder)
                .resize(0, 600)
                .into(holder.ivPlace);

        holder.ivFavIcon.setSelected(tripPlan.isFavorited());
        holder.ivPlace.setColorFilter(Color.argb(150, 50, 50, 50));
    }

    @Override
    public int getItemCount() {
        return mTripPlans.size();
    }

    public TripPlan get(int position) {
        return mTripPlans.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTotalTravelTime;
        TextView tvGroupType;
        TextView tvTravelSeason;
        TextView tvTravelMonth;
        ImageView ivPlace;
        TextView tvPlanName;
        ImageView ivFavIcon;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTotalTravelTime = (TextView) itemView.findViewById(R.id.tvTotalTravelTime);
            tvGroupType = (TextView) itemView.findViewById(R.id.tvGroupType);
            tvTravelSeason = (TextView) itemView.findViewById(R.id.tvTravelSeason);
            tvTravelMonth = (TextView) itemView.findViewById(R.id.tvTravelMonth);
            ivPlace = (ImageView) itemView.findViewById(R.id.ivPlace);
            tvPlanName = (TextView) itemView.findViewById(R.id.tvPlanName);
            ivFavIcon = (ImageView) itemView.findViewById(R.id.ivFavorite);
            ivFavIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();
                    TripPlan tripPlan = mTripPlans.get(position);

                    v.setSelected(!v.isSelected());
                    tripPlan.setFavorite(v.isSelected());

                    if (v.isSelected()) {
                        ObjectAnimator anim1 = ObjectAnimator.ofFloat(v, "scaleX", 0.8f);
                        ObjectAnimator anim2 = ObjectAnimator.ofFloat(v, "scaleY", 0.8f);
                        AnimatorSet set1 = new AnimatorSet();
                        set1.playTogether(anim1, anim2);
                        ObjectAnimator anim3 = ObjectAnimator.ofFloat(v, "scaleX", 1.0f);
                        ObjectAnimator anim4 = ObjectAnimator.ofFloat(v, "scaleY", 1.0f);
                        AnimatorSet set2 = new AnimatorSet();
                        set2.playTogether(anim3, anim4);
                        AnimatorSet set4 = new AnimatorSet();
                        set4.playSequentially(set1, set2);
                        set4.start();
                    } else {
                        ObjectAnimator anim1 = ObjectAnimator.ofFloat(v, "scaleX", 0.8f);
                        ObjectAnimator anim2 = ObjectAnimator.ofFloat(v, "scaleY", 0.8f);
                        AnimatorSet set1 = new AnimatorSet();
                        set1.playTogether(anim1, anim2);
                        ObjectAnimator anim3 = ObjectAnimator.ofFloat(v, "scaleX", 1.0f);
                        ObjectAnimator anim4 = ObjectAnimator.ofFloat(v, "scaleY", 1.0f);
                        AnimatorSet set2 = new AnimatorSet();
                        set2.playTogether(anim3, anim4);
                        AnimatorSet set4 = new AnimatorSet();
                        set4.playSequentially(set1, set2);
                        set4.start();
                    }
                }
            });
        }
    }
}
