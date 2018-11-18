package com.example.suyash.uberflicker.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.widget.ImageView;

import com.example.suyash.uberflicker.R;
import com.example.suyash.uberflicker.core.cache.ImageLRUCache;
import com.example.suyash.uberflicker.core.net.HttpManager;
import com.example.suyash.uberflicker.core.net.LoadPhotoTask;
import com.example.suyash.uberflicker.model.FlickrImage;

public class ImageLoader implements ILoadImage{

    private static ImageLoader instance;

    private ImageLoader() {
    }

    public static ImageLoader getInstance()
    {
        if (instance == null)
        {
            synchronized (ImageLoader.class)
            {
                if (instance == null)
                    instance = new ImageLoader();
            }
        }
        return instance;
    }

    public void setDrawable(ImageView imageView, FlickrImage flickrImage) {
        BitmapDrawable bitmapDrawable = ImageLRUCache.getInstance(imageView.getContext()).getBitmap(flickrImage.getImageURL());
        if(bitmapDrawable != null) {
             imageView.setImageDrawable(bitmapDrawable);
             return;
        }
        imageView.setImageResource(R.mipmap.ic_launcher);
        LoadPhotoTask loadPhotoTask = new LoadPhotoTask(this, flickrImage, imageView);
        HttpManager.getInstance().executeTask(loadPhotoTask);
    }

    @Override
    public void onTaskComplete(Bitmap bitmap, ImageView imageView, FlickrImage flickrImage) {
        if(bitmap == null) {
            bitmap = BitmapFactory.decodeResource(imageView.getContext().getResources(), R.mipmap.ic_launcher_round);
        }
        ImageLRUCache.getInstance(imageView.getContext()).putInCache(flickrImage.getImageURL(), bitmap);
        postOnUI(imageView, flickrImage.getImageURL());
    }

    private void postOnUI(final ImageView imageView, final String imgUrl ) {
        Handler mainHandler = new Handler(imageView.getContext().getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                if(imageView != null) {
                    imageView.setImageDrawable(ImageLRUCache.getInstance(imageView.getContext()).getBitmap(imgUrl));
                }
            }
        };
        mainHandler.post(myRunnable);
    }
}
