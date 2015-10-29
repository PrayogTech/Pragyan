package com.prayas.prayas;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kokila on 20/10/15.
 */
public class MyUsedData {


    private static MyUsedData mInstance;

    private ArrayList<UssageDetail> usedDataList;

    private Bitmap bookCoverBitmap;

    private MyUsedData(){

        usedDataList = new ArrayList<UssageDetail>();
    }

    public static synchronized MyUsedData getInstance (){
        if (mInstance == null) {
            mInstance = new MyUsedData();
        }
        return mInstance;
    }

    public ArrayList<UssageDetail> getUsedDataList() {
        return usedDataList;
    }

    public void setUsedDataList(ArrayList<UssageDetail> usedDataList) {
        this.usedDataList = usedDataList;
    }

    public Bitmap getBookCoverBitmap() {
        return bookCoverBitmap;
    }

    public void setBookCoverBitmap(Bitmap bookCoverBitmap) {
        this.bookCoverBitmap = bookCoverBitmap;
    }
    public  void clearBookCoverImage(){
        this.bookCoverBitmap = null;
    }
}
