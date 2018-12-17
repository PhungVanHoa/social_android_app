package com.liberasoft.zero.molitvenik2.utils;

import android.support.v4.app.FragmentManager;

import com.liberasoft.zero.molitvenik2.R;

public class Globals {

  // Data
  private static String username;
  private static String currentFragmentTag;
  // Behavioral
  private static boolean readDialogOpen;
  private static boolean searchingPrayer;

  public static String getCurrentFragmentTag() {
    return currentFragmentTag;
  }

  public static void setCurrentFragmentTag(FragmentManager fm) {
    Globals.currentFragmentTag = fm.findFragmentById(R.id.activity_main_fragment_container).getTag();
  }

  public static boolean isReadDialogOpen() {
    return readDialogOpen;
  }

  public static void setReadDialogOpen(boolean readDialogOpen) {
    Globals.readDialogOpen = readDialogOpen;
  }

  public static String getUsername() {
    return username;
  }

  public static void setUsername(String username) {
    Globals.username = username;
  }

  public static boolean isSearchingPrayer() {
    return searchingPrayer;
  }

  public static void setSearchingPrayer(boolean searchingPrayer) {
    Globals.searchingPrayer = searchingPrayer;
  }
}
