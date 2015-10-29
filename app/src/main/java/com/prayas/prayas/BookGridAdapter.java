package com.prayas.prayas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kokila on 16/10/15.
 */
public class BookGridAdapter extends ArrayAdapter<BookDetail> {
    private Context mContext;
   private LayoutInflater inflater;
//    private LayoutInflater inflater;

    public BookGridAdapter(Context context, ArrayList<BookDetail> books) {
        super(context, R.layout.book_grid, books);
        mContext = context;
        inflater = LayoutInflater.from(mContext);

        //RobotoLight = Typeface.createFromAsset(mContext.getAssets(),"Roboto-Light.ttf");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        BookDetail item = this.getItem(position);

        TextView nametextView;
        ImageView iconimageView;
        TextView authorTextview;

        BookViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.book_grid, null);
            nametextView = (TextView) convertView.findViewById(R.id.book_name);
            iconimageView = (ImageView)convertView.findViewById(R.id.book_image);
            authorTextview = (TextView)convertView.findViewById(R.id.book_author);

            viewHolder = new BookViewHolder(nametextView, iconimageView, authorTextview);
            convertView.setTag(viewHolder);
        }

        //set data
        // Reuse existing row view
        viewHolder = (BookViewHolder) convertView.getTag();

        viewHolder.nametextView.setText(item.bookName);
        viewHolder.authorTextview.setText(item.authorName);
       // Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), item.bookIcon);
       // Bitmap bmp = BitmapFactory.decodeByteArray(item.byteArray, 0, item.byteArray.length);
        viewHolder.iconimageView.setImageBitmap(item.bookBitmap);
        //(item.getFile_path().getDensitySizeForIcon(mC
       // viewHolder.iconimageView.setBackground(R.drawable.comic);

        convertView.setTag(R.id.folder_holder, item);
        return convertView;
    }
}
