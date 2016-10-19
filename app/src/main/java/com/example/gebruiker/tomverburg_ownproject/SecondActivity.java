package com.example.gebruiker.tomverburg_ownproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * Created by Gebruiker on 19-10-2016.
 */

public class SecondActivity extends AppCompatActivity {
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private static final String TAG = "SecondActivity";
    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView textView;
    private EditText editText;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        textView = (TextView) findViewById(R.id.article);
        editText = (EditText)findViewById(R.id.search_article);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        editText = (EditText)findViewById(R.id.search_article);

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());


                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    textView.setText("niet ingelogd");
                    Intent getNameScreen = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(getNameScreen);
                }
                // [START_EXCLUDE]
                // [END_EXCLUDE]
            }
        };


    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    private void signOut() {
        mAuth.signOut();
    }

    public void signOutButton(View v) {
        signOut();
    }
    public void searchButton(View v){
        Intent getNameScreen = new Intent(getApplicationContext(),SearchListActivity.class);
        String query= String.valueOf(editText.getText());
        query.replace(" ", "");
        getNameScreen.putExtra("query", query);
        startActivity(getNameScreen);

    }
}