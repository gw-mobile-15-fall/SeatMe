package com.seatme.gwu.seatme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.seatme.gwu.seatme.Constants;
import com.seatme.gwu.seatme.R;

/**
 * Created by Yan on 11/15/2015.
 *
 * Simple Reward system allowing user to use their credits to buy Starbucks or Movie tickets.
 */
public class RewardActivity extends AppCompatActivity {
    private TextView mCredit;
    private TextView mRedeemText;
    private RadioGroup mRedeemChoice;
    private ParseUser user;
    private Button mClear;
    private Button mSubmit;
    private Button mHome;
    private boolean isRedeemable = false;
    private int credit;
    private boolean isClear = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);
        mCredit = (TextView)findViewById(R.id.my_credit);
        mSubmit = (Button)findViewById(R.id.radio_submit);
        mClear = (Button)findViewById(R.id.clear_choice);
        mRedeemChoice = (RadioGroup) findViewById(R.id.radio_group);
        mHome = (Button)findViewById(R.id.back_home);
        mRedeemText = (TextView)findViewById(R.id.redeem_hint);

        user=ParseUser.getCurrentUser();
        // if guest user, ask them to signin or signup
        if(user==null){
            mRedeemChoice.setVisibility(View.INVISIBLE);
            mClear.setVisibility(View.INVISIBLE);
            mSubmit.setVisibility(View.INVISIBLE);
            mRedeemText.setVisibility(View.INVISIBLE);
            mHome.setText(R.string.back_home);

            mCredit.setText(R.string.reward_requirelogin);
            mHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                }
            });

        }
        // if signed in user, check if they are qualified to redeem any rewards
        else {
            mHome.setVisibility(View.INVISIBLE);

            credit = (int)user.getNumber(Constants.CREDIT);
            mCredit.setText(mCredit.getText().toString() + credit);

            mRedeemChoice.clearCheck();
            mRedeemChoice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton rb = (RadioButton) group.findViewById(checkedId);
                    if (null != rb && checkedId > -1) {
                        if (checkedId == R.id.radio_starbuck) {
                            if (credit - 200 < 0) {
                                isRedeemable = false;
                                if (!isClear) {
                                    Toast.makeText(getBaseContext(), R.string.not_enough_credit, Toast.LENGTH_SHORT).show();
                                }
                                else isClear = false;

                            } else isRedeemable = true;
                        } else {
                            if (credit - 1000 < 0) {
                                isRedeemable = false;
                                if (!isClear) {
                                    Toast.makeText(getBaseContext(), R.string.not_enough_credit, Toast.LENGTH_SHORT).show();
                                }
                                else isClear = false;
                            } else isRedeemable = true;
                        }
                    }
                }
            });
            mClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isClear = true;
                    mRedeemChoice.clearCheck();
                }
            });

            //decrease user's credits when user use them to get specific item.
            mSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isRedeemable) {
                        if (mRedeemChoice.getCheckedRadioButtonId() == R.id.radio_starbuck) {
                            user.increment(Constants.CREDIT, -200);
                            credit -= 200;
                        } else {
                            user.increment(Constants.CREDIT, -1000);
                            credit -= 1000;
                        }
                        mCredit.setText(R.string.you_current_credit + credit);
                        try {
                            user.save();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getBaseContext(), R.string.redeem_succeed, Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(getBaseContext(), R.string.thankyou,Toast.LENGTH_LONG).show();
                    isClear = true;
                    mRedeemChoice.clearCheck();

                }
            });
        }

    }
}
