package com.seatme.gwu.seatme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.seatme.gwu.seatme.Constants;
import com.seatme.gwu.seatme.PersistanceManager;
import com.seatme.gwu.seatme.R;
import com.seatme.gwu.seatme.model.Room;
import com.seatme.gwu.seatme.util.RoomListAdapter;

import java.util.ArrayList;
import java.util.Collections;

public class HistoryActivity extends AppCompatActivity {
    private ListView mListView;


    private String mRoom;
    private String mPlace;
    private ArrayList<Room> mRooms;
    private PersistanceManager mPersistanceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Bundle Title = getIntent().getExtras();
        if (Title != null) {
            mRoom = Title.getString(Constants.ROOM);
            mPlace = Title.getString(Constants.PLACE);
        }

        mListView = (ListView) findViewById(R.id.room_history_list);

        Firebase.setAndroidContext(this);
        mPersistanceManager = new PersistanceManager(this);

        Firebase myFirebaseRef = new Firebase("https://seatmegwu.firebaseio.com/").child(mPlace).child(mRoom);

        Query Ref = myFirebaseRef.limitToLast(10);

        Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                mRooms = new ArrayList<Room>();
                System.out.println("There are " + snapshot.getChildrenCount() + "  posts");
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    //getRooms(postSnapshot.getKey());
                    Room room = postSnapshot.getValue(Room.class);
                    System.out.println(room.getName());
                    mRooms.add(room);
                }
                Collections.reverse(mRooms);
                changeAdapter();
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> list, View v, int pos, long id) {

                Room r = (Room) list.getItemAtPosition(pos);
                mPersistanceManager.saveRoom(r);
                Intent intent = new Intent(getBaseContext(), RoomDetailActivity.class);
                intent.putExtra(Constants.PLACE, mPlace);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Firebase.goOffline();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Firebase.goOnline();
    }

    void changeAdapter() {
        RoomListAdapter adapter = new RoomListAdapter(this, mRooms);
        mListView.setAdapter(adapter);
        System.out.println("change adapter!");
    }
}
