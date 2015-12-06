package com.seatme.gwu.seatme.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.seatme.gwu.seatme.PersistanceManager;
import com.seatme.gwu.seatme.R;
import com.seatme.gwu.seatme.model.Room;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;


public class RoomDetailActivity extends AppCompatActivity {

    private final String TAG = "RoomDetailActivity";

    private static int[] COLORS = new int[] { Color.RED, Color.BLUE};

    private static String[] NAME_LIST = new String[] { "Taken", "Not Taken"};
    private CategorySeries mSeries = new CategorySeries("");
    private DefaultRenderer mRenderer = new DefaultRenderer();
    private GraphicalView mChartView;
    private TextView mRoomNameView;
    private TextView mSeatnumbnerView;
    private TextView mDescriptionView;
    private ImageView mImageView;

    private PersistanceManager mPersistanceManager;
    private Room r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);

        mRoomNameView = (TextView) findViewById(R.id.detail_roomname);
        mSeatnumbnerView = (TextView) findViewById(R.id.detail_seatnumber);
        mDescriptionView = (TextView) findViewById(R.id.detail_description);
        mImageView = (ImageView) findViewById(R.id.detailed_image);

        mPersistanceManager = new PersistanceManager(this);
        r = mPersistanceManager.getCurrentRoom();

        mRoomNameView.setText(r.getName());
        mSeatnumbnerView.setText(r.getNumberOfSeat());
        mDescriptionView.setText(r.getDescription());

        double taken = Double.parseDouble(r.getFullness());
        double notTaken = 100 - taken;
        double[] PIECHARTVALUES = new  double[] {taken,notTaken};

        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
        mRenderer.setChartTitleTextSize(20);
        mRenderer.setLabelsTextSize(45);
        mRenderer.setShowLegend(false);

//        mRenderer.setMargins(new int[]{20, 30, 15, 0 });
        mRenderer.setZoomButtonsVisible(true);
        mRenderer.setStartAngle(90);

        for (int i = 0; i < PIECHARTVALUES.length; i++) {
            mSeries.add(NAME_LIST[i] + " " + PIECHARTVALUES[i]+ "%", PIECHARTVALUES[i]);
            SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
            renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
            mRenderer.addSeriesRenderer(renderer);
        }

        if (mChartView != null) {
            mChartView.repaint();
        }

        LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
        mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);
        mRenderer.setClickEnabled(true);
        mRenderer.setSelectableBuffer(10);

        layout.addView(mChartView, new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, AbsListView.LayoutParams.FILL_PARENT));


        Ion.with(mImageView).load(r.getImage()).setCallback(new FutureCallback<ImageView>() {
            @Override
            public void onCompleted(Exception e, ImageView result) {
                if (e == null) {
                    //yay

                } else {
                    //log the error information , helping to check. Then keep running app without weather icon
                    Log.d(TAG, "image failed to load " + e.toString());

                }
            }
        });
    }

}
