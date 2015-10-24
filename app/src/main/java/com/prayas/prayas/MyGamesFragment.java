package com.prayas.prayas;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyGamesFragment extends android.support.v4.app.Fragment {


    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    public static MyGamesFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        MyGamesFragment fragment = new MyGamesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_games, container, false);
//        TextView textView = (TextView) view;
        //      textView.setText("Fragment #" + mPage);
        return view;
    }


}
