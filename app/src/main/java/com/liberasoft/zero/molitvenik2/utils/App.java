package com.liberasoft.zero.molitvenik2.utils;

import android.app.Application;

import com.liberasoft.zero.molitvenik2.di.component.DaggerDependencyComponent;
import com.liberasoft.zero.molitvenik2.di.component.DependencyComponent;
import com.liberasoft.zero.molitvenik2.di.module.NetworkModule;
import com.liberasoft.zero.molitvenik2.di.module.StorageModule;

public class App extends Application {

  private DependencyComponent dependencyComponent;

  @Override
  public void onCreate() {
    super.onCreate();

    dependencyComponent = DaggerDependencyComponent.builder()
            .networkModule(new NetworkModule())
            .storageModule(new StorageModule(getApplicationContext()))
            .build();
  }

  public DependencyComponent getDependencyComponent() {
    return dependencyComponent;
  }
}
