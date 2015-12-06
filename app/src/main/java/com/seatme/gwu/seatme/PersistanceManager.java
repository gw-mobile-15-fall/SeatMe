package com.seatme.gwu.seatme;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.seatme.gwu.seatme.model.Room;

/**
 * Created by Huanzhou on 2015/12/5.
 */
public class PersistanceManager {

    private Context mContext;

    public PersistanceManager(Context context) {
        mContext = context;
    }

    public boolean saveRoom(Room room) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(Constants.NAME, room.getName());
        editor.putString(Constants.FULLNESS, room.getFullness());
        editor.putString(Constants.NUMBEROFSEAT, room.getNumberOfSeat());
        editor.putString(Constants.TIME, room.getTime());
        editor.putString(Constants.DESCRIPTION, room.getDescription());
        editor.putString(Constants.IMAGE, room.getImage());

        editor.apply();

        return true;

    }

    public Room getCurrentRoom() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        Room r = new Room();

        r.setName(sharedPreferences.getString(Constants.NAME, ""));
        r.setFullness(sharedPreferences.getString(Constants.FULLNESS, ""));
        r.setNumberOfSeat(sharedPreferences.getString(Constants.NUMBEROFSEAT, ""));
        r.setTime(sharedPreferences.getString(Constants.TIME, ""));
        r.setDescription(sharedPreferences.getString(Constants.DESCRIPTION, ""));
        r.setImage(sharedPreferences.getString(Constants.IMAGE, ""));

        return r;
    }
}
