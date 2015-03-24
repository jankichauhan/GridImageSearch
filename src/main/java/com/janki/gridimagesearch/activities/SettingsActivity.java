package com.janki.gridimagesearch.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import com.janki.gridimagesearch.R;

public class SettingsActivity extends Activity implements OnItemSelectedListener{

    Spinner spSize;
    Spinner spColor;
    Spinner spType;

    ArrayAdapter<CharSequence> aSpSize;
    ArrayAdapter<CharSequence> aSpColor;
    ArrayAdapter<CharSequence> aSpType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        spSize = (Spinner) findViewById(R.id.spSize);
// Create an ArrayAdapter using the string array and a default spinner layout
        aSpSize = ArrayAdapter.createFromResource(this,
                R.array.size_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        aSpSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spSize.setAdapter(aSpSize);

        spColor = (Spinner) findViewById(R.id.spColor);
        aSpColor = ArrayAdapter.createFromResource(this,
                R.array.color_array, android.R.layout.simple_spinner_item);
        aSpColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spColor.setAdapter(aSpColor);

        spType = (Spinner) findViewById(R.id.spType);
        aSpType = ArrayAdapter.createFromResource(this,
                R.array.type_array, android.R.layout.simple_spinner_item);
        aSpType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(aSpType);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
