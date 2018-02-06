package com.inverseapps.punchcard.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Inverse, LLC on 10/18/16.
 */

public class Utilities {

    public static Bitmap createScaledBitmap(Context context, Bitmap bitmap){
        float scale = context.getResources().getDisplayMetrics().density;
        Bitmap croppedBitmap = cropToSquare(context, bitmap);
        int srcWidth = croppedBitmap.getWidth();
        int srcHeight = croppedBitmap.getHeight();
        int dstWidth = (int)(srcWidth* (1.0/scale));
        int dstHeight = (int)(srcHeight* (1.0/scale));
        return Bitmap.createScaledBitmap(croppedBitmap, dstWidth, dstHeight, true);
    }

    public static Bitmap cropToSquare(Context context, Bitmap bitmap) {
        float scale = 3;
        int width  = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width)? height - ( height - width) : height;
        if (newWidth >= 192*scale || newHeight >= 192*scale) {
            newWidth = (int)(192 * scale);
            newHeight = (int)(192*scale);
        }
        int cropW = (width - height) / 2;
        cropW = (cropW < 0)? 0: cropW;
        int cropH = (height - width) / 2;
        cropH = (cropH < 0)? 0: cropH;
        Bitmap cropImg = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);

        return cropImg;
    }

    public static String getBase64(final String input) {
        return Base64.encodeToString(input.getBytes(), Base64.NO_WRAP);
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    public static String stringDateFromDate(Date date, String dateFormat) {
        DateFormat df = new SimpleDateFormat(dateFormat, Locale.US);
        df.setTimeZone(TimeZone.getDefault());
        return df.format(date);
    }
    public static String stringDateFromDateWithoutTimezone(Date date, String dateFormat) {
        DateFormat df = new SimpleDateFormat(dateFormat);
        return df.format(date);
    }
    public static String middleStringDateFromDate(Date date) {
        return stringDateFromDate(date, "EEEE MMM dd, yyyy");
    }

    public static String monthStringDateFromDate(Date date) {
        return stringDateFromDateWithoutTimezone(date, "MMMM dd, yyyy");
    }

    public static String getMonthFromDate(Date date) {
        return stringDateFromDateWithoutTimezone(date, "MMMM yyyy");
    }

    public static String shortStringDateFromDate(Date date) {
        return stringDateFromDate(date, "MMM dd, yyyy");
    }

    public static String shortStringDateFromDate1(Date date) {
        return stringDateFromDate(date, "MM/dd/yyyy");
    }
    public static String shortStringTimeFromDate(Date date) {
        return stringDateFromDate(date, "h:mm a");
    }


    public static String shortStringDateTimeFromDate(Date date) {
        String dateString = shortStringDateFromDate(date);
        String timeString = shortStringTimeFromDate(date);
        return String.format("%s - %s", dateString, timeString);
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED );
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
