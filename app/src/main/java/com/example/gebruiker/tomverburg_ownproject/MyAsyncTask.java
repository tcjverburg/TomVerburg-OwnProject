package com.example.gebruiker.tomverburg_ownproject;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * MyAsyncTask.java
 * TomVerburg-OwnProject
 *
 * In this class the HTTP request is performed on a separate thread,
 * parallel to the main thread in SearchResultActivity. It also contains a
 * listener that checks whether the task is complete and which then calls a
 * callback function on it, so that the UI in the SearchActivity activity
 * is updated.
 **/
//source: https://www.youtube.com/watch?v=Gyaay7OTy-w
class MyAsyncTask extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        //Tries to make connection if possible
        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder buffer = new StringBuilder();
            String line;

            //Adds line by line to the buffer from the api
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            //Returns the entire buffer as string.
            return buffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //Source:http://stackoverflow.com/questions/26202568/android-pass-function-reference-to-asynctask
    //Listener for the task to be finished.
    interface TaskListener {
        void onFinished(String result);
    }

    // This is the reference to the associated listener
    private final TaskListener taskListener;

    MyAsyncTask(TaskListener listener) {
        // The listener reference is passed in through the constructor
        this.taskListener = listener;
    }

    //filters the string we got as a result of the method getting the information from the api
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // In onPostExecute we check if the listener is valid
        if(this.taskListener != null) {
            // And if it is we call the callback function on it.
            this.taskListener.onFinished(result);
        }
    }
}
