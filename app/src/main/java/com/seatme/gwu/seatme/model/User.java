package com.seatme.gwu.seatme.model;

import com.parse.ParseClassName;
import com.parse.ParseUser;

/**
 * Created by Huanzhou on 2015/10/29.
 */

@ParseClassName("User")
public class User extends ParseUser {

    public User() {
        super();
    }

    private int score;

    private String errMessage;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }
}
