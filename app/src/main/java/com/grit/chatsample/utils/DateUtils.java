package com.grit.chatsample.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The type Date utils.
 */
public class DateUtils {
    /**
     * The constant dateFormat1.
     */
    public static String dateFormat1 = "yyyy-MM-dd kk:mm:ss";
    /**
     * The constant dateFormat2.
     */
    public static String dateFormat2 = "dd-MM-yyyy";

    /**
     * Gets formatted date.
     *
     * @param date       the date
     * @param dateFormat the date format
     * @return the formatted date
     */
    public static String getFormattedDate(long date, String dateFormat) {
        try {
            SimpleDateFormat spf = new SimpleDateFormat(dateFormat);
            String newDate = spf.format(date);
            return newDate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Gets date in millis.
     *
     * @param date the date
     * @return the date in millis
     */
    public static Long getDateInMillis(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
            Date dateMillis = sdf.parse(date);
            return dateMillis.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * Gets date.
     *
     * @param date the date
     * @return the date
     */
    public static Date getDate(String date) {
        Date formattedDate = null;
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            formattedDate = simpleDateFormat.parse(date);
        } catch (ParseException e) {
           e.printStackTrace();
        }

        return formattedDate;
    }

    /**
     * Gets time.
     *
     * @param date the date
     * @return the time
     */
    public static long getTime(String date) {
        Date formattedDate = null;
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            formattedDate = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate.getTime();
    }

}
