package com.liberasoft.zero.molitvenik2.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.liberasoft.zero.molitvenik2.R;
import com.liberasoft.zero.molitvenik2.utils.constants.ErrorHandleReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeStamp {

  private static final int HOUR_SECONDS = 60 * 60;
  private static final int MIN_SECONDS = 60;
  private static final int DAY_SECONDS = 24 * 60 * 60;
  private static final int MONTH_SECONDS = 30 * 24 * 60 * 60;
  private static final int YEAR_SECONDS = 12 * 30 * 24 * 60 * 60;

  // Represents current CET time
  @SuppressLint("SimpleDateFormat")
  public static String getDate() {
    SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss dd.MM.yyyy");
    df.setTimeZone(TimeZone.getTimeZone("GMT+3"));
    String strDate = df.format(new Date());
    return strDate;
  }

  public static int convertInSeconds(String time) {
    int hoursInSec = Integer.parseInt(time.substring(0, 2)) * HOUR_SECONDS;
    int minInSec = Integer.parseInt(time.substring(3, 5)) * MIN_SECONDS;
    int sec = Integer.parseInt(time.substring(6, 8));
    int dayInSec = Integer.parseInt(time.substring(9, 11)) * DAY_SECONDS;
    int monthsInSec = Integer.parseInt(time.substring(12, 14)) * MONTH_SECONDS;
    int yearInSec = Integer.parseInt(time.substring(15, 17)) * YEAR_SECONDS;

    return hoursInSec + minInSec + sec + dayInSec + monthsInSec + yearInSec;
  }

  // Converts given time and current time into seconds
  private static int secondsPassed(String time) {
    return convertInSeconds(time) - convertInSeconds(getDate());
  }

  // Represents time passed in hours, minutes, seconds
  public static String timePassed(Context context, String time) {
    int passedSeconds = convertInSeconds(time) - convertInSeconds(getDate());
    int y, mon, d, h, m, s;

    y = Math.abs(passedSeconds / YEAR_SECONDS);
    mon = Math.abs((passedSeconds % YEAR_SECONDS) / MONTH_SECONDS);
    d = Math.abs(((passedSeconds % YEAR_SECONDS) % MONTH_SECONDS) / DAY_SECONDS);
    h = Math.abs((((passedSeconds % YEAR_SECONDS) % MONTH_SECONDS) % DAY_SECONDS) / HOUR_SECONDS);
    m = Math.abs(((((passedSeconds % YEAR_SECONDS) % MONTH_SECONDS) % DAY_SECONDS) % HOUR_SECONDS) / MIN_SECONDS);
    s = Math.abs(((((passedSeconds % YEAR_SECONDS) % MONTH_SECONDS) % DAY_SECONDS) % HOUR_SECONDS) % MIN_SECONDS);

    if (y != 0) {
      return y + " " + context.getResources().getString(R.string.year_ref);
    } else if (mon != 0) {
      return mon + " " + context.getResources().getString(R.string.month_ref);
    } else if (d != 0) {
      return d + " " + context.getResources().getString(R.string.day_ref);
    } else if (h != 0) {
      return h + " " + context.getResources().getString(R.string.hour_ref);
    } else if (m != 0) {
      return m + " " + context.getResources().getString(R.string.minute_ref);
    } else {
      return s + " " + context.getResources().getString(R.string.sec_ref);
    }
  }
}