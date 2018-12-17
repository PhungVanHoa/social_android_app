package com.liberasoft.zero.molitvenik2.db;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.liberasoft.zero.molitvenik2.adapter.ReadingCommentsAdapter;
import com.liberasoft.zero.molitvenik2.model.ReadingCommentsModel;
import com.liberasoft.zero.molitvenik2.utils.App;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.liberasoft.zero.molitvenik2.utils.constants.DatabaseReference.REF_COMMENTS_ON_PRAYERS;

/**
 * Used to listen to the chat of each prayer in the app. User is able to write chat in single
 * prayer in prayer_reading_dialog.
 */
public class DatabaseCommentListener implements IDatabase {

  @Inject
  DatabaseReference fDatabase;

  private String prayerId;
  private Activity activity;
  // Adapter connecting data with a recycle view
  private ReadingCommentsAdapter readingCommentsAdapter;
  // Comments list fetched from firebase
  private List<ReadingCommentsModel> prayersList;

  /**
   * @param prayerId represents id of a single prayer in database. Id is unique UUID value.
   */
  public DatabaseCommentListener(Activity activity, String prayerId) {
    // Unique prayer ID. Is used as a chat room.
    this.prayerId = prayerId;
    this.activity = activity;
    // Injection
    ((App) this.activity.getApplication()).getDependencyComponent().inject(this);

    // Initializing comments list
    prayersList = new ArrayList<>();
  }

  @Override
  public void retrieveData(Object o) {
    // Listening to values os a specific prayer.
    fDatabase.child(REF_COMMENTS_ON_PRAYERS).child(prayerId).addValueEventListener(getListener(o));
  }

  /**
   * Loading data comments to recycler view
   *
   * @param o recycler view
   */
  @Override
  public ValueEventListener getListener(Object o) {
    return new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        // Clearing past data from a list so they don't
        prayersList.clear();

        for (DataSnapshot s : dataSnapshot.getChildren()) {
          // Adding model to a list
          ReadingCommentsModel readingCommentsModel = s.getValue(ReadingCommentsModel.class);
          prayersList.add(readingCommentsModel);
        }
        // Adapter
        readingCommentsAdapter = new ReadingCommentsAdapter(activity, prayersList);
        readingCommentsAdapter.getRecyclerView((RecyclerView) o);
        // todo Sound should be played every time someone types new comment while you're scrolling the chat
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    };
  }
}
