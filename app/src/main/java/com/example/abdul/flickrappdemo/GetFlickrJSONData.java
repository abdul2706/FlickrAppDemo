package com.example.abdul.flickrappdemo;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetFlickrJSONData extends AsyncTask<String, Void, List<Photo>> implements GetRawData.OnDownloadComplete {

    private static final String TAG = "GetFlickrJSONData";
    private List<Photo> photoList = null;
    private String baseURL;
    private String language;
    private boolean matchAll;

    private final OnDataAvailable callBack;
    private boolean runOnSameThread = false;

    interface OnDataAvailable {
        void onDataAvailable(List<Photo> data, DownloadStatus status);
    }

    GetFlickrJSONData(OnDataAvailable callBack, String baseURL, String language, boolean matchAll) {
        this.callBack = callBack;
        this.baseURL = baseURL;
        this.language = language;
        this.matchAll = matchAll;
    }

    public void executeOnSameThread(String searchCriteria) {
        Log.d(TAG, "executeOnSameThread: start");
        runOnSameThread = true;
        String destinationURI = createURI(searchCriteria, language, matchAll);
        GetRawData getRawData = new GetRawData(this);
        getRawData.execute(destinationURI);
        Log.d(TAG, "executeOnSameThread: end");
    }

    private String createURI(String searchCriteria, String language, boolean matchAll) {
//        return Uri.parse(baseURL).buildUpon()
//                .appendQueryParameter("tags", searchCriteria)
//                .appendQueryParameter("tagmode", matchAll ? "ALL" : "ANY")
//                .appendQueryParameter("lang", language)
//                .appendQueryParameter("format", "json")
//                .appendQueryParameter("nojsoncallback", "1")
//                .build().toString();
        return Uri.parse(baseURL).buildUpon()
                .appendQueryParameter("id", "156365122@N06")
                .appendQueryParameter("lang", language)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .build().toString();
    }

    @Override
    protected void onPostExecute(List<Photo> s) {
        if(callBack != null) {
            callBack.onDataAvailable(s, DownloadStatus.OK);
        }
    }

    @Override
    protected List<Photo> doInBackground(String... strings) {
        String destinationUri = createURI(strings[0], language, matchAll);

        GetRawData getRawData = new GetRawData(this);
        getRawData.runOnSameThread(destinationUri);
        return photoList;
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        if(status == DownloadStatus.OK) {
            photoList = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONArray itemsArray = jsonObject.getJSONArray("items");
                for(int i = 0; i < itemsArray.length(); i++){
                    JSONObject jsonPhoto = itemsArray.getJSONObject(i);

                    String title = jsonPhoto.getString("title");
                    String author = jsonPhoto.getString("author");
                    String authorId = jsonPhoto.getString("author_id");
                    String tags = jsonPhoto.getString("tags");

                    JSONObject jsonMedia = jsonPhoto.getJSONObject("media");
                    String photoURL = jsonMedia.getString("m");
                    String link = photoURL.replaceFirst("_m.", "_b.");

                    Photo photoObject = new Photo(title, author, authorId, link, tags, photoURL);
                    photoList.add(photoObject);
                }
            } catch (JSONException e) {
                Log.e(TAG, "onDownloadComplete: error -> " + e.getMessage() );
                e.printStackTrace();
                status = DownloadStatus.FAILED_OR_EMPTY;
            }
        }
        if(runOnSameThread && callBack != null){
            callBack.onDataAvailable(photoList, status);
        }
    }

}
