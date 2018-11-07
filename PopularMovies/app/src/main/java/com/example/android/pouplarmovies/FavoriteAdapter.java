package com.example.android.pouplarmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.pouplarmovies.data.FavoriteEntry;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private List<FavoriteEntry> mFavoriteEntries;
    private Context mContext;

    public FavoriteAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater
                .inflate(R.layout.favorite_item_list, viewGroup, false);

        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder favoriteViewHolder, int position) {
        FavoriteEntry favoriteEntry = mFavoriteEntries.get(position);
        String getPosterPath = favoriteEntry.getPosterPath();

        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185/" + getPosterPath)
                .into(favoriteViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        if (mFavoriteEntries == null) return 0;
        return mFavoriteEntries.size();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;

        public FavoriteViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.favorite_list_image_view);
        }
    }

    public void setFavorites(List<FavoriteEntry> favoriteEntries) {
        mFavoriteEntries = favoriteEntries;
        notifyDataSetChanged();
    }
}
