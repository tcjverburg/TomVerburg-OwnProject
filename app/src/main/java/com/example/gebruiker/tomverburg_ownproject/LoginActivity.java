package com.example.gebruiker.tomverburg_ownproject;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

/**
 * LoginActivity.java
 * TomVerburg-OwnProject
 *
 * Starting activity in which the user has to login or create a new account using an email address.
 *
 * source:https://github.com/firebase/quickstart-android/blob/master/auth/app/src/main/java/com/google/firebase/quickstart/auth/EmailPasswordActivity.java
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private EditText mEmailField;
    private EditText mPasswordField;
    private TextView mFeedbackUserTextView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Views
        mEmailField = (EditText) findViewById(R.id.field_email);
        mPasswordField = (EditText) findViewById(R.id.field_password);
        mFeedbackUserTextView = (TextView)findViewById(R.id.user_feedback_login);

        // Buttons
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.email_create_account_button).setOnClickListener(this);

        getSupportActionBar().setTitle(R.string.app_name);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Intent getNameScreen = new Intent(getApplicationContext(), SearchActivity.class);
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createAccount(final String email, final String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!Objects.equals(password, "")&!Objects.equals(email, "")) {
            mAuth.createUserWithEmailAndPassword(email, password)

                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                mFeedbackUserTextView.setText(R.string.invalid_create_information);
                            }
                        }
                    });
        }
        else{
            mFeedbackUserTextView.setText(R.string.enter_all_fields);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!Objects.equals(password, "")&!Objects.equals(email, "")) {
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
                                mFeedbackUserTextView.setText(R.string.invalid_sign_in);
                            }
                        }
                    });
        }
        else {
            mFeedbackUserTextView.setText(R.string.invalid_sign_in);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        int i = v.getId();
        String emailText = mEmailField.getText().toString();
        if (i == R.id.email_create_account_button) {
            createAccount(emailText, mPasswordField.getText().toString());
        } else if (i == R.id.email_sign_in_button) {
            signIn(emailText, mPasswordField.getText().toString());
        }
    }
}