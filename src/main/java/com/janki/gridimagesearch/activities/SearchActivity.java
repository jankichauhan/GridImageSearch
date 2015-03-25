package com.janki.gridimagesearch.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.janki.gridimagesearch.R;
import com.janki.gridimagesearch.adapters.ImageResultsAdapter;
import com.janki.gridimagesearch.listners.EndlessScrollListener;
import com.janki.gridimagesearch.models.ImageResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchActivity extends ActionBarActivity {

    private EditText etQuery;
    private GridView gdResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;
    private final int REQUEST_CODE = 20;
    private final int mMaxResults = 64;
    private final String googleAPI = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8";
    private String query;
    private String imageSize;
    private String imageColor;
    private String imageType;
    private String searchFilters = "";
    int currentPage = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();
        imageResults = new ArrayList<ImageResult>();
        aImageResults = new ImageResultsAdapter(this, imageResults);
        gdResults.setAdapter(aImageResults);

    }

    private void setupViews() {

        //etQuery = (EditText) findViewById(R.id.etQuery);
        gdResults = (GridView) findViewById(R.id.gvResults);
        gdResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //launch image activity
                // Create Intent
                Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
                //Get Image result
                ImageResult result = imageResults.get(position);
                // Pass the image result
                i.putExtra("url", result.url);
                startActivity(i);
            }
        });

        gdResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {

                if (totalItemsCount > mMaxResults - 1) {
                    return;
                }
                currentPage = (page - 1) * 8;
                getSearchResults(getSearchQuery());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //ßMenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.menu_setting_activity, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String inputQuery) {
                // perform query here
                query = inputQuery;
                newSearch();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            openSettings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openSettings() {

        Intent i = new Intent(SearchActivity.this, SettingsActivity.class);
        startActivityForResult(i, REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {

            imageSize = data.getExtras().getString("size");
            imageColor = data.getExtras().getString("color");
            imageType = data.getExtras().getString("type");

            if (imageType != null && (!imageType.equals("none"))) {
                searchFilters = searchFilters + "&imgtype=" + imageType;
            }
            if (imageColor != null && (!imageColor.equals("none"))) {
                searchFilters = searchFilters + "&imgcolor=" + imageColor;
            }
            if (imageSize != null && (!imageSize.equals("none"))) {
                searchFilters = searchFilters + "&imgsz=" + imageSize;
            }

            newSearch();
        }
    }

    public String getSearchQuery() {

        String searchUrl = googleAPI
                + "&q=" + query
                + "&start=" + currentPage
                + searchFilters;

        return searchUrl;
    }

    public void newSearch() {

//        if (!isNetworkAvailable()) {
//            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
//            return;
//        }

        Log.d("DEBUG", getSearchQuery());
        aImageResults.clear();
        getSearchResults(getSearchQuery());
    }

    public void getSearchResults(String url) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Log.d("DEBUG", response.toString());
                JSONArray imageResultsJson = null;
                try {
                    imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                    //Making change to adapter, makes change in the array. So instead of using notify change, all results to adapter
                    aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Log.d("Info", imageResults.toString());
            }
        });
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
