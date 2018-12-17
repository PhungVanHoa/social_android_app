package com.liberasoft.zero.molitvenik2.view;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liberasoft.zero.molitvenik2.R;
import com.liberasoft.zero.molitvenik2.utils.FragmentTransition;
import com.liberasoft.zero.molitvenik2.utils.Globals;
import com.liberasoft.zero.molitvenik2.utils.SoftKeyboard;
import com.liberasoft.zero.molitvenik2.view.animations.DynamicAnimations;
import com.liberasoft.zero.molitvenik2.view.fragments.private_messages_fragment.PrivateMessagesFragment;
import com.liberasoft.zero.molitvenik2.view.dialogs.write_prayer_dialog.WritePrayerDialog;
import com.liberasoft.zero.molitvenik2.view.fragments.favorite_fragment.FavoritesFragment;
import com.liberasoft.zero.molitvenik2.view.fragments.main_fragment.DailyFragment;
import com.liberasoft.zero.molitvenik2.view.fragments.main_fragment.MonthlyFragment;
import com.liberasoft.zero.molitvenik2.view.fragments.main_fragment.YearlyFragment;
import com.liberasoft.zero.molitvenik2.view.fragments.personal_fragment.PersonalFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.liberasoft.zero.molitvenik2.utils.constants.ViewReference.REF_DAILY_TAG;
import static com.liberasoft.zero.molitvenik2.utils.constants.ViewReference.REF_FAVORITES_TAG;
import static com.liberasoft.zero.molitvenik2.utils.constants.ViewReference.REF_MONTHLY_TAG;
import static com.liberasoft.zero.molitvenik2.utils.constants.ViewReference.REF_PERSONAL_TAG;
import static com.liberasoft.zero.molitvenik2.utils.constants.ViewReference.REF_WRITING_PRIVATE_MESSAGE_FRAGMENT;
import static com.liberasoft.zero.molitvenik2.utils.constants.ViewReference.REF_YEARLY_TAG;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private FragmentManager fragmentManager;
  // Menu buttons
  private ImageButton newPrayerBtn;
  private ImageButton searchPrayerBtn;
  private ImageButton privateMessagesBtn;
  // Sub tabs parent
  private LinearLayout sortedPrayersTab;
  private EditText searchPrayerEditText;
  // Sub tabs children
  private TextView dailyFragmentView;
  private TextView monthlyFragmentView;
  private TextView yearlyFragmentView;

  {
    Globals.setSearchingPrayer(false);
  }

  @SuppressLint("CommitTransaction")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    fragmentManager = getSupportFragmentManager();
    // Main top tab for public, personal and favorite prayers
    loadTopNavigation();
    // Initializing landing fragment in this activity
    changeFragment(new DailyFragment(), REF_DAILY_TAG);
    // Loading majority of activity views to an activity
    initializeClassViews();
    // Adding listeners to a views
    initializeClassViewListeners();
  }

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
          = item -> {
    switch (item.getItemId()) {
      case R.id.main_fragment:
        changeFragment(new DailyFragment(), REF_DAILY_TAG);
        return true;
      case R.id.personal_fragment:
        changeFragment(new PersonalFragment(), REF_PERSONAL_TAG);
        return true;
      case R.id.favorites_fragment:
        changeFragment(new FavoritesFragment(), REF_FAVORITES_TAG);
        return true;

    }
    return false;
  };

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.fragment_daily_view:
        changeFragment(new DailyFragment(), REF_DAILY_TAG);
        break;
      case R.id.fragment_monthly_view:
        changeFragment(new MonthlyFragment(), REF_MONTHLY_TAG);
        break;
      case R.id.fragment_yearly_view:
        changeFragment(new YearlyFragment(), REF_YEARLY_TAG);
        break;
      case R.id.new_prayer_btn:
        writeNewPrayer();
        break;
      case R.id.search_prayer_btn:
        replaceSearchTab();
        break;
      case R.id.private_messages_btn:
        openPrivateMessagesFragment();
        break;
    }
  }

  // Functionality of a search button used to search specific prayers in all tabs.
  // This button is on global scale, visible in all fragments.
  private void replaceSearchTab() {
    if (Globals.isSearchingPrayer()) {
      // Loading current fragment where user is to a static variable
      Globals.setCurrentFragmentTag(getSupportFragmentManager());
      // Setting static variable to recognize that user is not in searching mode
      Globals.setSearchingPrayer(false);
      // Playing animations and color change
      DynamicAnimations.reverseAnim(searchPrayerBtn);
      DynamicAnimations.changeColor(this, searchPrayerBtn, R.color.status_bar);
      // Closing phone keyboard if it is open and user has closed search tab on button click
      SoftKeyboard.close(this, searchPrayerEditText);
      // Removing any text left from a view so user can see all prayers again.
      // Search function (searchPrayerEditText) is operating over textChangeListener
      searchPrayerEditText.setText("");
      searchPrayerEditText.setVisibility(View.GONE);
      // If user is not located on sorting by time main tab, sorting by time tab will not be shown.
      List<String> sortByTimeFragments = new ArrayList<>(Arrays.asList(REF_DAILY_TAG, REF_MONTHLY_TAG, REF_YEARLY_TAG));
      if (sortByTimeFragments.contains(Globals.getCurrentFragmentTag()))
        sortedPrayersTab.setVisibility(View.VISIBLE);
    } else {
      // Opening search tab
      Globals.setSearchingPrayer(true);
      Globals.setCurrentFragmentTag(getSupportFragmentManager());
      DynamicAnimations.playAnimation(searchPrayerBtn);
      DynamicAnimations.changeColor(this, searchPrayerBtn, R.color.yellow);
      sortedPrayersTab.setVisibility(View.GONE);
      searchPrayerEditText.setVisibility(View.VISIBLE);
    }
  }

  private void openPrivateMessagesFragment() {
    android.support.v4.app.Fragment fragment = new PrivateMessagesFragment(getSupportFragmentManager());
    FragmentTransition
            .createTransition(getSupportFragmentManager(), R.id.private_messages_container, fragment, REF_WRITING_PRIVATE_MESSAGE_FRAGMENT)
            // So we can return to main activity from fragment on backpress.
            .addToBackStack(null)
            .commit();
  }

  // Button on global scale visible to all fragment. Used to write new prayer to a database.
  @SuppressLint("CommitTransaction")
  private void writeNewPrayer() {
    Fragment prev = getFragmentManager().findFragmentByTag("dialog");
    if (prev != null) {
      getFragmentManager().beginTransaction().remove(prev);
    }
    getFragmentManager().beginTransaction().addToBackStack(null);
    DialogFragment dialogFragment = new WritePrayerDialog();
    dialogFragment.setStyle(0, R.style.DialogCustomTheme);
    dialogFragment.show(getSupportFragmentManager(), "dialog");
    // Playing animation on button click. Animation reversed when dialog is closed.
    DynamicAnimations.playAnimation(newPrayerBtn);
    DynamicAnimations.changeColor(this, newPrayerBtn, R.color.yellow);
  }

  private void changeFragment(android.support.v4.app.Fragment fragment, String fragmentType) {
    FragmentTransition.createTransition(fragmentManager, R.id.activity_main_fragment_container, fragment, fragmentType).commit();
  }

  private void initializeClassViews() {
    // Bottom screen buttons
    newPrayerBtn = findViewById(R.id.new_prayer_btn);
    searchPrayerBtn = findViewById(R.id.search_prayer_btn);
    privateMessagesBtn = findViewById(R.id.private_messages_btn);
    // Sub tab
    sortedPrayersTab = findViewById(R.id.subnavigator_tab_view);
    searchPrayerEditText = findViewById(R.id.subnavigator_search_view);
    // Main tab
    dailyFragmentView = findViewById(R.id.fragment_daily_view);
    monthlyFragmentView = findViewById(R.id.fragment_monthly_view);
    yearlyFragmentView = findViewById(R.id.fragment_yearly_view);

  }

  private void initializeClassViewListeners() {
    dailyFragmentView.setOnClickListener(this);
    monthlyFragmentView.setOnClickListener(this);
    yearlyFragmentView.setOnClickListener(this);
    newPrayerBtn.setOnClickListener(this);
    searchPrayerBtn.setOnClickListener(this);
    privateMessagesBtn.setOnClickListener(this);
  }

  private void loadTopNavigation() {
    BottomNavigationView navigation = findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
  }

}
