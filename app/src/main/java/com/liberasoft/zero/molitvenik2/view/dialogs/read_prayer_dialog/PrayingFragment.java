package com.liberasoft.zero.molitvenik2.view.dialogs.read_prayer_dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.liberasoft.zero.molitvenik2.R;
import com.liberasoft.zero.molitvenik2.utils.App;
import com.liberasoft.zero.molitvenik2.utils.FragmentTransition;
import com.liberasoft.zero.molitvenik2.utils.Globals;
import com.liberasoft.zero.molitvenik2.utils.TimeStamp;
import com.liberasoft.zero.molitvenik2.view.default_dialogs.DeletingPrayerWarningDialog;

import java.util.HashMap;
import java.util.Objects;

import javax.inject.Inject;

import static com.liberasoft.zero.molitvenik2.utils.constants.DatabaseReference.REF_PRAYERS;
import static com.liberasoft.zero.molitvenik2.utils.constants.PassingArgumentsReference.REF_ARGUMENT_DATE;
import static com.liberasoft.zero.molitvenik2.utils.constants.PassingArgumentsReference.REF_ARGUMENT_ID;
import static com.liberasoft.zero.molitvenik2.utils.constants.PassingArgumentsReference.REF_ARGUMENT_PEOPLE_PRAYED;
import static com.liberasoft.zero.molitvenik2.utils.constants.PassingArgumentsReference.REF_ARGUMENT_POSTED_BY;
import static com.liberasoft.zero.molitvenik2.utils.constants.PassingArgumentsReference.REF_ARGUMENT_TEXT;
import static com.liberasoft.zero.molitvenik2.utils.constants.PassingArgumentsReference.REF_ARGUMENT_TITLE;
import static com.liberasoft.zero.molitvenik2.utils.constants.ViewReference.REF_READING_DIALOG_COMMENTS_FRAGMENT;

@SuppressLint("ValidFragment")
public class PrayingFragment extends Fragment implements View.OnClickListener {

  @Inject
  DatabaseReference databaseReference;

  private LinearLayout editPrayerParent;
  private TextView textView;
  private EditText editPrayerText;
  private TextView titleView;
  private TextView postedByView;
  private TextView prayCountTextView;
  // Bottom dialog main buttons
  private CardView prayBtn;
  private CardView commentsFragmentBtn;
  private CardView openEditorBtn;
  // Btn texts & icons
  private TextView prayBtnText;
  private ImageView prayBtnIcon;

  private ImageButton editPrayerBtnSave;
  private ImageButton editPrayerBtnCancel;
  private ImageButton editPrayerBtnDeletePrayer;

  private RecyclerView commentsRecyclerView;
  private Button addCommentBtn;

  // Local variables
  private boolean isEditorOpen = false;
  private String title;
  private String text;
  private String date;
  private String postedBy;
  private String id;
  private HashMap<String, String> peoplePrayedMap;

  // Primitive variables
  private boolean isPrayerBeingPrayed;

  // Passing fragment manager from a higher in a hierarchy.
  // We have to pass it in order to work cause we are calling fragment from fragment.
  private FragmentManager fm;

  public PrayingFragment(FragmentManager fm) {
    this.fm = fm;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_read_prayer, container, false);

    ((App) Objects.requireNonNull(getActivity()).getApplication()).getDependencyComponent().inject(this);

    loadPassedData();
    loadViews(view);
    initializeListeners();
    setInitialText();


    return view;
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.edit_btn_view:
        if (!isEditorOpen) {
          switchToEditor();
          isEditorOpen = true;
        } else {
          switchToReader();
          isEditorOpen = false;
        }
        break;
      case R.id.edit_prayer_btn_save:
        saveEditedText();
        switchToReader();
        break;
      case R.id.pray_btn_view:
        likePrayer();
        break;
      case R.id.edit_prayer_btn_cancel:
        switchToReader();
        isEditorOpen = false;
        break;
      case R.id.comments_btn_view:
        commentsFragmentTransition();
        break;
      case R.id.edit_prayer_btn_delete_prayer:
        deletePrayerFromDatabase(id);
        break;
    }
  }

  private void deletePrayerFromDatabase(String id) {
    /*// Removing prayer text node
    databaseReference.child(REF_PRAYERS).child(id).removeValue();
    // Removing prayer comments node
    databaseReference.child(REF_COMMENTS_ON_PRAYERS).child(id).removeValue();*/
    String delDialTitle = getResources().getString(R.string.deleting_prayer_dialog_title);
    String delDialMessage = getResources().getString(R.string.deleting_prayer_dialog_message);
    DeletingPrayerWarningDialog dialog = new DeletingPrayerWarningDialog(getActivity(), id, this);

    dialog.createDialog(getContext(), delDialTitle, delDialMessage).show();
  }


  private void loadPassedData() {
    assert getArguments() != null;
    title = getArguments().getString(REF_ARGUMENT_TITLE);
    date = getArguments().getString(REF_ARGUMENT_DATE);
    text = getArguments().getString(REF_ARGUMENT_TEXT);
    postedBy = getArguments().getString(REF_ARGUMENT_POSTED_BY);
    id = getArguments().getString(REF_ARGUMENT_ID);
    peoplePrayedMap = (HashMap<String, String>) getArguments().getSerializable(REF_ARGUMENT_PEOPLE_PRAYED);
    // If user just created new prayer and it has no peoplePrayed HashMap, it will return an error
    // trying to count the amount in data passed so we're initialized it down below
    if (peoplePrayedMap == null) {
      peoplePrayedMap = new HashMap<>();
    }
    isPrayerBeingPrayed = peoplePrayedMap.containsKey(Globals.getUsername());
  }

  private void commentsFragmentTransition() {
    CommentsFragment commentsFragment = new CommentsFragment(fm);
    commentsFragment.setArguments(loadPassingArguments());
    // Preceding to a comment fragment with the passing arguments.
    FragmentTransition.createTransition(fm, R.id.reading_dialog_fragment_container, commentsFragment, REF_READING_DIALOG_COMMENTS_FRAGMENT).commit();
  }

  // Passing arguments previous loaded in this fragment onto next fragment
  private Bundle loadPassingArguments() {
    // I need to add new argument instead of just passing an old one. If user edited a prayer, we need
    // to add a new bundle instead of passing an old bundle that came from main sort by time fragment.
    Bundle newArgumentBundle = getArguments();
    // Overriding the existing REF_ARGUMENT_TEXT in case user edited text.
    newArgumentBundle.putString(REF_ARGUMENT_TEXT, textView.getText().toString());
    return newArgumentBundle;
  }

  private void likePrayer() {

    if (isPrayerBeingPrayed) {
      // Removing like from a prayer if it's already been registered as liked
      prayCountTextView.setText(String.valueOf(Integer.parseInt(prayCountTextView.getText().toString()) - 1));
      databaseReference.child(REF_PRAYERS).child(id).child("likes").child(Globals.getUsername()).removeValue();
      Toast.makeText(getActivity(), getResources().getString(R.string.prayer_removed_from_favorites), Toast.LENGTH_SHORT).show();
      isPrayerBeingPrayed = false;
    } else {
      // Liking prayer
      prayCountTextView.setText(String.valueOf(Integer.parseInt(prayCountTextView.getText().toString()) + 1));
      databaseReference.child(REF_PRAYERS).child(id).child("likes").child(Globals.getUsername()).setValue(TimeStamp.getDate());
      Toast.makeText(getActivity(), getResources().getString(R.string.prayer_added_to_favorites), Toast.LENGTH_SHORT).show();
      isPrayerBeingPrayed = true;
    }
    markBtnPraying();
  }

  private void initializeListeners() {
    prayBtn.setOnClickListener(this);
    openEditorBtn.setOnClickListener(this);
    editPrayerBtnSave.setOnClickListener(this);
    editPrayerBtnCancel.setOnClickListener(this);
    commentsFragmentBtn.setOnClickListener(this);
    editPrayerBtnDeletePrayer.setOnClickListener(this);

  }

  private void loadViews(View view) {
    titleView = view.findViewById(R.id.prayer_title_view);
    textView = view.findViewById(R.id.prayer_text_view);
    postedByView = view.findViewById(R.id.posted_by_text_view);
    prayCountTextView = view.findViewById(R.id.pray_view_count);
    prayBtn = view.findViewById(R.id.pray_btn_view);
    commentsFragmentBtn = view.findViewById(R.id.comments_btn_view);
    openEditorBtn = view.findViewById(R.id.edit_btn_view);
    editPrayerParent = view.findViewById(R.id.edit_prayer_parent);
    editPrayerText = view.findViewById(R.id.edit_prayer_text);
    editPrayerBtnSave = view.findViewById(R.id.edit_prayer_btn_save);
    editPrayerBtnCancel = view.findViewById(R.id.edit_prayer_btn_cancel);
    editPrayerBtnDeletePrayer = view.findViewById(R.id.edit_prayer_btn_delete_prayer);
    prayBtnText = view.findViewById(R.id.pray_btn_text_view);
    prayBtnIcon = view.findViewById(R.id.pray_btn_icon_view);
    // Setting text programmatically to be scrollable with limited max lines.
    // Could not achieve limited height of dialog with unlimited text in text view.
    textView.setMovementMethod(new ScrollingMovementMethod());
    // Removing edit prayer button if it's not users personal prayer
    if (!postedBy.equals(Globals.getUsername())) {
      openEditorBtn.setVisibility(View.GONE);
    }
    // Changing initial view of button if prayer is already being played.
    markBtnPraying();
  }

  // Changes button layout according to database. If user is praying for that specific prayer
  // or not.
  private void markBtnPraying() {
    if (isPrayerBeingPrayed) {
      prayBtnText.setTextColor(getResources().getColor(R.color.yellow));
      prayBtnIcon.setColorFilter(getResources().getColor(R.color.yellow));
    } else {
      prayBtnText.setTextColor(getResources().getColor(R.color.white));
      prayBtnIcon.setColorFilter(getResources().getColor(R.color.white));
    }
  }

  // Adding text to a views in dialog fragment
  private void setInitialText() {
    titleView.setText(title);
    textView.setText(text);
    postedByView.setText("piše " + postedBy);
    prayCountTextView.setText(peoplePrayedCount());
  }

  private String peoplePrayedCount() {
    return String.valueOf(peoplePrayedMap.size());
  }

  private void saveEditedText() {
    databaseReference.child("prayers").child(id).child("text").setValue(editPrayerText.getText().toString());
    isEditorOpen = false;
  }

  private void switchToEditor() {
    editPrayerParent.setVisibility(View.VISIBLE);
    textView.setVisibility(View.GONE);
    editPrayerText.setText(textView.getText());
    //openEditorBtn.setImageResource(R.mipmap.icon_cancel);
  }

  private void switchToReader() {
    editPrayerParent.setVisibility(View.GONE);
    textView.setVisibility(View.VISIBLE);
    textView.setText(editPrayerText.getText());
    editPrayerText.setText("");
    //openEditorBtn.setImageResource(R.mipmap.edit_icon);
  }

  @Override
  public void onStart() {
    super.onStart();
    Globals.setReadDialogOpen(true);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Globals.setReadDialogOpen(false);
  }
}
