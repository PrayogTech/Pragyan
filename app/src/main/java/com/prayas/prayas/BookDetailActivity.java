package com.prayas.prayas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class BookDetailActivity extends AppCompatActivity {

    private Button readSampleBtn, buyBookBtn;
    private TextView bookTitleTV, bookAuthorTV, bookISBNTV, bookPublisherTV, bookDofPublshedTV, bookDescriptionTV, bookPriceTV;

    private ImageView bookImageView;

    private Activity activity;

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    private BookDetail bookData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_book_detail);
        android.support.v7.app.ActionBar topBar =  getDelegate().getSupportActionBar();
        getSupportActionBar().setTitle("Book Details");
        Drawable drawable;
       // int decode = Integer.decode("3F51B5");
        drawable = new ColorDrawable(0xFF3F51B5);
        getSupportActionBar().setBackgroundDrawable(drawable);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        activity = this;

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Bundle bundle = activity.getIntent().getExtras();
        if(!bundle.equals("")) {
            Intent intent = getIntent();

            bookData = (BookDetail) intent.getSerializableExtra("BookDetail");
            Bitmap cover = MyUsedData.getInstance().getBookCoverBitmap();
            if (cover != null){
                bookData.bookBitmap = cover;
            }
            renderBookDetail(bookData);

        }

        readSampleBtn = (Button) findViewById(R.id.sampleButton);
        readSampleBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager packageManager = getPackageManager();

                if (bookData.bookSampleDirectory != null && !bookData.bookSampleDirectory.equals("")) {

                    File directory = new File(bookData.bookSampleDirectory);

                    Uri uri = Uri.fromFile(directory.getAbsoluteFile());
                    // File urlFile = new File(bookData.bookFilePath.getFile());
                    //  Uri uri = Uri.fromFile(urlFile.getAbsoluteFile());

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, "application/epub+zip");
                    //intent.setDataAndType(uri, "application/pdf");
                    startActivity(intent);
                }else {
                    Toast.makeText(activity, "Sample Book Not found", Toast.LENGTH_SHORT).show();
                }

            }
        });

        buyBookBtn = (Button) findViewById(R.id.buyButton);
        buyBookBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: add popup
                // ArrayList<UssageDetail> ussageList = MyUsedData.getInstance().getUsedDataList();
                Gson gson = new Gson();
                Boolean purchaseStatus = false;
                String jsonCartList = sharedpreferences.getString("order", "");
                if (!jsonCartList.equals("")) {
                    Type t = new TypeToken<List<UssageDetail>>() {}.getType();
                    ArrayList<UssageDetail> ussageList =  (ArrayList<UssageDetail>)gson.fromJson(jsonCartList, t);
                    Log.d("mook ", ussageList.toString());
                    if (ussageList.size() > 0) {
                        for (UssageDetail u : ussageList){
                            System.out.print("val stoer"+ u);
                        }
                    Iterator<UssageDetail> iterator = ussageList.iterator();
                    while (iterator.hasNext()) {
                        UssageDetail ussageInfo = iterator.next();
                        if (ussageInfo.ussageId == bookData.bookISBN && ussageInfo.dataType == "BOOK_PURCHASE") {
                            purchaseStatus = true;

                            // File urlFile = new File(bookData.bookFilePath.getFile());
                            // Uri uri = Uri.fromFile(urlFile);
                            File directory = new File(bookData.bookDiretory);
                            Uri uri = Uri.fromFile(directory.getAbsoluteFile());

                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(uri, "application/epub+zip");
                            //intent.setDataAndType(uri, "application/pdf");
                            startActivity(intent);
                            //  Toast.makeText(activity, "You have already purchased this book", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }
            }
                if(!purchaseStatus) {
                    showAlertForPurchase();
                }
            }
        });


    }


    public void renderBookDetail(BookDetail bookDetail){

        bookTitleTV = (TextView) findViewById(R.id.bookTitle);
        bookAuthorTV = (TextView) findViewById(R.id.bookAuthor);

        bookISBNTV = (TextView) findViewById(R.id.isbnTextView);
        bookPublisherTV = (TextView) findViewById(R.id.publisherTextView);

        bookDescriptionTV = (TextView) findViewById(R.id.descriptionTextView);
        bookPriceTV = (TextView) findViewById(R.id.priceTextView);

        bookImageView = (ImageView) findViewById(R.id.bookImageView);

        if (bookDetail.bookBitmap != null){
            bookImageView.setImageBitmap(bookDetail.bookBitmap);
        }
        //TODO: render data

        if (bookDetail.bookName != ""){
            bookTitleTV.setText(bookDetail.bookName.trim());
        }

        if (bookDetail.authorName != ""){
            bookAuthorTV.setText(bookDetail.authorName.trim());
        }

        if (bookDetail.bookISBN != ""){
            bookISBNTV.setText(bookDetail.bookISBN.trim());
        }

        if (bookDetail.bookPublisher != ""){
            bookPublisherTV.setText(bookDetail.bookPublisher.trim());
        }

        if (bookDetail.bookDescription != ""){
            bookDescriptionTV.setText(bookDetail.bookDescription.trim());
        }

        if (bookDetail.bookPrice != ""){
            bookPriceTV.setText("Ussage Charge : "+bookDetail.bookPrice.trim());
        }


    }

    public  void showAlertForPurchase(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);

        String message =  bookPriceTV.getText() + " will be applicable " + "  for getting " + bookTitleTV.getText()  +  " !!";
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        UssageDetail bookPurchaseData = new UssageDetail();
                        bookPurchaseData.ussageId = bookData.bookISBN;
                        bookPurchaseData.dataType = "BOOK_PURCHASE";
                        bookPurchaseData.dataMessage = "You have used " + bookData.bookName + " at " + bookData.bookPrice;
                        Date dateobj = new Date();
                        bookPurchaseData.orderDate = dateobj;
                        bookPurchaseData.bookInfo = bookData;

                        MyUsedData.getInstance().getUsedDataList().add(bookPurchaseData);

                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        Gson gson = new Gson();
                        String jsonCartList = gson.toJson(MyUsedData.getInstance().getUsedDataList());
                        editor.putString("order", jsonCartList);
                        editor.commit();

                        //File urlFile = new File(bookData.bookFilePath.getFile());
                        // Uri uri = Uri.fromFile(urlFile);
                        File directory = new File(bookData.bookDiretory);
                        Uri uri = Uri.fromFile(directory.getAbsoluteFile());

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, "application/epub+zip");
                        //intent.setDataAndType(uri, "application/pdf");
                        startActivity(intent);
                    }
                });
        builder1.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
