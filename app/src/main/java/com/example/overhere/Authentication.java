package com.example.overhere;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.Arrays;
import java.util.List;

public class Authentication extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    // Instance for auth
    FirebaseUser user;

    SharedPreferences.Editor prefs;

    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        prefs = getApplicationContext().getSharedPreferences("Data Pref", MODE_PRIVATE).edit();

        user = FirebaseAuth.getInstance().getCurrentUser();
        // checking if user already signed in
        if (user != null) {
            Toast.makeText(Authentication.this, "User signed in", Toast.LENGTH_SHORT).show();
            prefs.putString("User", user.getEmail().toString()).apply();
        } else {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);

        }



    }

    // [START auth_fui_result]
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                user = FirebaseAuth.getInstance().getCurrentUser();
                prefs.putString("User", user.getEmail().toString()).apply();
                // ...
            } else {
                Toast.makeText(Authentication.this, "Sign in failed. Please check your email and password.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //When the button clicked, go to save device activity
    public void saveDevice(View view){
        Button saveDeice = findViewById(R.id.saveDevice);
        Intent saveIntent = new Intent(this, SaveDevice.class);
        startActivity(saveIntent);
    }

    // When the button clicked , go to the find device activity
    public void findDevice(View view) {
        // Go to the find device activity
        Button findDevice = findViewById(R.id.findDevice);
        Intent findIntent = new Intent(this, FindDevice.class);
        startActivity(findIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }



    public void signOut(View view) {
        // [START auth_fui_signout]
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Authentication.this, "User signed out", Toast.LENGTH_SHORT).show();
                    finish();
                    }
                });
        // [END auth_fui_signout]
    }
}
