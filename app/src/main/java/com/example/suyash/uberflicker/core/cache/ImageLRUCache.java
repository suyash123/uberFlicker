package com.example.suyash.uberflicker.core.cache;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.util.LruCache;

public class ImageLRUCache extends LruCache<String, ImageLRUCache.BitmapAndSize> {

    protected Resources mResources;

    private Context context;

    private final static int MAX_CACHE_SIZE = 1024 * 15;

    private static ImageLRUCache instance;

    private ImageLRUCache(int maxSize, Context context) {
        super(maxSize);
        this.context = context;
        this.mResources = context.getResources();
    }

    public static ImageLRUCache getInstance(Context context)
    {
        if (instance == null)
        {
            synchronized (ImageLRUCache.class)
            {
                if (instance == null)
                    instance = new ImageLRUCache(MAX_CACHE_SIZE, context);
            }
        }
        return instance;
    }

    public BitmapAndSize putInCache(String data, Bitmap bitmap)
    {
        if (null != bitmap)
        {
            BitmapDrawable value = new BitmapDrawable(context.getResources(), bitmap);
            if (RecyclingBitmapDrawable.class.isInstance(value))
                ((RecyclingBitmapDrawable) value).setIsCached(true);
            return put(data, new BitmapAndSize(value, sizeOf(value)));
        }

        return null;
    }

    @Override
    protected int sizeOf(String key, BitmapAndSize value) {
        return value.allocationSize;
    }

    public int sizeOf(BitmapDrawable bitmapDrawable) {
        final int bitmapSize = getBitmapSize(bitmapDrawable) / 1024;
        return bitmapSize == 0 ? 1 : bitmapSize;
    }

    public int getBitmapSize(BitmapDrawable bd) {
        if (bd == null)
            return 0;

        return getBitmapSize(bd.getBitmap());
    }

    public int getBitmapSize(Bitmap bitmap) {
        if (bitmap == null)
            return 0;
        return bitmap.getByteCount();
    }

    public BitmapDrawable getBitmap(String key) {
        return fetchAndupdateCacheForRecycledBitmap(key);
    }

    protected void entryRemoved(boolean evicted, String key, BitmapDrawable oldValue, BitmapDrawable newValue)
    {
        if (RecyclingBitmapDrawable.class.isInstance(oldValue))
        {
            ((RecyclingBitmapDrawable) oldValue).setIsCached(false);
        }
    }

    private BitmapDrawable fetchAndupdateCacheForRecycledBitmap(String key){
        BitmapAndSize bitmapAndSize = get(key);
        BitmapDrawable bitmapDrawable = bitmapAndSize == null ? null : bitmapAndSize.bitmap;
        if (null != bitmapDrawable && bitmapDrawable.getBitmap() != null && bitmapDrawable.getBitmap().isRecycled()) {
            remove(key);
            return null;
        }
        return bitmapDrawable;
    }

    public static final class BitmapAndSize {
        final BitmapDrawable bitmap;
        final int allocationSize;

        public BitmapAndSize(BitmapDrawable bitmap, int allocationSize) {
            this.bitmap = bitmap;
            this.allocationSize = allocationSize;
        }

        private BitmapDrawable getBitmap() {
            return bitmap;
        }

        public int getAllocationSize() {
            return allocationSize;
        }
    }

}
