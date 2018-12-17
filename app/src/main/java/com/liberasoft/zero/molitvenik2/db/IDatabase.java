package com.liberasoft.zero.molitvenik2.db;

import com.google.firebase.database.ValueEventListener;

public interface IDatabase<T> {

  void retrieveData(T t);

  ValueEventListener getListener(T t);
}
