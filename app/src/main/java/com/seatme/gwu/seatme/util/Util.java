package com.seatme.gwu.seatme.util;

import android.graphics.Bitmap;

import com.google.gson.JsonObject;
import com.seatme.gwu.seatme.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

/**
 * Created by Huanzhou on 2015/11/2.
 */
public class Util {

    public static String S3ImagePrefix = "https://s3.amazonaws.com/hereseas-public-images/";

    public static boolean isEmailValid(String email) {
        return email.contains("@");
    }

    public static boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    public static boolean saveReactionImage(Bitmap bitmap, File directory){
        File image = new File(directory, Constants.IMAGE_FILE_NAME);

        FileOutputStream outStream;
        try {

            outStream = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outStream);

            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static URL parseJsonFromS3(JsonObject jsonObject) throws Exception{

        boolean result = jsonObject.get("result").getAsBoolean();
        String imageUrl;

        if(result){
            imageUrl = S3ImagePrefix + jsonObject.get("data").getAsString();
            System.out.println(imageUrl);
            return  new URL(imageUrl);
        }else
            return null;

    }
}
