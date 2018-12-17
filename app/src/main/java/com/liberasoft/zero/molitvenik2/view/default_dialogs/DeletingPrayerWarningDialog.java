package com.liberasoft.zero.molitvenik2.view.default_dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.liberasoft.zero.molitvenik2.R;
import com.liberasoft.zero.molitvenik2.utils.App;
import com.liberasoft.zero.molitvenik2.view.dialogs.read_prayer_dialog.ReadPrayerDialog;

import javax.inject.Inject;

import static com.liberasoft.zero.molitvenik2.utils.constants.DatabaseReference.REF_COMMENTS_ON_PRAYERS;
import static com.liberasoft.zero.molitvenik2.utils.constants.DatabaseReference.REF_PRAYERS;

@SuppressLint("ValidFragment")
public class DeletingPrayerWarningDialog extends ReadPrayerDialog implements IDefaultDialog {

  @Inject
  DatabaseReference databaseReference;

  private String id;
  private Activity activity;
  private Fragment fragment;

  public DeletingPrayerWarningDialog(Activity activity, String id, Fragment fragment) {
    this.activity = activity;
    this.id = id;
    this.fragment = fragment;
    ((App) this.activity.getApplication()).getDependencyComponent().inject(this);
  }

  @Override
  public AlertDialog.Builder createDialog(Context context, String title, String message) {
    return new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog_Alert)
            .setCancelable(true)
            .setPositiveButton(context.getResources().getString(R.string.deleting_prayer_positive_btn), (dialogInterface, i) -> {
              databaseReference.child(REF_PRAYERS).child(id).removeValue();
              databaseReference.child(REF_COMMENTS_ON_PRAYERS).child(id).removeValue();
              // Dismissing this dialog
              dialogInterface.dismiss();
              // Dismissing parent dialog
              dismissParentDialog();
            })
            .setNeutralButton(context.getResources().getString(R.string.deleting_prayer_negative_btn), new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
              }
            })
            .setTitle(title)
            .setMessage(message);
  }

  // If user does delete this prayer, he needs to be redirected to main screen of an app.
  // This means, parent dialog (reading prayer dialog fragment) needs to be closed.
  private void dismissParentDialog() {
    DialogFragment parentDialogFragment = (DialogFragment) fragment.getParentFragment();
    assert parentDialogFragment != null;
    parentDialogFragment.dismiss();
  }
}
