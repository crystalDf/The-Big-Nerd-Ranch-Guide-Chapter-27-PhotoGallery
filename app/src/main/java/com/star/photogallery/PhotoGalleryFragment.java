package com.star.photogallery;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.io.IOException;
import java.util.ArrayList;

public class PhotoGalleryFragment extends Fragment {

    private static final String TAG = "PhotoGalleryFragment";

    private GridView mGridView;
    private ArrayList<GalleryItem> mItems;

    private int mCurrentPage = 1;
    private int mFetchedPage = 0;
    private int mCurrentPosition = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        new FetchItemsTask().execute(mCurrentPage);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        mGridView = (GridView) v.findViewById(R.id.gridView);

        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (((firstVisibleItem + visibleItemCount) == totalItemCount) &&
                        (mCurrentPage == mFetchedPage)) {
                    mCurrentPosition = firstVisibleItem + 3;
                    mCurrentPage++;
                    new FetchItemsTask().execute(mCurrentPage);
                }
            }
        });

        setupAdapter();

        return v;
    }

    private class FetchItemsTask extends AsyncTask<Integer, Void, ArrayList<GalleryItem>> {

        @Override
        protected ArrayList<GalleryItem> doInBackground(Integer... params) {
//            try {
//                String result = new FlickrFetchr().getUrl("https://www.google.com");
//                Log.i(TAG, "Fetch contents of URL: " + result);
//            } catch (IOException e) {
//                Log.e(TAG, "Failed to fetch URL: ", e);
//                e.printStackTrace();
//            }
//            new FlickrFetchr().fetchItems();
//
//            return null;

            return new FlickrFetchr().fetchItems(params[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<GalleryItem> items) {
            if (mItems == null) {
                mItems = items;
            } else {
                mItems.addAll(items);
            }

            mFetchedPage++;

            setupAdapter();
        }
    }

    private void setupAdapter() {
        if ((getActivity() != null) && (mGridView != null)) {
            if (mItems != null) {
                mGridView.setAdapter(new ArrayAdapter<GalleryItem>(
                        getActivity(), android.R.layout.simple_gallery_item, mItems));
            } else {
                mGridView.setAdapter(null);
            }
            mGridView.setSelection(mCurrentPosition);
        }
    }
}
