package com.seatme.gwu.seatme.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.seatme.gwu.seatme.Constants;
import com.seatme.gwu.seatme.R;
import com.seatme.gwu.seatme.model.RoomInfo;
import com.seatme.gwu.seatme.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PushInformationActivity extends AppCompatActivity {

    private final String TAG = "PushInformationActivity";

    private static final int CAMERA_REQUEST = 1337;


    private TextView mFullnessView;
    private EditText mSeatnumbnerView;
    private Button mSubmitButton;
    private String mAction;
    private String mRoom;
    private SeekBar mFullnessSeekBar;
    private EditText mDescriptionView;
    private int mFullnessValue = 0;
    private ArrayList<String>  arraySpinner;
    private Spinner mSpinner;
    private Button mPictureButton;
    private ImageView mImageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);

        Bundle Title = getIntent().getExtras();
        if (Title != null) {
            mAction = Title.getString(Constants.ACTION);
            mRoom = Title.getString(Constants.ROOM);
            System.out.println(mAction);
            System.out.println(mRoom);
        }

        setContentView(R.layout.activity_push_information);
        mSpinner = (Spinner)findViewById(R.id.push_information_spinner_room);
        mFullnessSeekBar = (SeekBar) findViewById(R.id.push_information_seekbar_fullness);
        mFullnessView = (TextView) findViewById(R.id.push_information_text_fullness);
        mSeatnumbnerView = (EditText) findViewById(R.id.push_information_form_seatnumber);
        mDescriptionView = (EditText) findViewById(R.id.push_information_form_description);
        mSubmitButton = (Button) findViewById(R.id.push_information_form_submit_button);
        mPictureButton = (Button) findViewById(R.id.push_information_picture_button);
        mImageView = (ImageView) findViewById(R.id.room_picture);

        Firebase myFirebaseRef = new Firebase("https://seatmegwu.firebaseio.com/").child("RoomInfo").child(mRoom);

        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                arraySpinner = new ArrayList<String>();
                System.out.println("There are " + snapshot.getChildrenCount() + "  posts");
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    RoomInfo roomInfo = postSnapshot.getValue(RoomInfo.class);
                    arraySpinner.add(roomInfo.getRoom());
                }

                System.out.println(arraySpinner.indexOf(0));
                changeSpinner();
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });



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


        mPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,CAMERA_REQUEST);
                System.out.println("picture");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK){
            Bitmap image = (Bitmap) data.getExtras().get("data");
            File externalFilesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            boolean imageSaved = Util.saveReactionImage(image, externalFilesDir);

            if(imageSaved == false){
                Log.e(TAG, "Problem saving image");
            }
        }

        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();


        File imageFile = openHighScoreImage();

        if (imageFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            mVictorySelfieImageView.setImageBitmap(bitmap);
            mVictorySelfieImageView.setVisibility(View.VISIBLE);
        } else {
            mVictorySelfieImageView.setVisibility(View.GONE);
        }





    }

    private File openHighScoreImage(){
        File photosDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(photosDirectory, Constants.IMAGE_FILE_NAME);

        return imageFile;
    }

    private void changeSpinner(){

        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, arraySpinner);

        mSpinner.setAdapter(adapter);
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

        Firebase myFirebaseRef = new Firebase("https://seatmegwu.firebaseio.com/").child("").child(room);

        Map<String, String> post = new HashMap<String, String>();
        post.put("fullness", fullness);
        post.put("numberOfSeat", seatnumber);
        post.put("time", new Date().toString());
        post.put("description", description);
        myFirebaseRef.push().setValue(post);

    }



}
