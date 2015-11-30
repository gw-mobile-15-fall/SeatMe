package com.seatme.gwu.seatme.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.seatme.gwu.seatme.Constants;
import com.seatme.gwu.seatme.R;
import com.seatme.gwu.seatme.model.Place;
import com.seatme.gwu.seatme.model.Room;
import com.seatme.gwu.seatme.util.RoomListAdapter;

/**
 * Created by Yan on 11/19/2015.
 */
public class CheckRoomActivity extends AppCompatActivity {
    private ListView mListView;

    private String mPlace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_room_info);
        mListView = (ListView) findViewById(R.id.room_list);
        //TODO: add a data structure for rooms in a building, the following is just example code
        Room r = new Room();
        RoomListAdapter adapter = new RoomListAdapter(this, r);
        mListView.setAdapter(adapter);

        Firebase.setAndroidContext(this);

        Bundle Title = getIntent().getExtras();
        if (Title != null) {
            mPlace = Title.getString(Constants.ROOM);
        }

        Firebase myFirebaseRef = new Firebase("https://seatmegwu.firebaseio.com/");

        System.out.println(mPlace);

        myFirebaseRef.child(mPlace).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                System.out.println("There are " + snapshot.getChildrenCount() + "  posts");
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Place room = postSnapshot.getValue(Place.class);
                    System.out.println(room.getNumberOfSeat());
                    System.out.println(room.getFullness());
                }

            }
            @Override public void onCancelled(FirebaseError error) { }
        });

    }
}
