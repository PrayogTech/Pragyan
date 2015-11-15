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
   // byte[] byteArray;
    transient Bitmap bookBitmap;
    String bookISBN = "";
    String bookPublisher = "";
   // String bookPublishedDate = "";
    String bookPrice = "Rs. 5/-";
    String bookDescription = "";
    URL bookFilePath;
    String bookDiretory = "";
    URL bookSampeFilePath;
    String bookSampleDirectory = "";


    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int getBookIcon() {
        return bookIcon;
    }

    public void setBookIcon(int bookIcon) {
        this.bookIcon = bookIcon;
    }

    public Bitmap getBookBitmap() {
        return bookBitmap;
    }

    public void setBookBitmap(Bitmap bookBitmap) {
        this.bookBitmap = bookBitmap;
    }

    public String getBookISBN() {
        return bookISBN;
    }

    public void setBookISBN(String bookISBN) {
        this.bookISBN = bookISBN;
    }

    public String getBookPublisher() {
        return bookPublisher;
    }

    public void setBookPublisher(String bookPublisher) {
        this.bookPublisher = bookPublisher;
    }

  /*  public String getBookPublishedDate() {
        return bookPublishedDate;
    }

    public void setBookPublishedDate(String bookPublishedDate) {
        this.bookPublishedDate = bookPublishedDate;
    }
*/
    public String getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(String bookPrice) {
        this.bookPrice = bookPrice;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public URL getBookFilePath() {
        return bookFilePath;
    }

    public void setBookFilePath(URL bookFilePath) {
        this.bookFilePath = bookFilePath;
    }

    public String getBookDiretory() {
        return bookDiretory;
    }

    public void setBookDiretory(String bookDiretory) {
        this.bookDiretory = bookDiretory;
    }

    public URL getBookSampeFilePath() {
        return bookSampeFilePath;
    }

    public void setBookSampeFilePath(URL bookSampeFilePath) {
        this.bookSampeFilePath = bookSampeFilePath;
    }

    public String getBookSampleDirectory() {
        return bookSampleDirectory;
    }

    public void setBookSampleDirectory(String bookSampleDirectory) {
        this.bookSampleDirectory = bookSampleDirectory;
    }
}
