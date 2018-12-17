package com.liberasoft.zero.molitvenik2.view.dialogs.read_prayer_dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liberasoft.zero.molitvenik2.R;
import com.liberasoft.zero.molitvenik2.utils.FragmentTransition;
import com.liberasoft.zero.molitvenik2.view.dialogs.read_prayer_dialog.PrayingFragment;

import static com.liberasoft.zero.molitvenik2.utils.constants.ViewReference.REF_READING_DIALOG_PRAYER_FRAGMENT;

@SuppressWarnings("unchecked")
public class ReadPrayerDialog extends DialogFragment {
  /**
   * 2 fragments exist in this dialog, reading prayer fragment and comments fragment
   */
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.dialog_read_prayer, container, false);

    // Passing an argument that has been set for this dialog next to reading prayer fragment
    Bundle passingPrayerArgument = getArguments();
    // Loading passed argument into a fragment
    Fragment readPrayerFragment = new PrayingFragment(getChildFragmentManager());
    readPrayerFragment.setArguments(passingPrayerArgument);
    // Initializing reading prayers fragment. Transitioning to different fragment.
    FragmentTransition.createTransition(getChildFragmentManager(), R.id.reading_dialog_fragment_container, readPrayerFragment, REF_READING_DIALOG_PRAYER_FRAGMENT).commit();

    return view;
  }
}