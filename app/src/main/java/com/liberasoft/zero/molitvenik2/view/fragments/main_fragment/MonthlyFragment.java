package com.liberasoft.zero.molitvenik2.view.fragments.main_fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liberasoft.zero.molitvenik2.R;
import com.liberasoft.zero.molitvenik2.db.DatabasePrayersListener;
import com.liberasoft.zero.molitvenik2.utils.Globals;
import com.liberasoft.zero.molitvenik2.view.fragments.manager.FragmentViewManager;

import java.util.Objects;

public class MonthlyFragment extends Fragment {

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_by_time, container, false);
    FragmentViewManager.changeColor(getActivity(), "monthly");

    TextView fragmentTab = Objects.requireNonNull(getActivity()).findViewById(R.id.fragment_monthly_view);

    RecyclerView recyclerView = view.findViewById(R.id.by_time_recycler_view);
    DatabasePrayersListener DatabasePrayersListener = new DatabasePrayersListener(getActivity(), getChildFragmentManager());
    DatabasePrayersListener.retrieveData(recyclerView);


    return view;
  }

  @Override
  public void onStart() {
    super.onStart();
    assert getFragmentManager() != null;
    Globals.setCurrentFragmentTag(getFragmentManager());
  }
}
