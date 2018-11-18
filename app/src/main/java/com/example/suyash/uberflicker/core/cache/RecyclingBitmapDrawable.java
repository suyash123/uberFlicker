package com.example.suyash.uberflicker.core.cache;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public class RecyclingBitmapDrawable extends BitmapDrawable {

    private int mCacheRefCount = 0;

    private int mDisplayRefCount = 0;

    private boolean mHasBeenDisplayed;

    public RecyclingBitmapDrawable(Resources res, Bitmap bitmap)
    {
        super(res, bitmap);
    }

    @SuppressWarnings("deprecation")
    public RecyclingBitmapDrawable(Bitmap bitmap)
    {
        super(bitmap);
    }

    public void setIsDisplayed(boolean isDisplayed)
    {
        synchronized (this)
        {
            if (isDisplayed)
            {
                mDisplayRefCount++;
                mHasBeenDisplayed = true;
            }
            else
            {
                if (mDisplayRefCount > 0)
                {
                    mDisplayRefCount--;
                }
            }
        }

        checkState();
    }

    public void setIsCached(boolean isCached)
    {
        synchronized (this)
        {
            if (isCached)
            {
                mCacheRefCount++;
            }
            else
            {
                if (mCacheRefCount > 0)
                {
                    mCacheRefCount--;
                }
            }
        }
        checkState();
    }

    private synchronized void checkState()
    {
        if (mCacheRefCount <= 0 && mDisplayRefCount <= 0 && mHasBeenDisplayed && isBitmapValid())
        {
            getBitmap().recycle();
        }
    }

    public synchronized boolean isBitmapValid()
    {
        Bitmap bitmap = getBitmap();
        return bitmap != null && !bitmap.isRecycled();
    }

    public synchronized boolean isBitmapMutable()
    {
        Bitmap bitmap = getBitmap();
        return null != bitmap && bitmap.isMutable();
    }

    public int size()
    {
        Bitmap bitmap = this.getBitmap();
        return bitmap.getByteCount();
    }

    public void incrementCacheReference()
    {
        setIsCached(true);
    }
}
