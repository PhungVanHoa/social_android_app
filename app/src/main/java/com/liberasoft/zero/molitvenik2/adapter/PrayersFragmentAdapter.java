package com.liberasoft.zero.molitvenik2.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liberasoft.zero.molitvenik2.R;
import com.liberasoft.zero.molitvenik2.model.PrayersModel;
import com.liberasoft.zero.molitvenik2.utils.TimeStamp;
import com.liberasoft.zero.molitvenik2.view.dialogs.read_prayer_dialog.ReadPrayerDialog;

import java.util.Collections;
import java.util.List;

import static com.liberasoft.zero.molitvenik2.utils.constants.PassingArgumentsReference.REF_ARGUMENT_DATE;
import static com.liberasoft.zero.molitvenik2.utils.constants.PassingArgumentsReference.REF_ARGUMENT_ID;
import static com.liberasoft.zero.molitvenik2.utils.constants.PassingArgumentsReference.REF_ARGUMENT_PEOPLE_PRAYED;
import static com.liberasoft.zero.molitvenik2.utils.constants.PassingArgumentsReference.REF_ARGUMENT_POSTED_BY;
import static com.liberasoft.zero.molitvenik2.utils.constants.PassingArgumentsReference.REF_ARGUMENT_TEXT;
import static com.liberasoft.zero.molitvenik2.utils.constants.PassingArgumentsReference.REF_ARGUMENT_TITLE;

public class PrayersFragmentAdapter {

  private Activity activity;
  private FragmentManager fm;
  private List<PrayersModel> mainItemList;

  public PrayersFragmentAdapter(Activity activity, FragmentManager fm, List<PrayersModel> mainItemList) {
    this.activity = activity;
    this.fm = fm;
    this.mainItemList = mainItemList;
    // Sorting array by time posted into a recycler view.
    Collections.sort(this.mainItemList, (p1, p2) -> String.valueOf(TimeStamp.convertInSeconds(p2.getDate())).compareTo(String.valueOf(TimeStamp.convertInSeconds(p1.getDate()))));
  }

  public void getRecyclerView(RecyclerView recyclerView) {

    recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayout.VERTICAL, false));
    recyclerView.setHasFixedSize(true);
    recyclerView.setAdapter(new MainRecycler());
  }

  private class MainRecycler extends RecyclerView.Adapter<MainRecycler.CustomViewHolder> {

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
      View view = LayoutInflater.from(activity).inflate(R.layout.recycler_item_main, viewGroup, false);

      return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder h, int i) {
      // Adding data to views


      h.titleView.setText(mainItemList.get(i).getTitle());
      // Instead of passing real time to view I am passing time left since it's been posted.
      h.dateView.setText(TimeStamp.timePassed(activity, mainItemList.get(i).getDate()));
      h.idView.setText(mainItemList.get(i).getId());

    }

    @Override
    public int getItemCount() {
      return mainItemList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

      TextView dateView, titleView, idView;
      LinearLayout parent;

      CustomViewHolder(@NonNull View itemView) {
        super(itemView);

        titleView = itemView.findViewById(R.id.title_view);
        dateView = itemView.findViewById(R.id.date_view);
        idView = itemView.findViewById(R.id.id_view);
        parent = itemView.findViewById(R.id.main_recycler_item_parent);


        // Reading specific prayer on click
        itemView.setOnClickListener(view -> {
          for (PrayersModel m : mainItemList) {
            if (m.getId().equals(idView.getText().toString())) {
              openDialog(m);
            }
          }
        });

      }
    }
  }

  private Bundle passDialogData(PrayersModel prayerModel) {
    Bundle args = new Bundle();
    args.putString(REF_ARGUMENT_TITLE, prayerModel.getTitle());
    args.putString(REF_ARGUMENT_TEXT, prayerModel.getText());
    args.putString(REF_ARGUMENT_DATE, prayerModel.getDate());
    args.putString(REF_ARGUMENT_POSTED_BY, prayerModel.getPostedBy());
    args.putString(REF_ARGUMENT_ID, prayerModel.getId());
    args.putSerializable(REF_ARGUMENT_PEOPLE_PRAYED, prayerModel.getLikes());
    return args;
  }

  private void openDialog(PrayersModel prayerModel) {
    Fragment prev = activity.getFragmentManager().findFragmentByTag("read_prayer_dialog");
    if (prev != null) {
      activity.getFragmentManager().beginTransaction().remove(prev);
    }
    activity.getFragmentManager().beginTransaction().addToBackStack(null);
    DialogFragment dialogFragment = new ReadPrayerDialog();
    dialogFragment.setStyle(0, R.style.DialogCustomTheme);
    dialogFragment.show(fm, "read_prayer_dialog");


    dialogFragment.setArguments(passDialogData(prayerModel));
  }
}
