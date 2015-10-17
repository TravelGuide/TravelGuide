package com.travelguide.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by htammare on 10/16/2015.
 */

@ParseClassName("DayDetails")
public class DayParseModel extends ParseObject{

    //Constructor
    public DayParseModel(){
        super();
    }

    //Getting Data
    public Integer getTravelDay() {return getInt("travelDay");}
    public Date getTravelDate() {return getDate("travelDate");}
    public String getCreatedUserId() {return getString("createdUserId");}
    public String getPlanName() {return getString("planName");}

    //Saving Data
    public void putTravelDay(Integer travelDay) { put("travelDay", travelDay);}
    public void putTravelDate(Date travelDate) {put("travelDate",travelDate);}
    public void putCreatedUserId(String createdUserId) { put("createdUserId", createdUserId);}
    public void putPlanName(String planName) {put("planName",planName);}
}
