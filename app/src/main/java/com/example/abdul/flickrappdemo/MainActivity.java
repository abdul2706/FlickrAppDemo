package com.example.abdul.flickrappdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements GetFlickrJSONData.OnDataAvailable, RecyclerItemClickListener.OnRecyclerClickListener {

    private static final String TAG = "MainActivity";
    private FlickrRecyclerViewAdapter flickrRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activateToolbar(false);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, this));

        flickrRecyclerViewAdapter = new FlickrRecyclerViewAdapter(this, new ArrayList<Photo>());
        recyclerView.setAdapter(flickrRecyclerViewAdapter);
        Log.d(TAG, "onCreate: end");
    }

    @Override
    protected void onResume(){
        Log.d(TAG, "onResume: start");
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String queryResult = sharedPreferences.getString(FLICKR_QUERY, "");
//        queryResult = "format=json&id=156365122@N06";
        if(queryResult.length() > 0) {
            GetFlickrJSONData getFlickrJSONData = new GetFlickrJSONData(this, "https://api.flickr.com/services/feeds/photos_public.gne", "en-us", true);
            getFlickrJSONData.execute(queryResult);
        }
        Log.d(TAG, "onResume: end");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            return true;
        } else if(id == R.id.action_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataAvailable(List<Photo> data, DownloadStatus status) {
        Log.d(TAG, "onDataAvailable: start");
        if (status == DownloadStatus.OK) {
            flickrRecyclerViewAdapter.loadData(data);
        } else {
            Log.d(TAG, "onDataAvailable: Download Failed with status -> " + status);
        }
        Log.d(TAG, "onDataAvailable: end");
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i(TAG, "onItemClick: start");
        Toast.makeText(MainActivity.this, "normal tap at position -> " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Log.i(TAG, "onItemLongClick: start");
//        Toast.makeText(MainActivity.this, "long tap at position -> " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, PhotoDetailActivity.class);
        intent.putExtra(PHOTO_TRANSFER, flickrRecyclerViewAdapter.getPhoto(position));
        startActivity(intent);
    }
}
