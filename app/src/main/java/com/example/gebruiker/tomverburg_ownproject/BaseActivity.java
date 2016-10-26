package com.example.gebruiker.tomverburg_ownproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * BaseActivity.java
 * TomVerburg-OwnProject
 *
 * Base activity which contains a listener which tracks whether a user is still logged in
 * and returns the application to the LoginActivity if this is not the case. This class also
 * contains various methods which are used more than once through different activities.
 * BaseActivity.java is also the superclass of SearchActivity, HistoryActivity and
 * FavoritesActivity. This also makes it possible to efficiently create a horizontal navigation
 * bar for these classes.
 *
 */

public class BaseActivity extends AppCompatActivity   {
    private FirebaseAuth mAuth;
    private static final String TAG = "Base";
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private String userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefUsers = database.getReference("email");

        userName = user.getEmail().split("@")[0].replaceAll("^[^a-zA-Z0-9\\s]+|[^a-zA-Z0-9\\s]+$", "");
        myRefUsers.child(user.getUid()).setValue(userName);

        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                }
                else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Intent getNameScreen = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(getNameScreen);
                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportActionBar().setTitle(R.string.app_name);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case R.id.sign_out:
            signOut();
            return(true);
    }
        return(super.onOptionsItemSelected(item));
    }

    private void signOut() {
        mAuth.signOut();
        Intent getNameScreen = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(getNameScreen);
        user = null;
    }

    public ListAdapter adapter(ArrayList<String> historyTitles){
        return new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, historyTitles);
    }

    public void goToUrl(String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
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

    public String getUserName(){
        return userName;
    }
    public FirebaseUser getUser(){
        return user;
    }


}