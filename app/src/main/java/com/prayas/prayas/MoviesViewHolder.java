package com.prayas.prayas;

import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by vivekanandjha on 21/10/15.
 */
public class MoviesViewHolder {
    TextView nametextView;
    ImageView previewImageView;
    TextView artistTextview;
    TextView durationTextview;
    //TextView mimeTypeTextview;

    public MoviesViewHolder(TextView nametextView, ImageView previewImageView, TextView artistTextview, TextView durationTextview) {
        this.nametextView = nametextView;
        this.previewImageView = previewImageView;
        this.artistTextview = artistTextview;
        this.durationTextview = durationTextview;
        //this.mimeTypeTextview = mimeTextview;
    }
}
