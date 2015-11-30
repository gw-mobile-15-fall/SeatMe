package com.seatme.gwu.seatme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.seatme.gwu.seatme.Constants;
import com.seatme.gwu.seatme.R;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PushInformationActivity extends AppCompatActivity {

    private final String TAG = "PushInformationActivity";

    private TextView mFullnessView;
    private EditText mSeatnumbnerView;
    private Button mSubmitButton;
    private String mAction;
    private String mRoom;
    private SeekBar mFullnessSeekBar;
    private EditText mDescriptionView;
    private int mFullnessValue = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);

        Bundle Title = getIntent().getExtras();
        if (Title != null) {
            mAction = Title.getString(Constants.ACTION);
            mRoom = Title.getString(Constants.ROOM);
            System.out.println(mAction);
        }

        setContentView(R.layout.activity_push_information);
        mFullnessSeekBar = (SeekBar) findViewById(R.id.push_information_seekbar_fullness);
        mFullnessView = (TextView) findViewById(R.id.push_information_text_fullness);
        mSeatnumbnerView = (EditText) findViewById(R.id.push_information_form_seatnumber);
        mDescriptionView = (EditText) findViewById(R.id.push_information_form_description);
        mSubmitButton = (Button) findViewById(R.id.push_information_form_submit_button);


        mFullnessSeekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int progress = 0;

                    @Override
                    public void onProgressChanged(SeekBar seekBar,
                                                  int progresValue, boolean fromUser) {
                        progress = progresValue;
                        mFullnessValue = progresValue;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // Do something here,
                        //if you want to do anything at the start of
                        // touching the seekbar
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // Display the value in textview
                        mFullnessView.setText(progress + "/" + seekBar.getMax());
                    }
                });


        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("push data");
                attemptPushData(mRoom);
                Intent intent = new Intent(getBaseContext(), SelectService.class);
                startActivity(intent);
            }
        });
    }

    private void attemptPushData(String room) {

        String fullness = Integer.toString(mFullnessValue);
        String seatnumber = mSeatnumbnerView.getText().toString();
        String description =mDescriptionView.getText().toString();

        System.out.println("fullness "+ fullness);
        System.out.println("seatnumber "+ seatnumber);

        if (TextUtils.isEmpty(fullness) || TextUtils.isEmpty(seatnumber) ) {
            System.out.println("empty input");
            return;
        }

        Firebase myFirebaseRef = new Firebase("https://seatmegwu.firebaseio.com/").child(room);

        Map<String, String> post = new HashMap<String, String>();
        post.put("fullness", fullness);
        post.put("numberOfSeat", seatnumber);
        post.put("time", new Date().toString());
        post.put("description", description);
        myFirebaseRef.push().setValue(post);

    }



}
