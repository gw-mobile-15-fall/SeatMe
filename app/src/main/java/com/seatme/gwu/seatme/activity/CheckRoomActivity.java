package com.seatme.gwu.seatme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.seatme.gwu.seatme.Constants;
import com.seatme.gwu.seatme.PersistanceManager;
import com.seatme.gwu.seatme.R;
import com.seatme.gwu.seatme.model.Room;
import com.seatme.gwu.seatme.util.RoomListAdapter;
import com.seatme.gwu.seatme.util.Util;

import java.util.ArrayList;

/**
 * Created by Huanzhou on 11/19/2015.
 *
 * CheckRoomActivity is to check the newest situation for all the rooms in a specific building, like Gelman library,
 * showing the room name, fullness, number of seat as well as update time using a list-view.
 */
public class CheckRoomActivity extends AppCompatActivity {
    private ListView mListView;

    private String mPlace;
    private ArrayList<Room> mRooms;
    private PersistanceManager mPersistanceManager;
    private ImageButton mHome;
    private ImageButton mSearchShotCut;
    private ImageButton mPlaceShotCut;
    private ImageButton mReward;
    private ImageButton mProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_room_info);
        mListView = (ListView) findViewById(R.id.room_list);
        mHome = (ImageButton) findViewById(R.id.home);
        mSearchShotCut = (ImageButton) findViewById(R.id.search_button);
        mPlaceShotCut = (ImageButton) findViewById(R.id.place_button);
        mReward = (ImageButton) findViewById(R.id.reward);
        mProfile = (ImageButton) findViewById(R.id.profile);

        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SelectService.class);
                startActivity(intent);
            }
        });

        mSearchShotCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SearchMapsActivity.class);
                intent.putExtra(Constants.ACTION, "Search");
                startActivity(intent);
            }
        });

        //Share seat button, direct user to push information activity, but will check session state.
        mPlaceShotCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check the session state, if user do not login or session has expired, redirect to login page.
                if (Util.getCurrentUser() == null) {
                    Toast.makeText(getBaseContext(), R.string.NOTIFICATION_REQUIRELOGIN, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getBaseContext(), SearchMapsActivity.class);
                    intent.putExtra(Constants.ACTION, "Room");
                    startActivity(intent);
                }

            }
        });

        mReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), RewardActivity.class);
                startActivity(intent);
            }
        });

        mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });


        Firebase.setAndroidContext(this);
        mPersistanceManager = new PersistanceManager(this);

        //get building name using extras, which will be used as parameters in firebase.
        Bundle Title = getIntent().getExtras();
        if (Title != null) {
            mPlace = Title.getString(Constants.PLACE);
        }

        //retrieve data from firebase dynamically. In order to show the newest record for each room in one specific building, we use two nested loop，
        //The outer loop is to retrieve each room information, the inner loop is to get the newest record in this room. When finish loading the data, then it
        //will change the adapter applying for list-view.
        Firebase myFirebaseRef = new Firebase(Constants.FIREBASE_SEATME);

        myFirebaseRef.child(mPlace).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mRooms = new ArrayList<Room>();
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

        //When user select one record, then go to the RoomDetailActivity, which will show the detail information of this record using PersistanceManager to pass value.
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

    /**
     * when onPause, disconnect the firebase connection to save energy and traffic load.
     */
    @Override
    protected void onPause() {
        super.onPause();
        Firebase.goOffline();
    }

    /**
     * when onResume, reconnect the firebase connection.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Firebase.goOnline();
    }

    void changeAdapter() {
        RoomListAdapter adapter = new RoomListAdapter(this, mRooms);
        mListView.setAdapter(adapter);
    }


}
