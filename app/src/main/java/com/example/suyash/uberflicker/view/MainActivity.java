package com.example.suyash.uberflicker.view;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.suyash.uberflicker.R;
import com.example.suyash.uberflicker.model.FlickrImage;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SearchedImagesAdapter searchedImagesAdapter;
    private List<FlickrImage> imageList = new LinkedList<>();
    private GridLayoutManager gridLayoutManager;
    private EditText inputEditText;

    private static final int GRID_SPAN_COUNT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = this.findViewById(R.id.mSearchResults);
        searchedImagesAdapter = new SearchedImagesAdapter(this, imageList);
        recyclerView.setAdapter(searchedImagesAdapter);

        gridLayoutManager = new GridLayoutManager(this, GRID_SPAN_COUNT,
                StaggeredGridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            }
        });
        inputEditText = this.findViewById(R.id.mSearchText);
        inputEditText.addTextChangedListener(mTextChangeListener);

        ImageView searchView = this.findViewById(R.id.mSearchClick);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ToDo
            }
        });
    }

    private TextWatcher mTextChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.d("MainActivity", "before text changed = " + s.toString());
        }

        @Override
        public void onTextChanged(CharSequence query, int start, int before, int count) {

            Log.d("MainActivity", "on text changed = " + query.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            Log.d("MainActivity", "after text changed = " + editable.toString());
        }
    };
}
