package com.example.abdul.flickrappdemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

class FlickrRecyclerViewAdapter extends RecyclerView.Adapter<FlickrRecyclerViewAdapter.FlickrImageViewHolder> {

    private static final String TAG = "FlickrRecyclerViewAdapt";
    private List<Photo> photoList;
    private Context context;

    FlickrRecyclerViewAdapter(Context context, List<Photo> photoList) {
        this.context = context;
        this.photoList = photoList;
    }

    @NonNull
    @Override
    public FlickrImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse, parent, false);
        return new FlickrImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlickrImageViewHolder holder, int position) {
        if (photoList == null || photoList.size() == 0) {
            holder.thumbnail.setImageResource(R.drawable.baseline_photo_black_48dp);
            holder.title.setText(R.string.empty_photo);
        } else {
            Photo photoItem = photoList.get(position);
            Picasso.get().load(photoItem.getImage())
                    .error(R.drawable.baseline_photo_black_48dp)
                    .placeholder(R.drawable.baseline_photo_black_48dp)
                    .into(holder.thumbnail);

            holder.title.setText(photoItem.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return ((photoList != null && photoList.size() != 0) ? photoList.size() : 1);
    }

    public void loadData(List<Photo> newPhotoList) {
        photoList = newPhotoList;
        notifyDataSetChanged();
    }

    public Photo getPhoto(int position) {
        return ((photoList != null && photoList.size() != 0) ? photoList.get(position) : null);
    }

    static class FlickrImageViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "FlickrImageViewHolder";
        private ImageView thumbnail;
        private TextView title;

        FlickrImageViewHolder(View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            title = itemView.findViewById(R.id.title);
        }
    }

}
