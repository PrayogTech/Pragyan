package com.prayas.prayas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.HashMap;

public class MyStoresActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    private Context mContext;

    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle;
    private String[] mStoreTitles;
    private String[] mAppTitles;
    private String[] mActivities;
    private  String[] mSectionTitles;
    private String[] mMenuItem;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private Toolbar toolbar;
    private DrawerAdapter mAdapter;

    private HashMap<String, String> drawerItems = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sidebar_layout);
        mTitle = "My Store";

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this;

        mActivities = new String[]{"My Store", "My Ussage"};
        mStoreTitles = new String[]{"Books", "Movies", "Games", "Food" };
        mAppTitles = new String[]{"About", "Send Feedback", "Contact" };
        mSectionTitles = new String []{"ACTIVITIES", "EXPLORE STORE", "PRAYAS"};

        mMenuItem = new String []{"My Store", "My Order","Books", "Movies", "Games", "Food","About", "Send Feedback", "Contact" };
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mAdapter = new DrawerAdapter(this);
        for (int i = 0; i < mSectionTitles.length; i++) {

            mAdapter.addSectionHeaderItem(mSectionTitles[i]);
            //drawerItems.put(mSectionTitles[i], SECTION);
            switch (i){
                case 0:
                    for (int item = 0; item < mActivities.length; item++) {
                        Log.d("item menu", mActivities[item]);
                        mAdapter.addItem(mActivities[item]);
                        // drawerItems.put(mActivities[item], mActivities[item]);
                    }
                    break;
                case 1:
                    for (int item = 0; item < mStoreTitles.length; item++) {
                        mAdapter.addItem(mStoreTitles[item]);
                        //drawerItems.put(mStoreTitles[item], mStoreTitles[item]);
                    }
                    break;
                case 2:
                    for (int item = 0; item < mAppTitles.length; item++) {
                        mAdapter.addItem(mAppTitles[item]);
                        // drawerItems.put(mAppTitles[item], mAppTitles[item]);
                    }
                    break;
                default:
                    break;
            }

        }

        // Set the adapter for the list view
        // mAdapter = new ArrayAdapter<String>(this, R.layout.drawer_list_item, mStoreTitles);
        Log.d("log", "test" + mAdapter + "/////" + mDrawerList);
        mDrawerList.setAdapter(mAdapter);
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());


        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                getDelegate().getSupportActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                getDelegate().getSupportActionBar().setTitle(mTitle);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getDelegate().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getDelegate().getSupportActionBar().setHomeButtonEnabled(true);


        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        StoreFragmentPagerAdapter pagerAdapter = new StoreFragmentPagerAdapter(getSupportFragmentManager(), MyStoresActivity.this);
                //new StoreFragmentPagerAdapter(getSupportFragmentManager(), MyStoresActivity.this);
        viewPager.setAdapter(pagerAdapter);

        // Give the TabLayout the ViewPager
         tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }
    }



    public static String POSITION = "POSITION";

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, tabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        viewPager.setCurrentItem(savedInstanceState.getInt(POSITION));
    }


    /*
    net
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }else {

            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position, String menuItem) {
        // Toast.makeText(this, R.string.app_name, Toast.LENGTH_SHORT).show();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        //  setTitle(mMenuItem[position]);
        mDrawerLayout.closeDrawer(mDrawerList);

        Log.d("position", position + "");
        Intent intent;
        Bundle information = new Bundle();
        switch (menuItem) {
            case "My Store":

                break;
            case "My Ussage":
                intent = new Intent(MyStoresActivity.this,MyOrdersActivity.class);

                intent.putExtras(information);
                startActivity(intent);
                break;
            case "Books":
                intent = new Intent(MyStoresActivity.this,MainActivity.class);

                intent.putExtras(information);
                startActivity(intent);
                break;
            case "Movies":
                intent  = new Intent(MyStoresActivity.this,MoviesViewActivity.class);

                intent.putExtras(information);
                startActivity(intent);
                break;
            case "Games":
                intent = new Intent(MyStoresActivity.this,GamesViewActivity.class);

                intent.putExtras(information);
                startActivity(intent);
                break;
            case "Food":
                String packageName = "com.cymaxtec.restomenu";
                intent = getPackageManager().getLaunchIntentForPackage(packageName);
                if(intent != null) {
                    startActivity(intent);
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        // TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        //toolbarTitle.set
        getDelegate().getSupportActionBar().setTitle(mTitle);
        // getSupportActionBar().setTitle(mTitle);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {

            String item = (String) view.getTag(R.id.folder_holder);
            selectItem(position, item);
        }
    }
}


