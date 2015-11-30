package com.seatme.gwu.seatme.util;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.seatme.gwu.seatme.R;
import com.seatme.gwu.seatme.model.Room;

/**
 * Created by Yan on 11/19/2015.
 */
public class RoomListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private Room r;


    public RoomListAdapter(Activity context, Room room) {

        super(context, R.layout.room_list, room.room);
        this.context = context;
        r = room;
    }

    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.room_list, null, true);
        TextView room = (TextView)rowView.findViewById(R.id.room_num);
        TextView descrpit = (TextView) rowView.findViewById(R.id.room_description);
       // room.setText(r.room[position]);
        //descrpit.setText(r.discription[position]);
        return rowView;
    }
}
