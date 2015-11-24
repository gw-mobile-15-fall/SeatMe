package com.seatme.gwu.seatme.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.seatme.gwu.seatme.R;
import com.seatme.gwu.seatme.model.Room;
import com.seatme.gwu.seatme.util.RoomListAdapter;

/**
 * Created by Yan on 11/19/2015.
 */
public class CheckRoomActivity extends AppCompatActivity {
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_room_info);
        mListView = (ListView) findViewById(R.id.room_list);
        //TODO: add a data structure for rooms in a building, the following is just example code
        Room r = new Room();
        RoomListAdapter adapter = new RoomListAdapter(this, r);
        mListView.setAdapter(adapter);
    }
}
