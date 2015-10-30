package com.prayas.prayas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MoviesViewActivity extends AppCompatActivity  {

    private Cursor mCursor;
    private String mWhereClause;
    private String mSortOrder;

    private Activity activity;

    private ListView moviesListView;
    private ArrayList<MovieDetail> movieDetailArrayList = new ArrayList<>();

    private  MoviesAdapter moviesAdapter;
    FetchMoviesAsyncTask fetchMoviesAsynch;

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_view);

        getSupportActionBar().setTitle("Movies");
        Drawable drawable;
        // int decode = Integer.decode("3F51B5");
        drawable = new ColorDrawable(0xFF3F51B5);
        getSupportActionBar().setBackgroundDrawable(drawable);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        activity = this;

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        loadMoviesList();
       // MakeCursor();

    }


    class FetchMoviesAsyncTask extends AsyncTask<String, String, String>{

        /** progress dialog to show user that the backup is processing. */
        private ProgressDialog dialog;
        /** application context. */
        private MoviesViewActivity activity;

        public FetchMoviesAsyncTask(MoviesViewActivity mActivity) {
            activity = mActivity;
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Fetching Movies...");
            dialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            MakeCursor();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if(movieDetailArrayList.size() > 0) {
                getSupportActionBar().setTitle("Movies");
                if(moviesAdapter == null) {
                    moviesAdapter = new MoviesAdapter(activity, movieDetailArrayList);
                    moviesListView.setAdapter(moviesAdapter);
                    moviesListView.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            MovieDetail movieDetail = (MovieDetail) view.getTag(R.id.folder_holder);
                            showBook(movieDetail);
                    }
                });
                }else{
                    moviesAdapter = (MoviesAdapter) moviesListView.getAdapter();
                    moviesAdapter.notifyDataSetChanged();
                }

            }else {
                getSupportActionBar().setTitle("Movies Unavailable");
                Toast.makeText(activity, "Movies Not Available", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadMoviesList(){

        moviesListView = (ListView) findViewById(R.id.movieListView);
        //Set Adapter
        if(fetchMoviesAsynch !=null){

        }else {
            fetchMoviesAsynch = new FetchMoviesAsyncTask(this);
            fetchMoviesAsynch.execute();
        }
    }

    private void MakeCursor() {
        String[] cols = new String[] {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.ARTIST,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DESCRIPTION,
                MediaStore.Video.Media.TAGS
        };
        ContentResolver resolver = getContentResolver();
        if (resolver == null) {
            System.out.println("resolver = null");
        } else {
            String selection=MediaStore.Video.Media.DATA +" like?";
            String[] selectionArgs=new String[]{"%PrayasMovies%"};
            mSortOrder = MediaStore.Video.Media.TITLE + " COLLATE UNICODE";
            mWhereClause = MediaStore.Video.Media.TITLE + " != ''";
            mCursor = resolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    cols, selection , selectionArgs, mSortOrder);

            if (mCursor == null) {
                //MusicUtils.displayDatabaseError(this);
                //TODO: show error
                return;
            }

           /* if (mCursor.getCount() > 0) {
                getSupportActionBar().setTitle("Movies");// setTitle(R.string.videos_title);
            } else {
                getSupportActionBar().setTitle("Movies Not Found");// setTitle(R.string.no_videos_title);
            }*/

            String[] thumbColumns = { MediaStore.Video.Thumbnails.DATA,
                    MediaStore.Video.Thumbnails.VIDEO_ID };

            //TODO: parse cursors
            if(mCursor.moveToFirst()){
                do {

                    MovieDetail movieInfo = new MovieDetail();

                    int id = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Video.Media._ID));

                   Bitmap media = MediaStore.Video.Thumbnails.getThumbnail(activity.getContentResolver(),
                           id, MediaStore.Video.Thumbnails.MINI_KIND, null);
                    if (media != null) {
                        movieInfo.coverBitmap = media;
                    }else {
                        Log.d("thmbtmp", "null");
                    }
                    Cursor thumbCursor = resolver.query( MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                            thumbColumns, MediaStore.Video.Thumbnails.VIDEO_ID
                                    + " = " + id, null, null);

                    Log.d("thumb cursor:", thumbCursor.getCount()+"count");

                    if (thumbCursor.moveToFirst()) {
                        movieInfo.thumbPath = thumbCursor.getString(thumbCursor
                                .getColumnIndex(MediaStore.Video.Thumbnails.DATA));
                        Log.v("thumbpath", movieInfo.thumbPath);
                    }else {
                        Log.d("thumnail", "not found");
                        //Toast.makeText(activity,  "Thumbnail not found", Toast.LENGTH_SHORT).show();
                    }

                    movieInfo.filePath = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                    movieInfo.movieTitle = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                    movieInfo.movieArtist = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));
                    movieInfo.mimeType = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
                    movieInfo.movieDuration = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    movieInfo.movieDescription = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DESCRIPTION));

                    movieDetailArrayList.add(movieInfo);

                }while (mCursor.moveToNext());
            }


        }

    }


    public void showBook(MovieDetail movieDetail){
        Intent intent = new Intent(MoviesViewActivity.this,MovieDetailActivity.class);
        Bundle information = new Bundle();
        //information.putParcelable("bitm", bookDetail.bookBitmap);
        if (movieDetail.coverBitmap != null) {
            MyUsedData.getInstance().setMovieCoverBitmap(movieDetail.coverBitmap);
        }
        information.putSerializable("MovieDetail", movieDetail);

        intent.putExtras(information);
        startActivity(intent);

    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
        super.onDestroy();
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
