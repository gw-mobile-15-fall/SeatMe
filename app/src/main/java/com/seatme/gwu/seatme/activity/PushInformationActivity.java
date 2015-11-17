package com.seatme.gwu.seatme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.seatme.gwu.seatme.R;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PushInformationActivity extends AppCompatActivity {

    private final String TAG = "PushInformationActivity";

    private EditText mFullnessView;
    private EditText mSeatnumbnerView;
    private Button mSubmitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);

        setContentView(R.layout.activity_push_information);

        mFullnessView = (EditText) findViewById(R.id.push_information_form_fullness);
        mSeatnumbnerView = (EditText) findViewById(R.id.push_information_form_seatnumber);
        mSubmitButton = (Button) findViewById(R.id.push_information_form_submit_button);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("push data");
                attemptPushData();
                Intent intent = new Intent(getBaseContext(), SelectService.class);
                startActivity(intent);
            }
        });
    }

    private void attemptPushData() {

        String fullness = mFullnessView.getText().toString();
        String seatnumber = mSeatnumbnerView.getText().toString();

        System.out.println("fullness "+ fullness);
        System.out.println("seatnumber "+ seatnumber);

        if (TextUtils.isEmpty(fullness) || TextUtils.isEmpty(seatnumber) ) {
            System.out.println("empty input");
            return;
        }

        Firebase myFirebaseRef = new Firebase("https://seatmegwu.firebaseio.com/").child("library");

        Map<String, String> post = new HashMap<String, String>();
        post.put("fullness", fullness);
        post.put("seatnumber", seatnumber);
        post.put("time", new Date().toString());
        myFirebaseRef.push().setValue(post);

    }

}
