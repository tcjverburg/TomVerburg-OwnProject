package com.example.gebruiker.tomverburg_ownproject;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


/**
 * SearchActivity.java
 * TomVerburg-OwnProject
 *
 * In the activity the user can enter a search term and look for articles
 * related to this specific search term. This is done using an HTTP request
 * and the live api of The Guardian in the SearchResultActivity with the help of MyAsyncTask.
 */

public class SearchActivity extends BaseActivity implements View.OnClickListener  {
    private EditText editText;
    private FirebaseUser user;
    private DatabaseReference myRefHistory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        editText = (EditText)findViewById(R.id.search_article);
        findViewById(R.id.search_article_button).setOnClickListener(this);
        findViewById(R.id.search_nav).setOnClickListener(this);
        findViewById(R.id.history_nav).setOnClickListener(this);
        findViewById(R.id.favorites_nav).setOnClickListener(this);

        Button Nav = (Button)findViewById(R.id.search_nav);
        int myColor = getResources().getColor(R.color.colorButtonPressed);
        Nav.setBackgroundColor(myColor);
        user = getUser();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRefHistory = database.getReference("users").child(user.getUid()).child("search_history");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.search_article_button) {
            Intent getNameScreen = new Intent(getApplicationContext(),SearchResultActivity.class);
            String query= String.valueOf(editText.getText());
            if (!Objects.equals(query, "")) {
                myRefHistory.child(query).setValue(query);
                query = query.replaceAll(" ", "%20");
                getNameScreen.putExtra("query", query);
                startActivity(getNameScreen);
            }
        }
        else if (i == R.id.history_nav) {
            String userName = getUserName();
            Intent getNameScreen = new Intent(getApplicationContext(),HistoryActivity.class);
            getNameScreen.putExtra("userUid", user.getUid());
            getNameScreen.putExtra("userName", userName);
            startActivity(getNameScreen);
            finish();
        }
        else if (i == R.id.favorites_nav) {
            Intent getNameScreen = new Intent(getApplicationContext(),FavoritesActivity.class);
            startActivity(getNameScreen);
            finish();
        }


    }
}