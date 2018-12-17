package com.liberasoft.zero.molitvenik2.view.default_dialogs;

import android.app.AlertDialog;
import android.content.Context;

public interface IDefaultDialog {

  AlertDialog.Builder createDialog(Context context, String title, String message);
}
