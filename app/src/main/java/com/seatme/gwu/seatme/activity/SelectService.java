package com.seatme.gwu.seatme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.ParseUser;
import com.seatme.gwu.seatme.Constants;
import com.seatme.gwu.seatme.R;
import com.seatme.gwu.seatme.util.Util;

/**
 * Created by Yan on 11/1/2015.
 *
 * Provides two buttons to let users choose which service to use, either to find a seat or share room information. Sharing room information requires user has a login state, if user
 * haven't login or the session has expired, it will redirect to login page.
 */
public class SelectService extends AppCompatActivity {
    private Button mSearchSeat;
    private Button mPlaceSeat;
    private ImageButton mHome;
    private ImageButton mSearchShotCut;
    private ImageButton mPlaceShotCut;
    private ImageButton mReward;
    private ImageButton mProfile;
    private ParseUser user;
    private Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_service);

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
                Intent intent = new Intent(getBaseContext(), SearchMapsActivity.class);
                intent.putExtra(Constants.ACTION, "Search");
                startActivity(intent);
            }
        });

        //check the session state, if user do not login or session has expired, redirect to login page.
        mPlaceSeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        mPlaceShotCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        user = ParseUser.getCurrentUser();
        MenuItem logItem = menu.findItem(R.id.logOption);
        // if guest user, ask them to signin or signup
        if (user == null) {
            logItem.setTitle(R.string.login);
        } else {
            logItem.setTitle(R.string.logout);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logOption) {
            user = ParseUser.getCurrentUser();
            MenuItem logItem = menu.findItem(R.id.logOption);
            if (user == null) {
            } else {
                user.logOut();
                Toast.makeText(this, "logout succeed", Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.

                onOptionsItemSelected(item);
    }
}
