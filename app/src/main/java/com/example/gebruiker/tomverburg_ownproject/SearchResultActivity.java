package com.example.gebruiker.tomverburg_ownproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * SearchResultActivity.java
 * TomVerburg-OwnProject
 *
 * Activity which shows all the result of the search of the user. By clicking
 * you are able to read the selected article. By long clicking you save the article
 * to your favorites.
 */

public class SearchResultActivity extends BaseActivity  {
    private ArrayList<String> articles = new ArrayList<>();
    private ArrayList<String> urls = new ArrayList<>();
    private ListView theListView;
    private ListAdapter theAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        //Get search term from previous activity.
        Intent activityThatCalled = getIntent();
        String search = activityThatCalled.getExtras().getString("search");

        //Construct the url and set up listview.
        String url = "https://content.guardianapis.com/search?q=" + search + "&api-key=828ceb77-f98a-4d04-9912-9a626d996386";
        theListView = (ListView) findViewById(R.id.searchListView);

        MyAsyncTask task = new MyAsyncTask(new MyAsyncTask.TaskListener() {
            //After the listener of the MyAsyncTask is fired, the JSON result is then
            // split and entered in the array lists articles and urls. The array of articles
            //are then entered into the list view.
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
                theAdapter =  adapter(articles);
                theListView.setAdapter(theAdapter);
            }
        });

        task.execute(url);
        clickSelectArticle();
        clickAddToFavorites();
    }

    public void clickSelectArticle(){
        //Opens url of the selected article.
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                goToUrl(urls.get(position));
            }
        });
    }

    public void clickAddToFavorites(){
        //Adds the selected article to the users favorites after long clicking it.
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

    public void persistence(String title, String url) {
        //Saves the title as key and url as value in the specific shared preferences of the
        //current user and gives the user feedback.
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref" + getUser().getUid(), 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(title, url);
        editor.apply();
        Toast.makeText(SearchResultActivity.this, "\"" + title + "\" " + getString(R.string.added_to_favorites)  , Toast.LENGTH_SHORT).show();
    }

}



