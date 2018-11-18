package com.example.suyash.uberflicker.core.net;

import android.text.TextUtils;

import com.example.suyash.uberflicker.core.ISearchImages;
import com.example.suyash.uberflicker.core.parser.SearchedImgJsonParser;
import com.example.suyash.uberflicker.model.SearchedImages;

import org.json.JSONException;
import org.json.JSONObject;

public class SearchPhotosTask implements Runnable {

    private static final String SEARCH_TEXT_KEY = "&text=";
    private static final String SEARCH_PAGE_KEY = "&page=";
    private static final String API_KEY = "3e7cc266ae2b0e0d78e279ce8e361736";
    private static final String GET_SEARCH_IMAGES_URL = "services/rest/?method=flickr.photos.search&api_key=" +
            API_KEY + "&format=json&nojsoncallback=1&safe_search=1";
    private static final String MAIN_BASE_URL = "https://api.flickr.com/";

    private ISearchImages iSearchImages;
    private String searchText;
    private int pageNo;

    public SearchPhotosTask(ISearchImages iSearchImages, String searchText, int pageNo) {
        this.iSearchImages = iSearchImages;
        this.searchText = searchText;
        this.pageNo = pageNo;
    }

    @Override
    public void run() {
        HttpClient httpClient = new HttpClient();
        String responseData = httpClient.searchImages(getSearchUrl(searchText, pageNo));

        if(!TextUtils.isEmpty(responseData)) {
            try {
                JSONObject jsonObject = new JSONObject(responseData);
                SearchedImages searchedImages = new SearchedImgJsonParser().parseSearchListResponse(jsonObject);
                if(iSearchImages != null){
                    iSearchImages.onTaskComplete(searchedImages);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private String getSearchUrl(String searchText, int pageNo){
        String url = MAIN_BASE_URL;
        url += GET_SEARCH_IMAGES_URL;
        url += SEARCH_TEXT_KEY + searchText;
        url += SEARCH_PAGE_KEY + pageNo;
        return url;
    }
}
