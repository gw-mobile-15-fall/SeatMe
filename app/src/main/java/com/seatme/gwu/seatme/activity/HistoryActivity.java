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
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.seatme.gwu.seatme.Constants;
import com.seatme.gwu.seatme.PersistanceManager;
import com.seatme.gwu.seatme.R;
import com.seatme.gwu.seatme.model.Room;
import com.seatme.gwu.seatme.util.RoomListAdapter;
import com.seatme.gwu.seatme.util.Util;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Huanzhou on 11/15/2015.
 *
 * HistoryActivity allows users to get a list of recent records for one room, containing room name, fullness, number of seat as well as update time,
 * ordered by updated time. User can also select one record to get into Room Detailed information page to see more data.
 *
 */
public class HistoryActivity extends AppCompatActivity {

    private final String TAG = "HistoryActivity";

    private ListView mListView;

    private String mRoom;
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
        setContentView(R.layout.activity_history);

        //get room name and building name using extras, which will be used as parameters in firebase.
        Bundle Title = getIntent().getExtras();
        if (Title != null) {
            mRoom = Title.getString(Constants.ROOM);
            mPlace = Title.getString(Constants.PLACE);
        }

        mListView = (ListView) findViewById(R.id.room_history_list);

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

        Firebase myFirebaseRef = new Firebase(Constants.FIREBASE_SEATME).child(mPlace).child(mRoom);
        Query Ref = myFirebaseRef.limitToLast(10);

        Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                mRooms = new ArrayList<Room>();
                System.out.println("There are " + snapshot.getChildrenCount() + "  posts");
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Room room = postSnapshot.getValue(Room.class);
                    mRooms.add(room);
                }
                //in order to show the data order by updated time, just reverse the items from firebase, small trick..
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
        System.out.println("change adapter!");
    }
}
