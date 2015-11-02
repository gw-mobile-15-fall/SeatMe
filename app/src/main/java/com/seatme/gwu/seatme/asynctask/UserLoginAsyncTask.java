package com.seatme.gwu.seatme.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.seatme.gwu.seatme.model.User;

/**
 * Created by Huanzhou on 2015/11/2.
 */
public class UserLoginAsyncTask extends AsyncTask<String, Integer, User> {
    private Context mContext;
    private UserLoginCompletionListener mCompletionListener;
    private User mUser;

    public interface UserLoginCompletionListener {
        void UserLoginSuccess(User user);

        void UserLoginNotSuccess(String reason);
    }

    public UserLoginAsyncTask(Context context, UserLoginCompletionListener completionListener) {
        User user = new User();
        mUser = user;
        mCompletionListener = completionListener;
    }


    @Override
    protected User doInBackground(String... query) {
        // TODO: attempt authentication against a network service.

        try {
            // Simulate network access.

            System.out.println(query[0]+" "+ query[1]);

            ParseUser.logInInBackground(query[0], query[1], new LogInCallback() {
                public void done(ParseUser user, ParseException e) {
                    if (user != null) {
                        attempt(user, null);
                    } else {
                        System.out.println(e.toString());
                        attempt(user, e.toString());
                    }
                }
            });

            Thread.sleep(2000);
        } catch (InterruptedException e) {
            //mUser.setErrMessage(e.toString());
            System.out.println(e.toString());
        }

        return mUser;
    }

    @Override
    protected void onPostExecute(final User user) {

        super.onPostExecute(user);

        if (mCompletionListener != null) {
            if (mUser.getErrMessage() == null) {
                mCompletionListener.UserLoginSuccess(user);
            } else {
                mCompletionListener.UserLoginNotSuccess(mUser.getErrMessage());
            }
        }

    }

    private void attempt(ParseUser user, String e) {

        if (e == null) {
            mUser.setUsername(user.getUsername());
            mUser.setEmail(user.getEmail());
            mUser.setObjectId(user.getObjectId());
        } else if(user==null){
            mUser.setErrMessage(e.toString());
        }

    }


}

