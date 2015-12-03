package com.seatme.gwu.seatme.util;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.seatme.gwu.seatme.R;
import com.seatme.gwu.seatme.model.Room;

import java.util.List;

/**
 * Created by Yan on 11/19/2015.
 */
public class RoomListAdapter extends ArrayAdapter<Room> {
    private final Activity context;
    private Room r;


    public RoomListAdapter(Activity context, List<Room> rooms) {

        super(context, R.layout.room_list, rooms);
        this.context = context;
    }

    public View getView(int position, View view, ViewGroup parent){

        Room room = getItem(position);


        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.room_list, null, true);
        TextView roomName = (TextView)rowView.findViewById(R.id.room_name);
        TextView roomFullness = (TextView) rowView.findViewById(R.id.room_fullness);
        TextView roomNumberOfSeat = (TextView)rowView.findViewById(R.id.room_seat_number);
        TextView roomTime = (TextView) rowView.findViewById(R.id.room_time);

        roomName.setText(room.getName());
        roomFullness.setText(room.getFullness());
        roomNumberOfSeat.setText(room.getNumberOfSeat());
        roomTime.setText(room.getTime());

       // room.setText(r.room[position]);
        //descrpit.setText(r.discription[position]);
        return rowView;
    }
}
