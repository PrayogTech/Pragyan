package com.prayas.prayas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity {

    private Button watchSampleBtn, buyMovieBtn;
    private TextView movieTitleTV, movieArtistTV, bookPublisherTV, bookDofPublshedTV, movieDescriptionTV, moviePriceTV;

    private ImageView movieImageView;

    private Activity activity;

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    MovieDetail movieDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_movie_detail);


        getSupportActionBar().setTitle("Movie Details");
        Drawable drawable;
        // int decode = Integer.decode("3F51B5");
        drawable = new ColorDrawable(0xFF3F51B5);
        getSupportActionBar().setBackgroundDrawable(drawable);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        activity = this;
Log.d("detail called", "move");
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Bundle bundle = activity.getIntent().getExtras();
        if(!bundle.equals("")) {
            Intent intent = getIntent();

            movieDetail = (MovieDetail) intent.getSerializableExtra("MovieDetail");
            Bitmap cover = MyUsedData.getInstance().getMovieCoverBitmap();
            if (cover != null){
               // movieDetail.bookBitmap = cover;
                movieDetail.coverBitmap = cover;

            }else {
                Toast.makeText(activity, "Cover not found", Toast.LENGTH_SHORT).show();
            }
            //renderBookDetail(bookData);

            renderMovieDetail(movieDetail);
        }

        watchSampleBtn = (Button) findViewById(R.id.sampleButton);
        watchSampleBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("trailer click", movieDetail.trailerFilePath);
                if (!movieDetail.trailerFilePath.equals("")) {

                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                    File newFile = new File(movieDetail.trailerFilePath);
                    intent.setDataAndType(Uri.fromFile(newFile), movieDetail.trailerMimeType);
                    startActivity(intent);
                } else {
                    Toast.makeText(activity, "Trailer Not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buyMovieBtn = (Button) findViewById(R.id.buyButton);
        buyMovieBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
// ArrayList<UssageDetail> ussageList = MyUsedData.getInstance().getUsedDataList();
                Gson gson = new Gson();
                Boolean purchaseStatus = false;
                String jsonCartList = sharedpreferences.getString("order", "");
                if (!jsonCartList.equals("")) {
                    //  ArrayList ussageList = gson.fromJson(jsonCartList, ArrayList.class);
                    //  List<UssageDetail> ussageList = (List<UssageDetail>) gson.fromJson(jsonCartList, UssageDetail.class);
                    Type t = new TypeToken<List<UssageDetail>>() {}.getType();
                    ArrayList<UssageDetail> ussageList =  (ArrayList<UssageDetail>)gson.fromJson(jsonCartList, t);
                    Log.d("mook ", ussageList.toString());

                        Iterator<UssageDetail> iterator = ussageList.iterator();
                        while (iterator.hasNext()) {
                            UssageDetail ussageInfo = iterator.next();
                            if (ussageInfo.ussageId.equals(movieDetail.movieId)  && ussageInfo.dataType.equals("MOVIE_PURCHASE")) {
                                purchaseStatus = true;
                                Log.d("movie c", movieDetail.filePath);
                                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                                File newFile = new File(movieDetail.filePath);
                                intent.setDataAndType(Uri.fromFile(newFile), movieDetail.mimeType);
                                startActivity(intent);
                                //Toast.makeText(activity, "You have already purchased this movie", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                }

                if(!purchaseStatus)

                {
                    showAlertForPurchase(movieDetail);
                }
            }
        });
    }

    public void renderMovieDetail(MovieDetail movieDetail){

        movieTitleTV = (TextView) findViewById(R.id.movieTitle);
        movieArtistTV = (TextView) findViewById(R.id.movieArtist);

        movieDescriptionTV = (TextView) findViewById(R.id.descriptionTextView);
        moviePriceTV = (TextView) findViewById(R.id.priceTextView);
        movieImageView = (ImageView) findViewById(R.id.movieImageView);

        if (movieDetail.movieArtist != null && movieDetail.movieTitle != ""){
             movieTitleTV.setText(movieDetail.movieTitle.trim());
        }
        if (movieDetail.movieArtist != null && movieDetail.movieArtist != ""){
            movieArtistTV.setText(movieDetail.movieArtist.trim());
        }
        if (movieDetail.movieDescription != null && movieDetail.movieDescription != ""){
            movieDescriptionTV.setText(movieDetail.movieDescription.trim());
        }
        if (movieDetail.moviePrice != null && movieDetail.moviePrice != ""){
            moviePriceTV.setText(movieDetail.moviePrice.trim());
        }

        if (movieDetail.coverBitmap != null){
            movieImageView.setImageBitmap(movieDetail.coverBitmap);
        } else {
            Bitmap icon = BitmapFactory.decodeResource(activity.getResources(),
                    R.drawable.no_thumbnail);
            movieImageView.setImageBitmap(icon);
        }

        Log.d("hjhfj", "");
    }

    /**
     *
     * @param path
     *            the path to the Video
     * @return a thumbnail of the video or null if retrieving the thumbnail failed.
     */
    public static Bitmap getVidioThumbnail(String path) {
        Bitmap bitmap = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MICRO_KIND);
            if (bitmap != null) {
                return bitmap;
            }
        }
        return bitmap;
    }

    public  void showAlertForPurchase(final MovieDetail movieInfo ){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);

        String message = "Watch " + movieInfo.movieTitle + " for just " + movieInfo.moviePrice + " !!";
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setPositiveButton("WATCH",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        UssageDetail moviePurchaseData = new UssageDetail();
                        moviePurchaseData.ussageId = movieInfo.movieId;
                        moviePurchaseData.dataType = "MOVIE_PURCHASE";
                        moviePurchaseData.dataMessage = "Ussage charge  of " + movieInfo.moviePrice + " will be applicable to watch " + movieInfo.movieTitle + " !!" ;
                        Date dateobj = new Date();
                        moviePurchaseData.orderDate = dateobj;
                        moviePurchaseData.movieDetail = movieInfo;

                        Gson gson = new Gson();

                        String jsonCartprev =  sharedpreferences.getString("order", "");
                        if (!jsonCartprev.equals("")) {
                            //ArrayList ussageDetailArrayList = gson.fromJson(jsonCartList, ArrayList.class);
                            Type t = new TypeToken<List<UssageDetail>>() {
                            }.getType();
                            ArrayList<UssageDetail> ussageDetailArrayList = (ArrayList<UssageDetail>) gson.fromJson(jsonCartprev, t);
                            Log.d("mook set lst ", ussageDetailArrayList.toString());

                            MyUsedData.getInstance().setUsedDataList(ussageDetailArrayList);
                        }

                        //Set in my ussage
                        MyUsedData.getInstance().getUsedDataList().add(moviePurchaseData);

                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        //Gson gson = new Gson();
                        String jsonCartList = gson.toJson(MyUsedData.getInstance().getUsedDataList());
                        editor.putString("order", jsonCartList);
                        editor.commit();

                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                        File newFile = new File(movieInfo.filePath);
                        intent.setDataAndType(Uri.fromFile(newFile), movieInfo.mimeType);
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movies_view, menu);
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
}
