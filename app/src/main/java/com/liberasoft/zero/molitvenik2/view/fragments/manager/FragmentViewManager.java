package com.liberasoft.zero.molitvenik2.view.fragments.manager;

import android.app.Activity;
import android.widget.TextView;

import com.liberasoft.zero.molitvenik2.R;

import java.util.Objects;

public interface FragmentViewManager {

  static void changeColor(Activity activity, String fragmentType) {
    TextView view1 = Objects.requireNonNull(activity).findViewById(R.id.fragment_daily_view);
    TextView view2 = Objects.requireNonNull(activity).findViewById(R.id.fragment_monthly_view);
    TextView view3 = Objects.requireNonNull(activity).findViewById(R.id.fragment_yearly_view);

    TextView[] viewArray = {view1, view2, view3};
    for(TextView v : viewArray) {
      v.setTextColor(activity.getResources().getColor(R.color.white));
    }
    switch (fragmentType) {
      case "daily":
        view1.setTextColor(activity.getResources().getColor(R.color.yellow));
        break;
      case "monthly":
        view2.setTextColor(activity.getResources().getColor(R.color.yellow));
        break;
      case "yearly":
        view3.setTextColor(activity.getResources().getColor(R.color.yellow));
        break;


    }

  }
}
