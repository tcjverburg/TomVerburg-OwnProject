package com.example.gebruiker.tomverburg_ownproject;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


/**
 * MenuActivity.java
 * TomVerburg-OwnProject
 *
 * Activity which shows all the options the application has to offer.
 * This is the main menu of the application.
 */

public class MenuActivity extends BaseActivity{
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
        myRef = database.getReference("users").child(user.getUid().toString()).child("search_history");
        getSupportActionBar().setTitle("Hello world App");



    }





    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void searchButton(View v){
        Intent getNameScreen = new Intent(getApplicationContext(),SearchListActivity.class);
        String query= String.valueOf(editText.getText());
        if (!Objects.equals(query, "")) {
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