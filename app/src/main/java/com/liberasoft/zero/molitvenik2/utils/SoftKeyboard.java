package com.liberasoft.zero.molitvenik2.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class SoftKeyboard {

  public static void close(Activity activity, View viewHoldingKeyboard) {
    InputMethodManager im = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
    if (im != null) {
      im.hideSoftInputFromWindow(viewHoldingKeyboard.getWindowToken(), 0);
    }
  }
}
