package com.prayas.prayas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookDetailActivity extends AppCompatActivity {

    private Button readSampleBtn, buyBookBtn;
    private TextView bookTitleTV, bookAuthorTV, bookISBNTV, bookPublisherTV, bookDofPublshedTV, bookDescriptionTV, bookPriceTV;

    private ImageView bookImageView;

    private Activity activity;

    private BookDetail bookData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_book_detail);
        android.support.v7.app.ActionBar topBar =  getDelegate().getSupportActionBar();
        getSupportActionBar().setTitle("My new title");
        Drawable drawable;
       // int decode = Integer.decode("3F51B5");
        drawable = new ColorDrawable(0xFF3F51B5);
        getSupportActionBar().setBackgroundDrawable(drawable);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        activity = this;

        Bundle bundle = activity.getIntent().getExtras();
        if(!bundle.equals("")) {
            Intent intent = getIntent();
            bookData = (BookDetail) intent.getSerializableExtra("BookDetail");
            renderBookDetail(bookData);

        }

        readSampleBtn = (Button) findViewById(R.id.sampleButton);
        readSampleBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager packageManager = getPackageManager();

                Intent   testIntent = new Intent(Intent.ACTION_VIEW);
                testIntent.setType("application/pdf");


                List list = packageManager.queryIntentActivities(testIntent,
                        PackageManager.MATCH_DEFAULT_ONLY);


                File directory = new File(bookData.bookFilePath);
                Uri uri = Uri.fromFile(directory.getAbsoluteFile());

                Intent intent = new Intent(Intent.ACTION_VIEW);
             //   intent.setDataAndType(uri, "application/epub+zip");
                intent.setDataAndType(uri, "application/pdf");
                startActivity(intent);
            }
        });

        buyBookBtn = (Button) findViewById(R.id.buyButton);
        buyBookBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            //TODO: add popup

            }
        });


    }


    public void renderBookDetail(BookDetail bookDetail){

        bookTitleTV = (TextView) findViewById(R.id.bookTitle);
        bookAuthorTV = (TextView) findViewById(R.id.bookAuthor);

        bookISBNTV = (TextView) findViewById(R.id.isbntextView);
        bookPublisherTV = (TextView) findViewById(R.id.publisherTextView);
        bookDofPublshedTV = (TextView) findViewById(R.id.doPublishedtextView);

        bookDescriptionTV = (TextView) findViewById(R.id.descriptionTextView);
        bookPriceTV = (TextView) findViewById(R.id.priceTextView);

        bookImageView = (ImageView) findViewById(R.id.bookImageView);

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

        if (bookDetail.bookPublishedDate != ""){
            bookDofPublshedTV.setText(bookDetail.bookPublishedDate.trim());
        }

        if (bookDetail.bookDescription != ""){
            bookDescriptionTV.setText(bookDetail.bookDescription.trim());
        }

        if (bookDetail.bookPrice != ""){
            bookPriceTV.setText("Price: "+bookDetail.bookPrice.trim());
        }


    }

    public  void showAlertForPurchase(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);

        String message = "Purchase " + bookTitleTV.getText() + "for just " + bookPriceTV.getText() + " !!";
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setPositiveButton("BUY",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        UssageDetail bookPurchaseData = new UssageDetail();
                        bookPurchaseData.ussageId = bookData.bookISBN;
                        bookPurchaseData.dataType = "BOOK_PURCHASE";
                        bookPurchaseData.dataMessage = "You have purchased " + bookData.bookName + "at " + bookData.bookPrice;
                        Date dateobj = new Date();
                        bookPurchaseData.orderDate = dateobj;

                        File directory = new File(bookData.bookFilePath);
                        Uri uri = Uri.fromFile(directory.getAbsoluteFile());

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        //   intent.setDataAndType(uri, "application/epub+zip");
                        intent.setDataAndType(uri, "application/pdf");
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
