package com.seatme.gwu.seatme.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.parse.ParseException;
import com.parse.SignUpCallback;
import com.seatme.gwu.seatme.model.User;


/**
 * Created by Huanzhou on 2015/11/2.
 */
public class UserSignupAsyncTask extends AsyncTask<String,Integer,String> {

    private Context mContext;
    private UserSignupCompletionListener mCompletionListener;

    public interface UserSignupCompletionListener {
         void UserSignupSuccess();
         void UserSignupNotSuccess(String reason);
    }

    public UserSignupAsyncTask(Context context, UserSignupCompletionListener completionListener) {
        mContext = context;
        mCompletionListener = completionListener;
    }


    @Override
    protected String doInBackground(String... query) {
        // TODO: attempt authentication against a network service.
        SignUpCallbackSeatme s = new SignUpCallbackSeatme();
        try {
            // Simulate network access.
            User user = new User();

            user.setUsername(query[0]);
            user.setPassword(query[1]);
            user.setEmail(query[0]);
            user.signUpInBackground(s);

            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println(e.toString());
            return e.toString();
        }

        // TODO: register the new account here.
        return s.response;
    }

    @Override
    protected void onPostExecute(final String response) {

        super.onPostExecute(response);

        if (mCompletionListener != null) {
            if (response.equals("")) {
                mCompletionListener.UserSignupSuccess();
            } else {
                mCompletionListener.UserSignupNotSuccess(response);
            }
        }

    }
}

class SignUpCallbackSeatme implements SignUpCallback {

    public String response = "";

    @Override
    public void done(ParseException e) {
        if(e != null){
            response = e.toString();
        }
    }
}
