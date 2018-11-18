package com.example.suyash.uberflicker.core.parser;

import com.example.suyash.uberflicker.model.FlickrImage;
import com.example.suyash.uberflicker.model.SearchedImages;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchedImgJsonParser {

    private static final String KEY_PHOTOS_JSON = "photos";
    private static final String KEY_STAT = "stat";
    private static final String KEY_OK = "ok";
    private static final String KEY_PAGES = "pages";
    private static final String KEY_PHOTOS_ARRAY = "photo";
    private static final String KEY_PHOTO_ID = "id";
    private static final String KEY_OWNER = "owner";
    private static final String KEY_SECRET = "secret";
    private static final String KEY_SERVER = "server";
    private static final String KEY_FARM = "farm";
    private static final String KEY_TITLE = "title";

    public SearchedImages parseSearchListResponse(JSONObject jsonObject) {
        SearchedImages searchedImages = null;
        try {
            if (jsonObject.has(KEY_STAT)) {
                if (jsonObject.getString(KEY_STAT).contentEquals(KEY_OK)) {
                    JSONObject resultObject = jsonObject.getJSONObject(KEY_PHOTOS_JSON);
                    JSONArray jsonArray = resultObject.getJSONArray(KEY_PHOTOS_ARRAY);
                    searchedImages = new SearchedImages(resultObject.getInt(KEY_PAGES), parseSearchArray(jsonArray));
                }
            } else {
                return searchedImages;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return searchedImages;
    }

    private List<FlickrImage> parseSearchArray(JSONArray jsonArray) {
        List<FlickrImage> flickrImageList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                FlickrImage flickrImage = new FlickrImage(
                        jsonObj.getString(KEY_PHOTO_ID),
                        jsonObj.getString(KEY_OWNER),
                        jsonObj.getString(KEY_SECRET),
                        jsonObj.getString(KEY_SERVER),
                        jsonObj.getInt(KEY_FARM),
                        jsonObj.getString(KEY_TITLE));
                flickrImageList.add(flickrImage);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return flickrImageList;
    }
}
