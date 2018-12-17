package com.liberasoft.zero.molitvenik2.db;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.liberasoft.zero.molitvenik2.R;
import com.liberasoft.zero.molitvenik2.adapter.PrayersFragmentAdapter;
import com.liberasoft.zero.molitvenik2.model.PrayersModel;
import com.liberasoft.zero.molitvenik2.utils.App;
import com.liberasoft.zero.molitvenik2.utils.Globals;
import com.liberasoft.zero.molitvenik2.utils.TimeStamp;
import com.liberasoft.zero.molitvenik2.utils.constants.ErrorHandleReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import static com.liberasoft.zero.molitvenik2.utils.constants.ViewReference.REF_DAILY_TAG;
import static com.liberasoft.zero.molitvenik2.utils.constants.ViewReference.REF_FAVORITES_TAG;
import static com.liberasoft.zero.molitvenik2.utils.constants.ViewReference.REF_MONTHLY_TAG;
import static com.liberasoft.zero.molitvenik2.utils.constants.ViewReference.REF_PERSONAL_TAG;
import static com.liberasoft.zero.molitvenik2.utils.constants.ViewReference.REF_YEARLY_TAG;

public class DatabasePrayersListener implements IDatabase {

  @Inject
  DatabaseReference fDatabase;

  private Activity activity;
  private FragmentManager fm;
  private List<PrayersModel> prayersList = new ArrayList<>();
  // Connected inside of the listener so user can search prayers
  private EditText searchEditText;

  public DatabasePrayersListener(Activity activity, FragmentManager fm) {
    this.activity = activity;
    this.fm = fm;
    // Fetching view from a parent activity
    searchEditText = this.activity.findViewById(R.id.subnavigator_search_view);
    // Dependency injection
    ((App) Objects.requireNonNull(activity).getApplication()).getDependencyComponent().inject(this);
  }

  @Override
  public void retrieveData(Object o) {
    fDatabase.child("prayers").addValueEventListener(getListener(o));
  }

  @Override
  public ValueEventListener getListener(Object o) {
    return new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        prayersList.clear();

        for (DataSnapshot s : dataSnapshot.getChildren()) {

          PrayersModel fetchedPrayersModel = s.getValue(PrayersModel.class);
          // If prayer is private, it will not be shown in time sort tab.
          // Private prayer will only be shown in personal tab.
          assert fetchedPrayersModel != null;

          switch (Globals.getCurrentFragmentTag()) {
            case REF_DAILY_TAG:
              if (isDailyData(fetchedPrayersModel.getDate(), TimeStamp.getDate()) && !fetchedPrayersModel.isPrayerPrivate()) {
                prayersList.add(fetchedPrayersModel);
              }
              break;
            case REF_MONTHLY_TAG:
              if (isMonthlyData(fetchedPrayersModel.getDate(), TimeStamp.getDate()) && !fetchedPrayersModel.isPrayerPrivate()) {
                prayersList.add(fetchedPrayersModel);
              }
              break;
            case REF_YEARLY_TAG:
              if (isYearlyData(fetchedPrayersModel.getDate(), TimeStamp.getDate()) && !fetchedPrayersModel.isPrayerPrivate()) {
                prayersList.add(fetchedPrayersModel);
              }
              break;
            case REF_PERSONAL_TAG:
              if (fetchedPrayersModel.getPostedBy().equals(Globals.getUsername())) {
                prayersList.add(fetchedPrayersModel);
              }
              break;
            case REF_FAVORITES_TAG:
              if (fetchedPrayersModel.getLikes() != null) {
                if (fetchedPrayersModel.getLikes().containsKey(Globals.getUsername())) {
                  prayersList.add(fetchedPrayersModel);
                }
              }
              break;

          }
        }
        // User can search any prayer in the list of a current fragment
        // Listener is activated when searchEditText view text is being changed
        searchEditText.addTextChangedListener(searchPrayerListener(o));
        // If user is reading a prayer background data wont be changing because
        // there is a possibility user is in a search mode. Variable might be
        // to radical.
        // Had to implement it cause if database was changed from read prayer dialog
        // recycler view would refresh itself cause it's asynchronous and user
        // searched prayers would not show, instead user would see all prayers again.
        // Leaved favorite fragment out because favorites fragment has to have dynamic change
        if (!Globals.isReadDialogOpen() && !Globals.getCurrentFragmentTag().equals(REF_FAVORITES_TAG)) {
          // Loading data to an adapter for a fragments
          PrayersFragmentAdapter prayersFragmentAdapter = new PrayersFragmentAdapter(activity, fm, prayersList);
          prayersFragmentAdapter.getRecyclerView((RecyclerView) o);
        } else {
          // I have enabled only favorites fragment to be able to dynamically in the background change recycler
          // view items. Only time when recycler view is updated in the background is when the item
          // is being removed, and Globals.isReadDialogOpen needs to be true for this to work
          PrayersFragmentAdapter prayersFragmentAdapter = new PrayersFragmentAdapter(activity, fm, prayersList);
          prayersFragmentAdapter.getRecyclerView((RecyclerView) o);
        }

        if (Globals.isSearchingPrayer()) {
          PrayersFragmentAdapter prayersFragmentAdapter = new PrayersFragmentAdapter(activity, fm, modifyPrayersList(searchEditText.getText().toString()));
          prayersFragmentAdapter.getRecyclerView((RecyclerView) o);
        }

      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    };
  }

  private boolean isDailyData(String testDate, String currentDate) {
    return testDate.substring(testDate.length()  - 10, testDate.length()).equals(currentDate.substring(testDate.length() - 10, testDate.length() ));
  }

  private boolean isMonthlyData(String testDate, String currentDate) {
    return testDate.substring(testDate.length() - 7, testDate.length())
            .equals(currentDate.substring(currentDate.length() - 7, currentDate.length()));
  }

  private boolean isYearlyData(String testDate, String currentDate) {
    return testDate.substring(testDate.length() - 4, testDate.length())
            .equals(currentDate.substring(currentDate.length() - 4, currentDate.length()));
  }

  private TextWatcher searchPrayerListener(Object o) {
    return new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        PrayersFragmentAdapter prayersFragmentAdapter = new PrayersFragmentAdapter(activity, fm, modifyPrayersList(charSequence.toString()));
        prayersFragmentAdapter.getRecyclerView((RecyclerView) o);
      }

      @Override
      public void afterTextChanged(Editable editable) {
      }
    };
  }

  private List<PrayersModel> modifyPrayersList(String searchInput) {
    List<PrayersModel> newPrayerList = new ArrayList<>();
    for (PrayersModel p : prayersList) {
      if (p.getTitle().contains(searchInput)) {
        newPrayerList.add(p);
      }
    }
    return newPrayerList;
  }


}
