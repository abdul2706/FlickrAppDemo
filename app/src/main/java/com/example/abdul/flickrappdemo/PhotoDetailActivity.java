package com.example.abdul.flickrappdemo;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        activateToolbar(true);
        Intent intent = getIntent();
        Photo photo = (Photo) intent.getSerializableExtra(PHOTO_TRANSFER);
        if(photo != null) {
            TextView photoTitle = findViewById(R.id.photoTitle);
            Resources resources = getResources();
            photoTitle.setText(resources.getString(R.string.photo_title_text, photo.getTitle()));

            TextView photoTags = findViewById(R.id.photoTags);
            photoTags.setText(resources.getString(R.string.photo_tags_text, photo.getTags()));

            TextView photoAuthor = findViewById(R.id.photoAuthor);
            photoAuthor.setText(resources.getString(R.string.photo_author_text, photo.getAuthor()));

            ImageView photoImage = findViewById(R.id.photoImage);
            Picasso.get().load(photo.getLink())
                    .error(R.drawable.baseline_photo_black_48dp)
                    .placeholder(R.drawable.baseline_photo_black_48dp)
                    .into(photoImage);
        }
    }

}
