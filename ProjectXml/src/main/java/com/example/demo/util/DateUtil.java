package com.example.demo.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    /**
     * getDateFormat
     * 
     * @param date1
     * @param format
     * @return String
     */
    public static String getDateFormat(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return sdf.format(new Date()).toString();
    }

}