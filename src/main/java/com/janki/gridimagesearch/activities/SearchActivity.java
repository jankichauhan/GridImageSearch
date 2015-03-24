package com.janki.gridimagesearch.activities;

import android.content.Intent;
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

import com.janki.gridimagesearch.R;
import com.janki.gridimagesearch.adapters.ImageResultsAdapter;
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

    String imageSize;
    String imageColor;
    String imageType;

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
        }
    }

    public void onImageSearch(View view) {

        String query = etQuery.getText().toString();
        //   Toast.makeText(this, "Search query " + query, Toast.LENGTH_SHORT).show();
        AsyncHttpClient client = new AsyncHttpClient();
        //https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=android&rsz=8
        String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + query + "&rsz=8";
        if(imageType != null && (!imageType.equals("none")))
        {
            searchUrl = searchUrl + "&imgtype=" + imageType;
        }
        if(imageColor != null && (!imageColor.equals("none")))
        {
            searchUrl = searchUrl + "&imgcolor=" + imageColor;
        }
        if(imageSize != null && (!imageSize.equals("none")))
        {
            searchUrl = searchUrl + "&imgsz=" + imageSize;
        }

        Log.d("DEBUG", searchUrl);

        client.get(searchUrl, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
               // Log.d("DEBUG", response.toString());
                JSONArray imageResultsJson = null;
                try {
                    imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                    imageResults.clear();
                    //Making change to adapter, makes change in the array. So instead of using notify change, all results to adapter
                    aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

               // Log.d("Info", imageResults.toString());
            }
        });
    }
}
