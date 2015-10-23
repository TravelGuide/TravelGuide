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
import com.travelguide.models.Place;

import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {

    private List<Place> mPlaces;

    public PlaceAdapter(List<Place> places) {
        this.mPlaces = places;
    }

    @Override
    public PlaceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View placesView = inflater.inflate(R.layout.item_place, parent, false);
        ViewHolder viewHolder = new ViewHolder(placesView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PlaceAdapter.ViewHolder holder, int position) {
        holder.tv_Time.setText(mPlaces.get(position).getVisitingTime());
        holder.tv_Place.setText(mPlaces.get(position).getPlaceName());
    }

    @Override
    public int getItemCount() {
        return mPlaces.size();
    }

    public static  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_Time;
        public TextView tv_Place;
        RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_Time = (TextView) itemView.findViewById(R.id.tv_Time);
            tv_Place = (TextView) itemView.findViewById(R.id.tv_PlaceName);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int position = getLayoutPosition(); // gets item position
            Log.d("PlaceAdapter", "position: " + position);
        }
    }

}
