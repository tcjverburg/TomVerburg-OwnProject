package com.example.gebruiker.tomverburg_ownproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by Gebruiker on 19-10-2016.
 */

public class SecondActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "SecondActivity";
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText editText;
    private FirebaseUser user;
    private DatabaseReference myRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        editText = (EditText)findViewById(R.id.search_article);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference(user.getUid().toString()).child("search_history");

        // [END initialize_auth]
        editText = (EditText)findViewById(R.id.search_article);

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());


                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Intent getNameScreen = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(getNameScreen);
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

    private void signOut() {
        mAuth.signOut();
    }

    public void signOutButton(View v) {
        signOut();
        Intent getNameScreen = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(getNameScreen);
    }

    public void searchButton(View v){
        Intent getNameScreen = new Intent(getApplicationContext(),SearchListActivity.class);
        String query= String.valueOf(editText.getText());
        myRef.child(query).setValue(query);
        query = query.replaceAll(" ", "%20");
        getNameScreen.putExtra("query", query);
        startActivity(getNameScreen);
    }

    public void favoritesButton(View v){
        Intent getNameScreen = new Intent(getApplicationContext(),Favorites.class);
        startActivity(getNameScreen);
    }

    public void historyButton(View v){
        Intent getNameScreen = new Intent(getApplicationContext(),History.class);
        startActivity(getNameScreen);
    }
}