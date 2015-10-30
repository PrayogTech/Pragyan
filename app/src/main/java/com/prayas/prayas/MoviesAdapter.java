package com.prayas.prayas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by vivekanandjha on 21/10/15.
 */
public class MoviesAdapter extends ArrayAdapter<MovieDetail> {

    private Context mContext;
    private LayoutInflater inflater;

    public MoviesAdapter(Context context, ArrayList<MovieDetail> movies) {
        super(context, R.layout.movieitemlayout, movies);
        mContext = context;
        inflater = LayoutInflater.from(mContext);
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        MovieDetail item = this.getItem(position);

        TextView nametextView;
        ImageView previewImageView;
        TextView artistTextview;
        Spinner actionSpinner;
        TextView durationTextview;
        TextView mimeTextview;

        MoviesViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.movieitemlayout, null);
            nametextView = (TextView) convertView.findViewById(R.id.movieTitle);
            previewImageView = (ImageView)convertView.findViewById(R.id.preview_image);
            artistTextview = (TextView)convertView.findViewById(R.id.movieProducer);
            durationTextview = (TextView)convertView.findViewById(R.id.movieDuration);
            mimeTextview = (TextView)convertView.findViewById(R.id.movieMimeType);
           // actionSpinner = (Spinner)convertView.findViewById(R.id.actionSpinnerView);

            viewHolder = new MoviesViewHolder(nametextView, previewImageView, artistTextview, durationTextview, mimeTextview);
            convertView.setTag(viewHolder);
        }

        //TODO: set movie data

        // Reuse existing row view
        viewHolder = (MoviesViewHolder) convertView.getTag();
        viewHolder.nametextView.setText(item.movieTitle.trim());
        viewHolder.artistTextview.setText(item.movieArtist.trim());
        //60,000
        int sec = Integer.parseInt(item.movieDuration);
        int min = sec/60000;
      String  converted = String.format(Locale.ITALIAN, "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(item.movieDuration)),
                TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(item.movieDuration)) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(item.movieDuration)))
        );

        viewHolder.durationTextview.setText(converted);
        viewHolder.mimeTypeTextview.setText(item.movieArtist.trim());
        //Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.comic);
        //viewHolder.previewImageView.setImageBitmap(bmp);

        if (item.thumbPath != null) {
           viewHolder.previewImageView.setImageURI(Uri
                  .parse(item.thumbPath));
           /* Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(Uri.parse(item.thumbPath)));
                viewHolder.previewImageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }*/
            //imageView.setImageBitmap(bitmap);
        }else {
            Toast.makeText(mContext, "Thumb path empty", Toast.LENGTH_SHORT).show();
        }


        convertView.setTag(R.id.folder_holder, item);
        return  convertView;
    }

}
