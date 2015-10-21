package com.prayas.prayas;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by kokila on 16/10/15.
 */
public class BookViewHolder {

    TextView nametextView;
    ImageView iconimageView;
    TextView authorTextview;

public BookViewHolder(TextView nametextView, ImageView iconimageView, TextView authorTextview) {
this.authorTextview = authorTextview;
    this.nametextView = nametextView;
    this.iconimageView = iconimageView;
}
}