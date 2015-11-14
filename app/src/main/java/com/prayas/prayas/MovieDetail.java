package com.prayas.prayas;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by vivekanandjha on 21/10/15.
 */
public class MovieDetail implements Serializable {

    String filePath = "";
    String movieTitle = "";
    String movieArtist = "";
    String movieId = "";
    String mimeType = "";
    String thumbPath = "";
    String movieDuration;
    String movieDescription = "";
    String movieTags;
    String moviePrice = "Rs 5/-";
    String trailerFilePath = "";
    String trailerMimeType = "";
  transient   Bitmap coverBitmap;
    String coverFilepath;
    //int previewImage;
}
