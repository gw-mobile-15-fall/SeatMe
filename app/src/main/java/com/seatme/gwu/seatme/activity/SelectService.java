package com.seatme.gwu.seatme.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.seatme.gwu.seatme.R;

/**
 * Created by Yan on 11/1/2015.
 */
public class SelectService extends AppCompatActivity {
    private Button mSearchSeat;
    private Button mPlaceSeat;
    private ImageButton mHome;
    private ImageButton mSearchShotCut;
    private ImageButton mPlaceShotCut;
    private ImageButton mReward;
    private ImageButton mProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSearchSeat = (Button) findViewById(R.id.search);
        mPlaceSeat = (Button) findViewById(R.id.place);
        mHome = (ImageButton) findViewById(R.id.home);
        mSearchShotCut = (ImageButton) findViewById(R.id.search_button);
        mPlaceShotCut = (ImageButton) findViewById(R.id.place_button);
        mReward = (ImageButton) findViewById(R.id.reward);
        mProfile = (ImageButton) findViewById(R.id.profile);
        mSearchSeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: add intend to search seat activity
            }
        });

        mPlaceSeat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO: add intend to place seat activity
            }
        });

        mHome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO: add intend to go back to SelectService activity
            }
        });

        mSearchShotCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: add intend to search seat activity
            }
        });

        mPlaceShotCut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO: add intend to place seat activity
            }
        });
        mReward.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO: add intend to reward activity
            }
        });

        mProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO: add intend to profile setting activity
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
         //   Log.d(TAG, "settings button pressed");
         //   Intent intent = new Intent(this, SettingsActivity.class);
        //    startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
