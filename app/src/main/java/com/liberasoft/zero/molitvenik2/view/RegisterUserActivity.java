package com.liberasoft.zero.molitvenik2.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.liberasoft.zero.molitvenik2.R;
import com.liberasoft.zero.molitvenik2.db.DatabaseRegistrationListener;
import com.liberasoft.zero.molitvenik2.model.UserModel;
import com.liberasoft.zero.molitvenik2.utils.App;
import com.liberasoft.zero.molitvenik2.utils.Globals;
import com.liberasoft.zero.molitvenik2.utils.TimeStamp;

import java.util.Objects;

import javax.inject.Inject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.liberasoft.zero.molitvenik2.utils.constants.DatabaseReference.REF_USER;
import static com.liberasoft.zero.molitvenik2.utils.constants.SharedPreferenceReference.REF_MY_USERNAME;

public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener {

  private EditText usernameEditText;

  // Internal storage of a username
  @Inject
  SharedPreferences sp;
  @Inject
  DatabaseReference databaseReference;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register_user);
    ((App) Objects.requireNonNull(this).getApplication()).getDependencyComponent().inject(this);

    // Inspecting if user already has stored username (is already registered)
    toSkipRegisterActivity();

    usernameEditText = findViewById(R.id.username_edit_text);
    Button registerUserBtn = findViewById(R.id.register_user_btn);
    registerUserBtn.setOnClickListener(this);
  }

  private void toSkipRegisterActivity() {
    // If user has already registered a game and it's not the first time
    // he is opening an app.
    if (hasUsername()) {
      // Proceding to main activity
      startMainActivity();
      // Setting username to static variable so I don't have to fetch username
      // from SharedPreference throughout app.
      setUsernameToGlobal(retrieveusernameinternally());
      // Closing activity
      finish();
    }
  }

  private boolean hasUsername() {
    return !sp.getString(REF_MY_USERNAME, "").equals("");
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.register_user_btn:
        if (isNameCorrect(usernameEditText)) {
          new DatabaseRegistrationListener(this, this).retrieveData(usernameEditText);
        } else {
          Toast.makeText(this, getResources().getString(R.string.username_warning), Toast.LENGTH_SHORT)
                  .show();
        }
        break;
    }
  }

  public boolean doesUsernameExists(EditText usernameEditText, String listName) {
    return usernameEditText.getText().toString().equals(listName);
  }

  public void startMainActivity() {
    startActivity(appIntent());
  }

  public void storeUserFirebase(EditText usernameEditText) {
    UserModel userModel = new UserModel(usernameEditText.getText().toString(), TimeStamp.getDate());
    databaseReference.child(REF_USER).child(usernameEditText.getText().toString()).setValue(userModel);
  }

  public SharedPreferences.Editor storeUsernameInternally(EditText usernameEditText) {
    return sp.edit().putString(REF_MY_USERNAME, usernameEditText.getText().toString());
  }

  private String retrieveusernameinternally() {
    return sp.getString(REF_MY_USERNAME, "");
  }

  // Sending user to a main activity
  private Intent appIntent() {
    return new Intent(getApplicationContext(), MainActivity.class);
  }

  // Checking if user name meets the conditions
  private boolean isNameCorrect(EditText usernameEditText) {
    return usernameEditText.getText().length() > 4;
  }

  // Adding custom fonts to this activity
  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  public <T> void setUsernameToGlobal(T t) {
    Globals.setUsername(t.toString());
  }
}
