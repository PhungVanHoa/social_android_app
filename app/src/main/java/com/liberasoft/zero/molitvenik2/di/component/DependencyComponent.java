package com.liberasoft.zero.molitvenik2.di.component;

import com.liberasoft.zero.molitvenik2.db.DatabaseCommentListener;
import com.liberasoft.zero.molitvenik2.db.DatabasePrayersListener;
import com.liberasoft.zero.molitvenik2.db.DatabaseRegistrationListener;
import com.liberasoft.zero.molitvenik2.di.module.NetworkModule;
import com.liberasoft.zero.molitvenik2.di.module.StorageModule;
import com.liberasoft.zero.molitvenik2.view.RegisterUserActivity;
import com.liberasoft.zero.molitvenik2.view.default_dialogs.DeletingPrayerWarningDialog;
import com.liberasoft.zero.molitvenik2.view.dialogs.read_prayer_dialog.CommentsFragment;
import com.liberasoft.zero.molitvenik2.view.dialogs.read_prayer_dialog.PrayingFragment;
import com.liberasoft.zero.molitvenik2.view.dialogs.write_prayer_dialog.WritePrayerDialog;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetworkModule.class, StorageModule.class})
public interface DependencyComponent {

  // Fetching data from Firebase
  void inject(DatabasePrayersListener DatabasePrayersListener);

  // Storing username to internal memory
  void inject(RegisterUserActivity registerUserActivity);

  // Adding username to prayer model that will be sent as a prayer to firebasex
  void inject(WritePrayerDialog writePrayerDialog);

  // User is able to edit prayer in database
  void inject(PrayingFragment prayingFragment);

  // User database connection
  void inject(DatabaseRegistrationListener DatabaseRegistrationListener);

  // Listening to prayer likes
  void inject(DatabaseCommentListener databaseCommentListener);

  // Listening to specific prayer comments
  void inject(CommentsFragment commentsFragment);

  // Removing prayer from a database completely
  void inject(DeletingPrayerWarningDialog deletingPrayerWarningDialog);

}
