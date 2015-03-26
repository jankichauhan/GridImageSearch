package com.janki.gridimagesearch.models;

/**
 * Created by janki on 3/25/15.
 */
public class Filter {

    public String imgSize;
    public String imgColor;
    public String imgType;
    public String siteFilter;

    public Filter(){
        imgSize = "any";
        imgColor = "any";
        imgType = "any";
        siteFilter = "";

    }


    public String getFilterUrl(){
        StringBuilder filterUrl = new StringBuilder("");

        if (!imgSize.equals("any")){
            filterUrl.append("&imgsz="+imgSize);
        }
        if (!imgColor.equals("any")){
            filterUrl.append("&imgcolor="+imgColor);
        }
        if (!imgType.equals("any")){
            filterUrl.append("&imgtype="+imgType);
        }


        return filterUrl.toString();
    }
    public void setImgSize(int selectedPosition){

    }
}
