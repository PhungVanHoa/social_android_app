package com.liberasoft.zero.molitvenik2.view.dialogs.write_prayer_dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.liberasoft.zero.molitvenik2.R;
import com.liberasoft.zero.molitvenik2.model.PrayersModel;
import com.liberasoft.zero.molitvenik2.utils.App;
import com.liberasoft.zero.molitvenik2.utils.Globals;
import com.liberasoft.zero.molitvenik2.utils.TimeStamp;
import com.liberasoft.zero.molitvenik2.view.animations.DynamicAnimations;
import com.liberasoft.zero.molitvenik2.view.default_dialogs.IDefaultDialog;
import com.liberasoft.zero.molitvenik2.view.default_dialogs.WarningDialog;

import java.util.UUID;

import javax.inject.Inject;

public class WritePrayerDialog extends DialogFragment implements View.OnClickListener {

  @Inject
  DatabaseReference databaseReference;

  private TextView prayerTitleView;
  private TextView prayerTextView;
  // User can check this box in order for his prayer stay private. None of the users
  // will be able to see this prayer. With this option, user can somewhat personalize
  // application.
  private CheckBox privatePrayerCheckbox;
  // Dialog buttons. Chose CardView for the better layout.
  private CardView cancelBtn;
  private CardView sendBtn;
  // Warning dialogs to indicate users what will happen on click
  private IDefaultDialog iDefaultDialog;

  public WritePrayerDialog() {
    this.iDefaultDialog = new WarningDialog();
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.dialog_write_prayer, container, false);
    // Dependency injection
    ((App) (getActivity()).getApplication()).getDependencyComponent().inject(this);

    initializeViews(view);
    initializeViewListener();

    return view;
  }

  private boolean isPrayerPrivate() {
    return privatePrayerCheckbox.isChecked();
  }

  private void initializeViewListener() {
    cancelBtn.setOnClickListener(this);
    sendBtn.setOnClickListener(this);
  }

  private void initializeViews(View view) {
    prayerTitleView = view.findViewById(R.id.prayer_title_view);
    prayerTextView = view.findViewById(R.id.prayer_text_view);
    privatePrayerCheckbox = view.findViewById(R.id.private_prayer_checkbox);
    cancelBtn = view.findViewById(R.id.write_prayer_dialog_cancel);
    sendBtn = view.findViewById(R.id.write_prayer_dialog_send);
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.write_prayer_dialog_send:
        // Adding unique value instead of push firebase value so I can rewrite the same value directly
        // without need to read values to first determine which node is mine before I change anything
        String uniqueId = UUID.randomUUID().toString();
        PrayersModel newPrayer = new PrayersModel(getTitle(), getText(), TimeStamp.getDate(), Globals.getUsername(), uniqueId, null, isPrayerPrivate());

        if (isPrayerWritten()) {
          databaseReference.child("prayers").child(newPrayer.getId()).setValue(newPrayer);
          this.dismiss();
          String resWarTitle = getResources().getString(R.string.warning_dialog_private_prayer_title);
          String resWarText = getResources().getString(R.string.warning_dialog_private_prayer_text);
          if(isPrayerPrivate()) {
            // Showing warning dialog to the user that this pray will only be seen in
            // his personal prayers
            iDefaultDialog.createDialog(getContext(), resWarTitle, resWarText).show();
          }
        } else {
          Toast.makeText(getActivity(), getResources().getString(R.string.no_prayer_warning), Toast.LENGTH_SHORT).show();
        }
        break;
      case R.id.write_prayer_dialog_cancel:
        this.dismiss();
        break;

    }
  }

  private boolean isPrayerWritten() {
    int title = prayerTitleView.getText().length();
    int text = prayerTextView.getText().length();
    return title > 4 && text > 10;
  }

  private String getTitle() {
    return prayerTitleView.getText().toString();
  }

  private String getText() {
    return prayerTextView.getText().toString();
  }

  private void reverseAnimationOnDismiss() {
    ImageButton newPrayerBtn = getActivity().findViewById(R.id.new_prayer_btn);
    DynamicAnimations.reverseAnim(newPrayerBtn);
    DynamicAnimations.changeColor(getActivity(), newPrayerBtn, R.color.status_bar);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    reverseAnimationOnDismiss();
    // Clearing memory
    Runtime.getRuntime().gc();
  }
}
