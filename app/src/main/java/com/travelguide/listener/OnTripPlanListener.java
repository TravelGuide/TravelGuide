package com.travelguide.listener;

public interface OnTripPlanListener {

    void onTripPlanItemSelected(String tripPlanObjectId);

    void onTripPlanCreated(String tripPlanObjectId, String imageUrl);

    void onTripPlanNew();
}
