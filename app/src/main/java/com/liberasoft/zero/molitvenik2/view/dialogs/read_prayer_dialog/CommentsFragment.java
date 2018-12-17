package com.liberasoft.zero.molitvenik2.view.dialogs.read_prayer_dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.liberasoft.zero.molitvenik2.R;
import com.liberasoft.zero.molitvenik2.db.DatabaseCommentListener;
import com.liberasoft.zero.molitvenik2.db.IDatabase;
import com.liberasoft.zero.molitvenik2.model.ReadingCommentsModel;
import com.liberasoft.zero.molitvenik2.utils.App;
import com.liberasoft.zero.molitvenik2.utils.FragmentTransition;
import com.liberasoft.zero.molitvenik2.utils.Globals;
import com.liberasoft.zero.molitvenik2.utils.SoftKeyboard;
import com.liberasoft.zero.molitvenik2.utils.TimeStamp;
import com.liberasoft.zero.molitvenik2.utils.constants.ViewReference;

import java.util.Objects;

import javax.inject.Inject;

import static com.liberasoft.zero.molitvenik2.utils.constants.DatabaseReference.REF_COMMENTS_ON_PRAYERS;
import static com.liberasoft.zero.molitvenik2.utils.constants.PassingArgumentsReference.REF_ARGUMENT_ID;

@SuppressLint("ValidFragment")
public class CommentsFragment extends Fragment implements View.OnClickListener {

  @Inject
  DatabaseReference databaseReference;
  // To run fragment from fragment
  private FragmentManager fm;

  public CommentsFragment(FragmentManager fm) {
    this.fm = fm;
  }

  // Passing prayed ID that is unique UUID sent to database when the user wrote a prayed and
  // posted it to a database.
  String name;
  String id;
  String date;
  String text;
  // Will show comments loaded from a database
  private RecyclerView commentsRecyclerView;
  private EditText commentInputEditText;
  private ImageButton addBtn, cancelBtn;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_comments, container, false);

    ((App) Objects.requireNonNull(getActivity()).getApplication()).getDependencyComponent().inject(this);

    // Load passing argument into current fragment
    id = getArguments().getString(REF_ARGUMENT_ID);
    //Initial view load
    loadViews(view);
    // Initializing view listeners
    viewListeners();
    // Loading items into a recycler view
    commentsListener();

    return view;
  }

  private void commentsListener() {
    IDatabase commentsListener = new DatabaseCommentListener(getActivity(), id);
    commentsListener.retrieveData(commentsRecyclerView);
  }

  private void loadViews(View view) {
    commentsRecyclerView = view.findViewById(R.id.read_comments_prayers_dialog_recycler_view);
    commentInputEditText = view.findViewById(R.id.comment_dialog_input_edit_text);
    addBtn = view.findViewById(R.id.comment_dialog_add_btn);
    cancelBtn = view.findViewById(R.id.comment_dialog_cancel_btn);
  }

  private void viewListeners() {
    addBtn.setOnClickListener(this);
    cancelBtn.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.comment_dialog_add_btn:
        sendComment();
        SoftKeyboard.close(Objects.requireNonNull(getActivity()), commentInputEditText);
        resetCommentInput();
        break;
      case R.id.comment_dialog_cancel_btn:
        returnPreviousFragment();
        break;
    }
  }

  private void returnPreviousFragment() {
    PrayingFragment prayingFragment = new PrayingFragment(fm);
    prayingFragment.setArguments(getArguments());
    // Returning to previous fragment with same arguments data.
    FragmentTransition.createTransition(this.fm, R.id.reading_dialog_fragment_container, prayingFragment, ViewReference.REF_READING_DIALOG_PRAYER_FRAGMENT).commit();

  }

  private void sendComment() {
    if (isChatMessageValid()) {
      databaseReference.child(REF_COMMENTS_ON_PRAYERS).child(id).push().setValue(new ReadingCommentsModel(Globals.getUsername(), TimeStamp.getDate(), getChatMessage()));
    } else {
      Toast.makeText(getContext(), getResources().getString(R.string.chat_message_warning), Toast.LENGTH_SHORT).show();
    }
  }

  private void resetCommentInput() {
    commentInputEditText.setText("");
  }

  private String getChatMessage() {
    return commentInputEditText.getText().toString();
  }

  private boolean isChatMessageValid() {
    return getChatMessage().length() > 1;
  }
}
