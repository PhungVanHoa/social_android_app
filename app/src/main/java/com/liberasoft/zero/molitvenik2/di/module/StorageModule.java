package com.liberasoft.zero.molitvenik2.di.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class StorageModule {

  Context context;

  public StorageModule(Context context) {
    this.context = context;
  }

  @Provides
  Context providesContext() {
    return context;
  }

  @Provides
  SharedPreferences provideSharedPreference(Context context) {
    return PreferenceManager.getDefaultSharedPreferences(context);
  }
}
