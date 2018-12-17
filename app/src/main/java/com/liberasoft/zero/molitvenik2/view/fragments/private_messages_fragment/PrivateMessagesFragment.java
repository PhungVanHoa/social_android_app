package com.liberasoft.zero.molitvenik2.view.fragments.private_messages_fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liberasoft.zero.molitvenik2.R;

@SuppressLint("ValidFragment")
public class PrivateMessagesFragment extends Fragment {

  private FragmentManager fm;

  // Passing fragment manager in order to be able to transition to another fragment.
  // I would not need to pass fm if parent of this fragment was activity, but its parent is
  // another fragment.
  public PrivateMessagesFragment(FragmentManager fm) {
    this.fm = fm;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_private_messages, container, false);

    BottomAppBar bottomAppBar = view.findViewById(R.id.personal_messages_bottom_app_bar);

    return view;
  }
}
