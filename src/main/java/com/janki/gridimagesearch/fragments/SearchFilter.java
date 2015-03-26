package com.janki.gridimagesearch.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.janki.gridimagesearch.R;
import com.janki.gridimagesearch.models.Filter;

/**
 * Created by janki on 3/25/15.
 */
public class SearchFilter extends DialogFragment {

    private Spinner spSize;
    private Spinner spColor;
    private Spinner spType;

    ArrayAdapter<CharSequence> aSpSize;
    ArrayAdapter<CharSequence> aSpColor;
    ArrayAdapter<CharSequence> aSpType;

    private EditText etSiteFilter;
    String [] imgSizeArray;
    String [] imgTypeArray;

    public SearchFilter(){

    }

    public static SearchFilter newInstance(){
        SearchFilter settingsFragment = new SearchFilter();

        return settingsFragment;
    }

    private void setupViews(View view){
        spSize = (Spinner) view.findViewById(R.id.spSize);
// Create an ArrayAdapter using the string array and a default spinner layout
        aSpSize = ArrayAdapter.createFromResource(getActivity(),
                R.array.size_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        aSpSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spSize.setAdapter(aSpSize);

        spColor = (Spinner) view.findViewById(R.id.spColor);
        aSpColor = ArrayAdapter.createFromResource(getActivity(),
                R.array.color_array, android.R.layout.simple_spinner_item);
        aSpColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spColor.setAdapter(aSpColor);

        spType = (Spinner) view.findViewById(R.id.spType);
        aSpType = ArrayAdapter.createFromResource(getActivity(),
                R.array.type_array, android.R.layout.simple_spinner_item);
        aSpType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(aSpType);

        imgSizeArray = getResources().getStringArray(R.array.size_array);
        imgTypeArray = getResources().getStringArray(R.array.type_array);
    }

    public interface SearchFilterListener{
        public void onSaveButtonClick(String filters);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setTitle(R.string.title_activity_settings);
        View view = inflater.inflate(R.layout.activity_settings,null,false);
        setupViews(view);
        builder.setView(view)
                .setPositiveButton(R.string.save_label,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SearchFilterListener listener = (SearchFilterListener) getActivity();
                        listener.onSaveButtonClick(onSave());
                    }
                })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
        return builder.create();
    }

    public String onSave(){

        Filter filter = new Filter();

        filter.imgSize = imgSizeArray[spSize.getSelectedItemPosition()];
        filter.imgColor = spColor.getSelectedItem().toString();
        filter.imgType = imgTypeArray[spType.getSelectedItemPosition()];

        return filter.getFilterUrl();
    }
}
