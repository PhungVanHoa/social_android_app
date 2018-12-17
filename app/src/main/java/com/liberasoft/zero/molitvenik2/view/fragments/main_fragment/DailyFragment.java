package com.liberasoft.zero.molitvenik2.view.fragments.main_fragment;

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
import com.liberasoft.zero.molitvenik2.view.fragments.manager.FragmentViewManager;

public class DailyFragment extends Fragment {


  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_by_time, container, false);
    FragmentViewManager.changeColor(getActivity(), "daily");

    LinearLayout subnavigator_parent = getActivity().findViewById(R.id.subnavigator_tab_view);
    // If user is in a search mode (search tab open), sort by time tab wont turn visible
    if (!Globals.isSearchingPrayer()) {
      subnavigator_parent.setVisibility(View.VISIBLE);
    }

    RecyclerView recyclerView = view.findViewById(R.id.by_time_recycler_view);
    DatabasePrayersListener DatabasePrayersListener = new DatabasePrayersListener(getActivity(), getChildFragmentManager());
    DatabasePrayersListener.retrieveData(recyclerView);


    return view;
  }

  @Override
  public void onStart() {
    super.onStart();
    Globals.setCurrentFragmentTag(getFragmentManager());
  }
}
