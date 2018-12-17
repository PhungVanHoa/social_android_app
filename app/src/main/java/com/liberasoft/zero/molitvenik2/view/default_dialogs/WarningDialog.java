package com.liberasoft.zero.molitvenik2.view.default_dialogs;

import android.app.AlertDialog;
import android.content.Context;

import com.liberasoft.zero.molitvenik2.R;

public class WarningDialog implements IDefaultDialog {


  @Override
  public AlertDialog.Builder createDialog(Context context, String title, String message) {
    return new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog_Alert)
            .setTitle(title)
            .setMessage(message)
            .setNeutralButton(context.getResources().getString(R.string.warning_dialog_understood_btn), (dialogInterface, i) -> dialogInterface.dismiss())
            .setCancelable(true);
  }
}
