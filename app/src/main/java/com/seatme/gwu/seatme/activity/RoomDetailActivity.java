package com.seatme.gwu.seatme.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.seatme.gwu.seatme.Constants;
import com.seatme.gwu.seatme.PersistanceManager;
import com.seatme.gwu.seatme.R;
import com.seatme.gwu.seatme.model.Room;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

/**
 * Created by Huanzhou on 11/15/2015.
 *
 * In RoomDetailActivity, user is able to get detailed room information for one record shared by other users, including room name, number of seat, a pie chart which
 * shows the fullness percentage using achartengine library, description and a photo. Users can also enter a list of recent records shared by other user by clicking show history.
 */
public class RoomDetailActivity extends AppCompatActivity {

    private final String TAG = "RoomDetailActivity";

    private static int[] COLORS = new int[]{Color.RED, Color.BLUE};

    private static String[] NAME_LIST = new String[]{Constants.OCCUPIED, Constants.VACANT};
    private CategorySeries mSeries = new CategorySeries("");
    private DefaultRenderer mRenderer = new DefaultRenderer();
    private GraphicalView mChartView;
    private TextView mRoomNameView;
    private TextView mSeatnumbnerView;
    private TextView mDescriptionView;
    private ImageView mImageView;
    private Button mHistoryButton;

    private PersistanceManager mPersistanceManager;
    private Room r;
    private String mPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);

        Bundle Title = getIntent().getExtras();
        if (Title != null) {
            mPlace = Title.getString(Constants.PLACE);
        }

        mRoomNameView = (TextView) findViewById(R.id.detail_roomname);
        mSeatnumbnerView = (TextView) findViewById(R.id.detail_seatnumber);
        mDescriptionView = (TextView) findViewById(R.id.detail_description);
        mImageView = (ImageView) findViewById(R.id.detailed_image);
        mHistoryButton = (Button) findViewById(R.id.history_button);

        mPersistanceManager = new PersistanceManager(this);
        r = mPersistanceManager.getCurrentRoom();

        mRoomNameView.setText(r.getName());
        mSeatnumbnerView.setText(r.getNumberOfSeat());
        mDescriptionView.setText(r.getDescription());

        //set the achartengine pie chart parameters, including color, lable size as well as items shown options
        double taken = Double.parseDouble(r.getFullness());
        double notTaken = 100 - taken;
        double[] PIECHARTVALUES = new double[]{taken, notTaken};

        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
        mRenderer.setChartTitleTextSize(20);
        mRenderer.setLabelsTextSize(50);
        mRenderer.setShowLegend(false);
        mRenderer.setShowTickMarks(false);
        mRenderer.setZoomButtonsVisible(true);
        mRenderer.setStartAngle(90);

        //show the percentage
        for (int i = 0; i < PIECHARTVALUES.length; i++) {
            mSeries.add(NAME_LIST[i] + " " + PIECHARTVALUES[i] + "%", PIECHARTVALUES[i]);
            SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
            renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
            mRenderer.addSeriesRenderer(renderer);
        }

        if (mChartView != null) {
            mChartView.repaint();
        }

        //load the pie chart layout with the parameters in mRenderer and mSeries we set before.
        LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
        mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);
        mRenderer.setClickEnabled(true);
        mRenderer.setSelectableBuffer(10);

        layout.addView(mChartView, new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, AbsListView.LayoutParams.FILL_PARENT));

        if (!r.getImage().equals("")) {
            Ion.with(mImageView).load(r.getImage()).setCallback(new FutureCallback<ImageView>() {
                @Override
                public void onCompleted(Exception e, ImageView result) {
                    if (e == null) {
                        //yay

                    } else {
                        //log the error information , helping to check. Then keep running app without image
                        Log.d(TAG, "image failed to load " + e.toString());

                    }
                }
            });
        }

        //set the extre before going to history activity, helping to save which building and which room to load.
        mHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), HistoryActivity.class);
                intent.putExtra(Constants.ROOM, r.getName());
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

}
