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
import com.firebase.client.ValueEventListener;
import com.seatme.gwu.seatme.Constants;
import com.seatme.gwu.seatme.PersistanceManager;
import com.seatme.gwu.seatme.R;
import com.seatme.gwu.seatme.model.Room;
import com.seatme.gwu.seatme.util.RoomListAdapter;

import java.util.ArrayList;

/**
 * Created by Yan on 11/19/2015.
 */
public class CheckRoomActivity extends AppCompatActivity {
    private ListView mListView;

    private String mPlace;
    private ArrayList<Room> mRooms;
    private PersistanceManager mPersistanceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_room_info);
        mListView = (ListView) findViewById(R.id.room_list);
        mRooms = new ArrayList<Room>();

        Firebase.setAndroidContext(this);
        mPersistanceManager = new PersistanceManager(this);

        Bundle Title = getIntent().getExtras();
        if (Title != null) {
            mPlace = Title.getString(Constants.PLACE);
        }

        Firebase myFirebaseRef = new Firebase("https://seatmegwu.firebaseio.com/");

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> list, View v, int pos, long id) {

                Room r = (Room) list.getItemAtPosition(pos);
                mPersistanceManager.saveRoom(r);
                Intent intent = new Intent(getBaseContext(), RoomDetailActivity.class);
                intent.putExtra(Constants.PLACE, mPlace);
                startActivity(intent);
            }
        });

        myFirebaseRef.child(mPlace).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                System.out.println("There are " + snapshot.getChildrenCount() + "  posts");
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    long numOfChild = postSnapshot.getChildrenCount();
                    long i = 0;
                    if (numOfChild <= 0)
                        continue;

                    for (DataSnapshot postSnapshotChild : postSnapshot.getChildren()) {
                        i++;
                        if (i < numOfChild)
                            continue;
                        Room room = postSnapshotChild.getValue(Room.class);
                        System.out.println(room.getName());
                        mRooms.add(room);
                        break;
                    }
                }
                changeAdapter();
            }

            @Override
            public void onCancelled(FirebaseError error) {
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

    void changeAdapter(){
        RoomListAdapter adapter = new RoomListAdapter(this, mRooms);
        mListView.setAdapter(adapter);
        System.out.println("change adapter!");
    }


}
