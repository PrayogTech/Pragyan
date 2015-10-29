package com.prayas.prayas;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyMoviesFragment extends android.support.v4.app.Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    private Context mContext;

    private ListView moviesListView;
    private ArrayList<MovieDetail> movieDetailArrayList = new ArrayList<>();

    private  MoviesAdapter moviesAdapter;

    boolean _areMoviesLoaded = false;

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    public static MyMoviesFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        MyMoviesFragment fragment = new MyMoviesFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_movies, container, false);



        //renderView();
//        TextView textView = (TextView) view;
        //      textView.setText("Fragment #" + mPage);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        moviesListView = (ListView) view.findViewById(R.id.movieListView);
        renderView();
       // super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !_areMoviesLoaded ) {
          //  renderView();
            _areMoviesLoaded = true;
        }
    }

    private  void  renderView(){

        movieDetailArrayList.clear();
      //  ArrayList<UssageDetail> ussageList = MyUsedData.getInstance().getUsedDataList();
        sharedpreferences = mContext.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();

        String jsonCartList =  sharedpreferences.getString("order", "");
        if (!jsonCartList.equals("")) {
            Type t = new TypeToken<List<UssageDetail>>() {}.getType();
            ArrayList<UssageDetail> ussageList =  (ArrayList<UssageDetail>)gson.fromJson(jsonCartList, t);
            Log.d("mook ", ussageList.toString());

                Iterator<UssageDetail> iterator = ussageList.iterator();
                while (iterator.hasNext()) {
                    UssageDetail ussageInfo = iterator.next();
                    if (ussageInfo.dataType == "MOVIE_PURCHASE") {
                        movieDetailArrayList.add(ussageInfo.movieDetail);
                    }
                }
                if (movieDetailArrayList.size() > 0) {
                    moviesAdapter = new MoviesAdapter(mContext, movieDetailArrayList);

                    moviesListView.setAdapter(moviesAdapter);
                    moviesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //TODO: TBD
                            MovieDetail viewHolder = (MovieDetail) view.getTag(R.id.folder_holder);
                            showAlertForPurchase(viewHolder);

                        }
                    });
                } else {
                    Toast.makeText(mContext, "You have not watched any movies", Toast.LENGTH_SHORT).show();
                }
        }
    }

    public  void showAlertForPurchase(final MovieDetail movieDetail){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);

        String message = "Watch " + movieDetail.movieTitle;
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setPositiveButton("OPEN",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                        File newFile = new File(movieDetail.filePath);
                        intent.setDataAndType(Uri.fromFile(newFile), movieDetail.mimeType);
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
