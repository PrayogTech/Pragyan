package com.prayas.prayas;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by kokila on 23/10/15.
 */
public class StoreFragmentPagerAdapter extends FragmentPagerAdapter {
    //private String tabTitles[] = new String[] { "Tab1", "Tab2" };
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "BOOKS", "MOVIES ", "GAMES" };
    private Context context;

    public StoreFragmentPagerAdapter(android.support.v4.app.FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        switch (position){
            case 0:
                return MyBooksFragment.newInstance(position + 1);
            case 1:
                return MyMoviesFragment.newInstance(position + 1);

            case 2:
                return MyGamesFragment.newInstance(position + 1);

            default:
                break;
        }
        return MyBooksFragment.newInstance(position + 1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
       // Toast.makeText(context, "Title"+ tabTitles[position], Toast.LENGTH_SHORT).show();
        return tabTitles[position];
    }
    public View getTabView(int position) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View v = LayoutInflater.from(context).inflate(R.layout.tabrowlayout, null);
        TextView tv = (TextView) v.findViewById(R.id.tabtitle);
        tv.setText(tabTitles[position]);
        //ImageView img = (ImageView) v.findViewById(R.id.imgView);
        //img.setImageResource(imageResId[position]);
        return v;
    }
}
