package com.travelguide.application;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.travelguide.models.DayParseModel;
import com.travelguide.models.PlaceParseModel;
import com.travelguide.models.PlanParseModel;

/**
 * Created by htammare on 10/16/2015.
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant client data..
 */
public class TravelGuideApplication extends Application {
    public static final String APPLICATION_ID = "V5dqynLl7Jx0SKUWlSZlnvJD6pM1kAaPSwMSMyzD";
    public static final String CLIENT_KEY = "52CEN5aiHzVXr5ioEvRr2lLpV6JIuO0sr7FkKUV8";

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(PlanParseModel.class);
        ParseObject.registerSubclass(DayParseModel.class);
        ParseObject.registerSubclass(PlaceParseModel.class);
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        // Initialization code here
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // If you would like all objects to be private by default, remove this line.
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
