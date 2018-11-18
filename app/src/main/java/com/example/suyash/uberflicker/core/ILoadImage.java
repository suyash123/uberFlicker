package com.example.suyash.uberflicker.core;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.example.suyash.uberflicker.model.FlickrImage;

public interface ILoadImage {

    void onTaskComplete(Bitmap bitmap, ImageView imageView, FlickrImage flickrImage);
}
