package com.example.suyash.uberflicker.model;

import java.io.Serializable;
import java.util.List;

public class SearchedImages implements Serializable {

    private int pages;
    private List<FlickrImage> flickrImageList;

    public SearchedImages(int pages, List<FlickrImage> flickrImageList) {
        this.pages = pages;
        this.flickrImageList = flickrImageList;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<FlickrImage> getFlickrImageList() {
        return flickrImageList;
    }

    public void setFlickrImageList(List<FlickrImage> flickrImageList) {
        this.flickrImageList = flickrImageList;
    }
}
