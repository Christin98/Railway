package com.kha.train.util;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Utils {
    public static String getTimeDiff(String str, String str2) {
        String str3 = "00";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        try {
            Date parse = simpleDateFormat.parse(str);
            Date parse2 = simpleDateFormat.parse(str2);
            long time = parse2.getTime() - parse.getTime();
            if (time < 0) {
                time = (simpleDateFormat.parse("24:00").getTime() - parse.getTime()) + (parse2.getTime() - simpleDateFormat.parse("00:00").getTime());
            }
            time -= (long) (((int) (time / 86400000)) * 86400000);
            int i = (int) (time / 3600000);
            int i2 = ((int) (time - ((long) (3600000 * i)))) / 60000;
            PrintStream printStream = System.out;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("log_tagHours: ");
            long j = (long) i;
            stringBuilder.append(new DecimalFormat(str3).format(j));
            stringBuilder.append(", Mins: ");
            long j2 = (long) i2;
            stringBuilder.append(new DecimalFormat(str3).format(j2));
            printStream.printf(stringBuilder.toString(), new Object[0]);
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(new DecimalFormat(str3).format(j));
            stringBuilder2.append("h ");
            stringBuilder2.append(new DecimalFormat(str3).format(j2));
            stringBuilder2.append("m");
            return stringBuilder2.toString();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String changeDateFormate(String str) {
        try {
            str = new SimpleDateFormat("dd MMM, EEEE").format(new SimpleDateFormat("dd-MM-yyyy").parse(str));
            return str;
        } catch (ParseException e) {
            e.printStackTrace();
            return str;
        }
    }

    public static String changeDateFormate(String str, String str2, String str3) {

        Date date = null;
        String strs = "00.00";
        if (str != null) {

            SimpleDateFormat inputFormat = new SimpleDateFormat(str2);
            SimpleDateFormat outputFormat = new SimpleDateFormat(str3);


            Log.e("changeDateFormate", "" + str);
            try {
                date = inputFormat.parse(str);
                strs = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }

        return strs;
//        try {
//            str = new SimpleDateFormat(str3).format(new SimpleDateFormat(str2).parse(str));
//            return str;
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return str;
//        }
    }

    public static String changeDateFormate(Date date, String str) {
        String date2;
        try {
            date2 = new SimpleDateFormat(str).format(date);
            return date2;
        } catch (Exception e) {
            e.printStackTrace();
            return String.valueOf(date);
        }
    }

    public static Date convertStrToDate(String str) {
        try {
            return new SimpleDateFormat("yyyyMMdd").parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void hideKeyboardFrom(Context context, View view) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String convertMillsecTime(long j, boolean z) {
        Date date = new Date(j);
        String str = "UTC";
        SimpleDateFormat simpleDateFormat;
        if (z) {
            simpleDateFormat = new SimpleDateFormat("HH:mm");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone(str));
            return simpleDateFormat.format(date);
        }
        simpleDateFormat = new SimpleDateFormat("hh:mm a");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(str));
        return simpleDateFormat.format(date);
    }

    public static void ShareDetails(Context context, String str) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/*");
        intent.putExtra("android.intent.extra.SUBJECT", "Share Indian Railway Details");
        intent.putExtra("android.intent.extra.TEXT", str);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, "Share Photo via..."));
    }

    public static String changeTimeFormat(String str, boolean z) {
        if (z) {
            return str;
        }
        try {
            str = new SimpleDateFormat("hh:mm a").format(new SimpleDateFormat("HH:mm").parse(str));
            return str;
        } catch (ParseException e) {
            e.printStackTrace();
            return str;
        }
    }
}
