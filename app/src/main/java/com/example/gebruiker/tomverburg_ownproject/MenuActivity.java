package com.example.gebruiker.tomverburg_ownproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * MenuActivity.java
 * TomVerburg-OwnProject
 *
 * Activity which shows all the options the application has to offer.
 * This is the main menu of the application.
 */

public class MenuActivity extends Activity {
    private FirebaseAuth mAuth;
    private static final String TAG = "MenuActivity";
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText editText;
    private FirebaseUser user;
    private DatabaseReference myRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        editText = (EditText)findViewById(R.id.search_article);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference(user.getUid().toString()).child("search_history");
        editText = (EditText)findViewById(R.id.search_article);

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
        if (query!="") {
            myRef.child(query).setValue(query);
            query = query.replaceAll(" ", "%20");
            getNameScreen.putExtra("query", query);
            startActivity(getNameScreen);
        }
    }

    public void favoritesButton(View v){
        Intent getNameScreen = new Intent(getApplicationContext(),FavoritesActivity.class);
        startActivity(getNameScreen);
    }

    public void historyButton(View v){
        Intent getNameScreen = new Intent(getApplicationContext(),HistoryActivity.class);
        getNameScreen.putExtra("userUid", user.getUid());
        startActivity(getNameScreen);
    }

    public void allUsersButton(View v){
        Intent getNameScreen = new Intent(getApplicationContext(),OtherUsersActivity.class);
        startActivity(getNameScreen);
    }


}