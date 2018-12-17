package com.liberasoft.zero.molitvenik2.db;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.liberasoft.zero.molitvenik2.R;
import com.liberasoft.zero.molitvenik2.model.UserModel;
import com.liberasoft.zero.molitvenik2.utils.App;
import com.liberasoft.zero.molitvenik2.view.RegisterUserActivity;

import java.util.Objects;

import javax.inject.Inject;

import static com.liberasoft.zero.molitvenik2.utils.constants.DatabaseReference.REF_USER;

public class DatabaseRegistrationListener implements IDatabase {

  @Inject
  DatabaseReference databaseReference;

  private Activity activity;
  private RegisterUserActivity registerUserActivity;

  public DatabaseRegistrationListener(Activity activity, RegisterUserActivity registerUserActivity) {
    this.activity = activity;
    this.registerUserActivity = registerUserActivity;
    ((App) Objects.requireNonNull(activity).getApplication()).getDependencyComponent().inject(this);
  }

  @Override
  public void retrieveData(Object o) {
    databaseReference.child(REF_USER).addListenerForSingleValueEvent(getListener(o));
  }

  @Override
  public ValueEventListener getListener(Object o) {
    return new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        boolean nameExists = false;

        for (DataSnapshot s : dataSnapshot.getChildren()) {
          UserModel userModel = s.getValue(UserModel.class);

          if (registerUserActivity.doesUsernameExists((EditText) o, userModel.getUsername())) {
            nameExists = true;
          }
        }
        if (!nameExists) {
          registerUserActivity.startMainActivity();
          registerUserActivity.storeUsernameInternally((EditText) o).apply();
          registerUserActivity.storeUserFirebase((EditText) o);
          registerUserActivity.setUsernameToGlobal(((EditText) o).getText());
          activity.finish();
        } else {
          Toast.makeText(activity, activity.getResources().getString(R.string.name_taken_warning), Toast.LENGTH_SHORT).show();
        }

      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    };
  }


}
