package com.example.suyash.uberflicker.core.net;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.example.suyash.uberflicker.core.ILoadImage;
import com.example.suyash.uberflicker.model.FlickrImage;

public class LoadPhotoTask implements Runnable {

    private ILoadImage iLoadImage;
    private FlickrImage flickrImage;
    private ImageView imageView;

    public LoadPhotoTask(ILoadImage iLoadImage, FlickrImage flickrImage, ImageView imageView) {
        this.iLoadImage = iLoadImage;
        this.flickrImage = flickrImage;
        this.imageView = imageView;
    }

    @Override
    public void run() {
        HttpClient httpClient = new HttpClient();

        Bitmap bitmap = httpClient.downloadImage(flickrImage.getImageURL());

        if(iLoadImage != null){
            iLoadImage.onTaskComplete(bitmap, imageView, flickrImage);
        }
    }
}
