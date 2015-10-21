package com.prayas.prayas;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.net.URL;

/**
 * Created by kokila on 15/10/15.
 */
public class BookDetail implements Serializable {

    String bookName = "";
    String authorName = "";
    int bookIcon;
    String bookISBN = "";
    String bookPublisher = "";
    String bookPublishedDate = "";
    String bookPrice = "";
    String bookDescription = "";
    URL bookFilePath;
}
