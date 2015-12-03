package com.seatme.gwu.seatme.model;

/**
 * Created by Huanzhou on 2015/11/30.
 */
public class Room {

    private String name;
    private String fullness;
    private String numberOfSeat;
    private String time;
    private String description;
    private String image;


    public Room(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFullness() {
        return fullness;
    }

    public void setFullness(String fullness) {
        this.fullness = fullness;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNumberOfSeat() {
        return numberOfSeat;
    }

    public void setNumberOfSeat(String numberOfSeat) {
        this.numberOfSeat = numberOfSeat;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
