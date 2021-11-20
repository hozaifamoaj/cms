package com.card.cms.utils.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static boolean isOk(Object value) {
        return !(value == null);
    }

    public static Date currentDate() {
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date today = new Date();
            return formatter.parse(formatter.format(today));
        } catch (Exception e) {
            return new Date();
        }

    }

    public static Date makeDate(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }


}
