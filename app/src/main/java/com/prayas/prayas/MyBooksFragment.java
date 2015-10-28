package com.prayas.prayas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class MyBooksFragment extends android.support.v4.app.Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    private BookGridAdapter bookGridAdapter;
    private GridView bookGridView;
    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<BookDetail> bookDetailArrayList = new ArrayList<>();

    boolean _areBooksLoaded = false;

    public static MyBooksFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        MyBooksFragment fragment = new MyBooksFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_books, container, false);

        this.inflater = inflater;


       // renderView();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        bookGridView = (GridView) view.findViewById(R.id.bookGridView);
        renderView();
       // super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !_areBooksLoaded ) {

            //renderView();
            _areBooksLoaded = true;
        }
    }

    private void renderView() {

        bookDetailArrayList.clear();
       // bookGridView = (GridView) getView().findViewById(R.id.bookGridView);
        ArrayList<UssageDetail> ussageList = MyUsedData.getInstance().getUsedDataList();
        if (ussageList.size() > 0) {
            Iterator<UssageDetail> iterator = ussageList.iterator();
            while (iterator.hasNext()) {
                UssageDetail ussageInfo = iterator.next();
                if (ussageInfo.dataType == "BOOK_PURCHASE") {
                    bookDetailArrayList.add(ussageInfo.bookInfo);
                }
            }
            if(bookDetailArrayList.size() > 0){
                bookGridAdapter = new BookGridAdapter(mContext,bookDetailArrayList);
                Log.d("fragment", bookGridAdapter.toString());
                Log.d("grid view", bookGridView.toString());
                bookGridView.setAdapter(bookGridAdapter);
                bookGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        BookDetail viewHolder = (BookDetail) view.getTag(R.id.folder_holder);
                        // File directory = new File(viewHolder.bookFilePath.getFile());
                        showAlertForPurchase(viewHolder);

                    }
                });
            }else{
                Toast.makeText(mContext, "You have not purchased any book", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(mContext, "You have not purchased any book", Toast.LENGTH_SHORT).show();
        }
    }


    public  void showAlertForPurchase(final BookDetail bookData){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);

        String message = "Open " + bookData.bookName + " to read !!";
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setPositiveButton("OPEN",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        File urlFile = new File(bookData.bookFilePath.getFile());
                        Uri uri = Uri.fromFile(urlFile.getAbsoluteFile());

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
}
