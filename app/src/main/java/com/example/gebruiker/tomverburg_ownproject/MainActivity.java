package com.example.gebruiker.tomverburg_ownproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * MainActivity.java
 * TomVerburg-OwnProject
 *
 * Starting activity in which the user has to login or create a new account.
 *
 * source:https://github.com/firebase/quickstart-android/blob/master/auth/app/src/main/java/com/
 * oogle/firebase/quickstart/auth/EmailPasswordActivity.java
 *
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private EditText mEmailField;
    private EditText mPasswordField;
    private FirebaseUser user;
    private DatabaseReference myRef;
    private String emailText;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Views

        mEmailField = (EditText) findViewById(R.id.field_email);
        mPasswordField = (EditText) findViewById(R.id.field_password);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("email");

        // Buttons
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.email_create_account_button).setOnClickListener(this);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    String name = user.getEmail().split("@")[0].replaceAll("^[^a-zA-Z0-9\\s]+|[^a-zA-Z0-9\\s]+$", "");;
                    myRef.child(name).setValue(user.getUid());
                    Intent getNameScreen = new Intent(getApplicationContext(), MenuActivity.class);
                    startActivity(getNameScreen);
                    finish();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void createAccount(final String email, final String password) {
        Log.d(TAG, "createAccount:" + email);

        mAuth.createUserWithEmailAndPassword(email, password)

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());


                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, R.string.valid_information,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                });
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(MainActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }

                        if (!task.isSuccessful()) {

                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        emailText = mEmailField.getText().toString();
        if (i == R.id.email_create_account_button) {
            createAccount(emailText, mPasswordField.getText().toString());
        } else if (i == R.id.email_sign_in_button) {
            signIn(emailText, mPasswordField.getText().toString());
        }
    }
}