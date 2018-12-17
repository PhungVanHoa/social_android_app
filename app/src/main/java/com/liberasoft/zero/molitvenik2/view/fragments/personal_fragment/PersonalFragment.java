package com.liberasoft.zero.molitvenik2.view.fragments.personal_fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.liberasoft.zero.molitvenik2.R;
import com.liberasoft.zero.molitvenik2.db.DatabasePrayersListener;
import com.liberasoft.zero.molitvenik2.utils.Globals;

/**
 * Fragment represents prayers that user has posted to an app
 */
public class PersonalFragment extends Fragment {

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_personal, container, false);
    // Submenu navigator {daily, monthly, yearly submenu}
    LinearLayout subnavigator_parent = getActivity().findViewById(R.id.subnavigator_tab_view);
    subnavigator_parent.setVisibility(View.GONE);


    RecyclerView personalRecyclerView = view.findViewById(R.id.personal_recycler_view);
    DatabasePrayersListener databasePrayersListener = new DatabasePrayersListener(getActivity(), getChildFragmentManager());
    databasePrayersListener.retrieveData(personalRecyclerView);

    return view;
  }

  @Override
  public void onStart() {
    super.onStart();
    assert getFragmentManager() != null;
    Globals.setCurrentFragmentTag(getFragmentManager());
  }
}
