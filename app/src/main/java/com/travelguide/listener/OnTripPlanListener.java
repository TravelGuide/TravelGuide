package com.travelguide.listener;

public interface OnTripPlanListener {

    void onTripPlanItemSelected(String tripPlanObjectId);

    void onTripPlanNew();

    void onTripPlanCreated();
}
