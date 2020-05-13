package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    public static final String LOG_TAG = NewsActivity.class.getName();
    private NewsAdapter mAdapter;
    private static final int NEWS_LOADER_ID = 1;
    private String news_url = "https://content.guardianapis.com/search";
    private TextView empty, no_connection;
    private ProgressBar spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        // Reference to the textvies in NewsActivity
        empty = (TextView) findViewById(R.id.empty);
        spin = (ProgressBar) findViewById(R.id.loading_spinner);
        no_connection = (TextView) findViewById(R.id.no_connection);

        // Find a reference to the {@link ListView} in the layout
        ListView NewsListView = (ListView) findViewById(R.id.list);

        //when there are no news to be loaded
        NewsListView.setEmptyView(empty);

        //Where there is no internet connection
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            spin.setVisibility(View.GONE);
            no_connection.setText(R.string.noConnection);
            NewsListView.setEmptyView(no_connection);
        } else {
            // Create a new {@link ArrayAdapter} of news
            mAdapter = new NewsAdapter(
                    this, new ArrayList<News>());

            // Set the adapter on the {@link ListView}
            // so the list can be populated in the user interface
            NewsListView.setAdapter(mAdapter);

            NewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    News obj = (News) adapterView.getItemAtPosition(i);
                    Uri webpage = Uri.parse(obj.getUrl());
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                    startActivity(webIntent);
                }
            });
            //Initializing the loader
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        }

    }

    //Loader callback methods
    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String sectionPref = sharedPrefs.getString("football", "football");
        Log.e("sujata", sectionPref);
        String datePref = sharedPrefs.getString("start_date", "2018-10-10");
        Log.e("sujata_date", datePref);

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(news_url);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value. For example, the `format=geojson`
        uriBuilder.appendQueryParameter("section", sectionPref);
        uriBuilder.appendQueryParameter("from-date", datePref);
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("q", "football");
        uriBuilder.appendQueryParameter("api-key", "751564f2-e3e0-40f4-90dd-11ad35f6e733");
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        mAdapter.clear();
        spin.setVisibility(View.GONE);
        empty.setText(R.string.emptyViewText);
        if ((news != null) && !(news.isEmpty())) {
            mAdapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();
    }

    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


