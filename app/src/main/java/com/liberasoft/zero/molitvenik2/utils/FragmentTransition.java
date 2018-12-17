package com.liberasoft.zero.molitvenik2.utils;

import android.annotation.SuppressLint;
import android.app.slice.Slice;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.Slide;
import android.view.Gravity;

public class FragmentTransition {

  @SuppressLint("CommitTransaction")
  public static FragmentTransaction createTransition(FragmentManager fm, int container, Fragment fragment, String tag) {
    return fm.beginTransaction().replace(container, fragment, tag);
  }

  @SuppressLint("RtlHardcoded")
  public static void animateFragment(Fragment fragment) {
    fragment.setEnterTransition(new Slide(Gravity.RIGHT));
    fragment.setExitTransition(new Slide(Gravity.LEFT));
  }
}
