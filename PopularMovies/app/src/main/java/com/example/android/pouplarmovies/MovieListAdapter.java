package com.example.android.pouplarmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieListAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mPosterImage;

    public MovieListAdapter(Context context, List<String> posterImage) {
        mContext = context;
        mPosterImage = posterImage;
    }

    @Override
    public int getCount() {
        return mPosterImage.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            // Define the layout parameters
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(mContext)
                .load("http://image.tmdb.org/t/p/w185/" + mPosterImage.get(position)).into(imageView);

        return imageView;
    }
}
