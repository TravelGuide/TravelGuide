package com.travelguide.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by htammare on 10/16/2015.
 */
@ParseClassName("PlanDetails")
public class PlanParseModel extends ParseObject {

    //Constructor
    public PlanParseModel(){
        super();
    }

    //Getting Data
    public String getCreatedUserId() {return getString("createdUserId");}
    public String getPlanName() {return getString("planName");}
    public String getCityImageUrl() {return getString("cityImageUrl");}
    public String getCityName() {return getString("cityName");}
    public Integer getTripTime() {return getInt("totalTripTime");}
    public String getTravelMonth() {return getString("travelMonth");}
    public String getTravelSeason() {return getString("travelSeason");}
    public Date getTripBeginDate() {return getDate("tripBeginDate");}
    public Date getTripEndDate() {return getDate("tripEndDate");}
    public String getTripNotes() {return getString("tripNotes");}
    public Integer getTripCost() {return getInt("tripCost");}
    public String getGroupType() {return getString("groupType");}

    //Saving Data
    public void putCreatedUserId(String createdUserId) { put("createdUserId", createdUserId);}
    public void putPlanName(String planName) {put("planName",planName);}
    public void putCityName(String cityName) {put("cityName",cityName);}
    public void puCityImageURL(String cityImageUrl) {put("cityImageUrl",cityImageUrl);}
    public void putTripTime(Integer totalTripTime) {put("totalTripTime",totalTripTime);}
    public void putTravelMonth(String travelMonth) {put("travelMonth",travelMonth);}
    public void putTravelSeason(String travelSeason) {put("travelSeason",travelSeason);}
    public void putTripBeginDate(Date tripBeginDate) {put("tripBeginDate",tripBeginDate);}
    public void putTripEndDate(Date tripEndDate) {put("tripEndDate",tripEndDate);}
    public void putTripNotes(String tripNotes) {put("tripNotes",tripNotes);}
    public void putTripCost(Integer tripCost) {put("tripCost",tripCost);}
    public void putGroupType(String groupType) {put("groupType",groupType);}
}
