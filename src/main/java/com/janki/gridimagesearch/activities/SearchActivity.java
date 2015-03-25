package com.janki.gridimagesearch.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
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
    String query;
    String imageSize;
    String imageColor;
    String imageType;
    String searchFilters;
    int currentPage = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchFilters = "";
        setupViews();
        imageResults = new ArrayList<ImageResult>();
        aImageResults = new ImageResultsAdapter(this, imageResults);
        gdResults.setAdapter(aImageResults);

    }

    private void setupViews() {

        etQuery = (EditText) findViewById(R.id.etQuery);
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
                customLoadMoreDataFromApi();
            }
        });

    }

    private void customLoadMoreDataFromApi() {

        query = etQuery.getText().toString();
        String searchUrl = getSearchQuery(query);
        //Adding 'start' query parameter for loading new data
        searchUrl = searchUrl + "&start=" + currentPage;
        getSearchResults(searchUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_setting_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
        }
    }

    public String getSearchQuery(String query) {

        String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + query + "&rsz=8" + searchFilters;

        return searchUrl;
    }

    public void onImageSearch(View view) {

        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        query = etQuery.getText().toString();
        //   Toast.makeText(this, "Search query " + query, Toast.LENGTH_SHORT).show();
        Log.d("DEBUG", getSearchQuery(query));
        aImageResults.clear();
        getSearchResults(getSearchQuery(query));
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
        //= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
