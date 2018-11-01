package com.example.android.pouplarmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.pouplarmovies.data.SaveMovieData;
import com.example.android.pouplarmovies.utilities.ParseJsonDataUtils;

public class MovieActivityFragment extends Fragment {

    OnMovieClickListener mCallback;

    // This interface will be called from main activity
    public interface OnMovieClickListener {
        void onMovieSelected(int position);
    }

    // Empty constructor for the fragment
    public MovieActivityFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnMovieClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " should implement OnMovieClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_main_activity, container, false);

        GridView gridView = rootView.findViewById(R.id.gridview_main_fragment);

        MovieListAdapter listAdapter = new MovieListAdapter(getContext(), SaveMovieData.getPosterImage());

        gridView.setAdapter(listAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCallback.onMovieSelected(position);
            }
        });

        return rootView;
    }
}
