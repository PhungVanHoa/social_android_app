package com.liberasoft.zero.molitvenik2.di.module;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dagger.Module;
import dagger.Provides;

@Module
public class NetworkModule {

  @Provides
  DatabaseReference provideFirebase() {
    return FirebaseDatabase.getInstance().getReference();
  }


}
