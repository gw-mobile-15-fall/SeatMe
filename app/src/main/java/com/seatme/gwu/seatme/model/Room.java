package com.seatme.gwu.seatme.model;

/**
 * Created by Yan on 11/19/2015.
 */
public class Room {
    public String room[] = {"R101", "R201"};
    public String discription[] = {"empty", "full"};


    public Room(){}

    private String fullness;
    private String numberOfSeat;
    private String time;


    public String getFullness() {
        return fullness;
    }

    public String getDate() {
        return time;
    }

    public void setDate(String time) {
        this.time = time;
    }

    public void setFullness(String fullness) {
        this.fullness = fullness;
    }

    public String getNumberOfSeat() {
        return numberOfSeat;
    }

    public void setNumberOfSeat(String numberOfSeat) {
        this.numberOfSeat = numberOfSeat;
    }
}
