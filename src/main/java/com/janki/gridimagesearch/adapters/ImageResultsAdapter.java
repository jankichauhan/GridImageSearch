package com.janki.gridimagesearch.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.etsy.android.grid.util.DynamicHeightTextView;
import com.janki.gridimagesearch.R;
import com.janki.gridimagesearch.models.ImageResult;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by janki on 3/23/15.
 */
public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {


    public ImageResultsAdapter(Context context, List<ImageResult> images) {
        super(context, R.layout.item_image_result, images);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageResult result = getItem(position);

        ViewHolder viewHolder = new ViewHolder();

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, parent, false);
        }

        viewHolder.ivImage = (DynamicHeightImageView) convertView.findViewById(R.id.ivImage);
        viewHolder.tvWebsite = (DynamicHeightTextView) convertView.findViewById(R.id.tvTitle);

        viewHolder.ivImage.setImageResource(0);

        double positionHeightRatio = getImageHeightRatio(position);
        viewHolder.ivImage.setHeightRatio(positionHeightRatio);

        viewHolder.tvWebsite.setText(Html.fromHtml(result.website));
        Picasso.with(getContext()).load(result.tbUrl).into(viewHolder.ivImage);

        return convertView;
    }

    private double getImageHeightRatio(final int position) {
        ImageResult imageInfo = getItem(position);
        return (double) imageInfo.height / (double) imageInfo.width;
    }

    private static class ViewHolder {
        DynamicHeightImageView ivImage;
        DynamicHeightTextView tvTitle;
        DynamicHeightTextView tvWebsite;

    }
}
