package com.example.suyash.uberflicker.view;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.suyash.uberflicker.R;
import com.example.suyash.uberflicker.core.ISearchImages;
import com.example.suyash.uberflicker.core.net.HttpManager;
import com.example.suyash.uberflicker.core.net.SearchPhotosTask;
import com.example.suyash.uberflicker.model.FlickrImage;
import com.example.suyash.uberflicker.model.SearchedImages;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ISearchImages, View.OnClickListener{

    private static final int DEFAULT_PAGE = 1;
    private static final int GRID_SPAN_COUNT = 3;

    private SearchedImagesAdapter searchedImagesAdapter;
    private List<FlickrImage> imageList = new LinkedList<>();

    private GridLayoutManager gridLayoutManager;
    private ProgressBar progressBar;
    private int pastVisibleItems, visibleItemCount, totalItemCount;

    private boolean isSearchInProgress;
    private int pageNo = DEFAULT_PAGE;

    private EditText inputEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = this.findViewById(R.id.mSearchResults);
        searchedImagesAdapter = new SearchedImagesAdapter(this, imageList);
        recyclerView.setAdapter(searchedImagesAdapter);

        gridLayoutManager = new GridLayoutManager(this, GRID_SPAN_COUNT, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addOnScrollListener(getOnScrollListener());

        inputEditText = this.findViewById(R.id.mSearchText);
        inputEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    handleSearchClick();
                    return true;
                }
                return false;
            }
        });
        progressBar = this.findViewById(R.id.mProgressBar);

        ImageView searchView = this.findViewById(R.id.mSearchClick);
        searchView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mSearchClick:
                handleSearchClick();
                break;
        }
    }

    private void handleSearchClick() {
        imageList.clear();
        searchedImagesAdapter.notifyDataSetChanged();
        pageNo = DEFAULT_PAGE;
        fetchImages(inputEditText.getText().toString(), pageNo);
    }

    private RecyclerView.OnScrollListener getOnScrollListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = gridLayoutManager.getChildCount();
                    totalItemCount = gridLayoutManager.getItemCount();
                    pastVisibleItems = gridLayoutManager.findFirstVisibleItemPosition();

                    if (!isSearchInProgress) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            fetchImages(inputEditText.getText().toString(), pageNo++);
                        }
                    }
                }
            }
        };
    }

    private void fetchImages(String searchText, int pageNo) {
        if(TextUtils.isEmpty(searchText))
            return;

        isSearchInProgress = true;

        progressBar.setVisibility(View.VISIBLE);
        SearchPhotosTask searchPhotosTask = new SearchPhotosTask(this, searchText, pageNo);
        HttpManager.getInstance().executeTask(searchPhotosTask);
    }

    @Override
    public void onTaskComplete(SearchedImages searchedImages) {
        isSearchInProgress = false;
        if (searchedImages != null) {
            imageList.addAll(searchedImages.getFlickrImageList());
            Handler mainHandler = new Handler(MainActivity.this.getMainLooper());

            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.INVISIBLE);
                    searchedImagesAdapter.notifyDataSetChanged();
                }
            };
            mainHandler.post(myRunnable);
        }
    }
}
