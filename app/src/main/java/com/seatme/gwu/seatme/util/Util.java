package com.seatme.gwu.seatme.util;

import android.graphics.Bitmap;

import com.seatme.gwu.seatme.Constants;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Huanzhou on 2015/11/2.
 */
public class Util {

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
}
