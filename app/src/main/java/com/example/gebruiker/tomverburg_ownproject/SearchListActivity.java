package com.example.gebruiker.tomverburg_ownproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * SearchListActivity.java
 * TomVerburg-OwnProject
 *
 * Activity which shows all the result of the search of the user. By clicking
 * you are able to read the selected article. By long clicking you save the article
 * to your favorites.
 */

public class SearchListActivity extends Activity  {
    private ArrayList<String> articles = new ArrayList<>();
    private ArrayList<String> urls = new ArrayList<>();
    private ArrayAdapter theAdapter;
    private ListView theListView;
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        Intent activityThatCalled = getIntent();
        String query = activityThatCalled.getExtras().getString("query");
        url = "https://content.guardianapis.com/search?q=" + query + "&api-key=828ceb77-f98a-4d04-9912-9a626d996386";

        MyAsyncTask task = new MyAsyncTask(new MyAsyncTask.TaskListener() {
            @Override
            public void onFinished(String result) {
                String[] parts = result.split("\"results\":");
                String json = parts[1];
                try {
                    JSONArray obj = new JSONArray(json);
                    for (int i = 0; i < obj.length(); i++) {
                        JSONObject c = obj.getJSONObject(i);
                        String title = c.getString("webTitle");
                        String url = c.getString("webUrl");
                        articles.add(title);
                        urls.add(url);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter();
            }
        });

        task.execute(url);

        theListView = (ListView) findViewById(R.id.searchListView);

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                goToUrl(urls.get(position));
            }
        });

        theListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                String title = String.valueOf(adapterView.getItemAtPosition(position));
                String url = urls.get(position);
                persistence(title, url);
                return true;
            }
        });
    }

    public void adapter() {
        theAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, articles);
        theListView.setAdapter(theAdapter);
    }

    private void goToUrl(String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    public void persistence(String title, String url) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(title, url);
        editor.commit();
        Toast.makeText(SearchListActivity.this, "You have added " + title + " to your favorites!", Toast.LENGTH_SHORT).show();
    }

}



