package com.seatme.gwu.seatme;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.seatme.gwu.seatme.model.User;

/**
 * Created by Huanzhou on 2015/11/2.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();

        ParseObject.registerSubclass(User.class);
        Parse.initialize(this, Constants.PARSE_APP_ID, Constants.PARSE_CLIENT_KEY);
    }

}
