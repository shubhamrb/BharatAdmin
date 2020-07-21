package com.bozobaka.bharatadmin.utils;

import android.content.Context;

import com.bozobaka.bharatadmin.R;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class AppUtil {
    private static Date dateInstance;
    private static long millisToAdd = 3_600_000; //one hour

    public static String getLocaleString(Context context) {
        String lang = PrefUtil.getFromPrefs(context, "language", "en");
        switch (lang) {
            case "english":
                lang = "en";
                break;
            case "hindi":
                lang = "hi";
                break;
            case "punjabi":
                lang = "pa";
                break;
        }
        return lang;
    }

    public static Locale getLocale(Context context) {
        String lang = PrefUtil.getFromPrefs(context, "language", "en");
        switch (lang) {
            case "english":
                lang = "en";
                break;
            case "hindi":
                lang = "hi";
                break;
        }
        return new Locale(lang);
    }

    public static String createRandomCode(int codeLength) {
        char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new SecureRandom();
        for (int i = 0; i < codeLength; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        System.out.println(output);
        return output;
    }

    public static int[] convertIntegers(List<Integer> integers) {
        int[] ret = new int[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < ret.length; i++) {
            ret[i] = iterator.next();
        }
        return ret;
    }

    public static int getCurrentDay() {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_WEEK);
        return day - 1;
    }

    public static String getNextDay(List<Integer> days) {
        int i, day = getCurrentDay();
        if (day == 6) {
            i = 0;
        } else {
            i = day + 1;
        }
        while (i != day) {
            if (days.contains(i)) {
                return getDaysOfWeek().get(i);
            }
            i++;
            if (i == 7) {
                i = 0;
            }
        }
        return getDaysOfWeek().get(day);
    }

    public static List<String> getDaysOfWeek() {
        ArrayList<String> strings = new ArrayList<String>();
        strings.add("Sunday");
        strings.add("Monday");
        strings.add("Tuesday");
        strings.add("Wednesday");
        strings.add("Thursday");
        strings.add("Friday");
        strings.add("Saturday");

        return strings;
    }

    public static String getCurrentTimeInHrs() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date d = getDateInstance();
        String str = sdf.format(d);
        return str;
    }

    public static String getHourLaterCurrentTimeInHrs(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        Date d = getDateInstance();
        try {
            d = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        d.setTime(d.getTime() + millisToAdd);
        String str = sdf.format(d);
        return str;
    }

    public static boolean isTimeWith_in_Interval(String valueToCheck, String startTime, String endTime) {
        boolean isBetween = false;
        try {
            Date time1 = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).parse(startTime);

            Date time2 = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).parse(endTime);

            Date d = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).parse(valueToCheck);

            if (time1.before(d) && time2.after(d)) {
                isBetween = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isBetween;
    }

    public static String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        DateFormat df = new SimpleDateFormat("ddMMyyyy", Locale.ENGLISH);
        return df.format(c);
    }

    private static Date getDateInstance() {
        if (dateInstance == null) {
            return new Date();
        }
        return dateInstance;
    }


}
