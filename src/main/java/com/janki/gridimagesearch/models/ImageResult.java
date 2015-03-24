package com.janki.gridimagesearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by janki on 3/23/15.
 */
/*
resopnse => results => [x] => url
resopnse => results => [x] => title
resopnse => results => [x] => width
resopnse => results => [x] => height


 */
public class ImageResult implements Serializable{

    public String url;
    public String title;
    public String tbUrl;
    public int height;
    public int width;

    public ImageResult(JSONObject image){

        try{

            this.url = image.getString("url");
            this.tbUrl = image.getString("tbUrl");
            this.title = image.getString("title");
            this.height = image.getInt("tbHeight");
            this.width = image.getInt("tbWidth");
        }
        catch (JSONException e) {
        }

    }

    public static ArrayList<ImageResult> fromJSONArray(JSONArray array){

        ArrayList<ImageResult> results = new ArrayList<ImageResult>();

        for(int i = 0; i < array.length(); i++)
        {
            try {
                results.add(new ImageResult(array.getJSONObject(i)));
             }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
        return  results;

    }
}
