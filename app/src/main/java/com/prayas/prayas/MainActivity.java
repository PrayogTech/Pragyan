package com.prayas.prayas;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.pdf.PdfReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;


public class MainActivity extends AppCompatActivity {

    private ArrayList<BookDetail> bookDetailArrayList = new ArrayList<>();

    private BookGridAdapter bookGridAdapter;
    private GridView bookGridView;
    private Context mContext;

    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private Toolbar toolbar;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        setTitle("Books");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sidebar_layout);
        mTitle = "Books";

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this;

        mPlanetTitles = new String[]{"Books", "Movies", "Games", "Food", "Social"};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        Log.d("log", "test" + mPlanetTitles.length);

        // Set the adapter for the list view
        mAdapter = new ArrayAdapter<String>(this, R.layout.drawer_list_item, mPlanetTitles);
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

        bookGridView = (GridView)findViewById(R.id.grid);

        loadBookGridView();
    }


    public  void  loadBookGridView(){


        getBooksFromDevice();

        bookGridAdapter = new BookGridAdapter(mContext,bookDetailArrayList);
        bookGridView.setAdapter(bookGridAdapter);
        bookGridView.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                BookDetail viewHolder = (BookDetail) view.getTag(R.id.folder_holder);
                File directory = new File(viewHolder.bookFilePath);
                showBook(directory);

            }
        });

    }

    public void getBooksFromDevice(){

        File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PrayasBook");

        if(!directory.exists()) {
            directory.mkdirs();
            //createFolder(m_applicationDir + "/FunnyB/public/"+ publicFolderNames[i], publicFolderNames[i]);
        }
        this.walkdir(directory);

    }

    public void walkdir(File dir) {
        String pdfPattern = ".pdf";
        String epubPattern = ".epub";

        File listFile[] = dir.listFiles();

        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    walkdir(listFile[i]);
                } else {
                    if (listFile[i].getName().endsWith(pdfPattern)){
                        //Do what ever u want
                        File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PrayasBook/" + listFile[i].getName() );
                        getPDFMetaData(directory.getAbsolutePath(), directory);
                        //getBookMetaDataFromPdf(directory.getAbsolutePath(), directory);
                        Log.d("file name", listFile[i].getName());


                    }else if(listFile[i].getName().endsWith(epubPattern)){
                        File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PrayasBook/" + listFile[i].getName() );

                        getBookMetaDataFromPdf(directory.getAbsolutePath(), directory);
                        Log.d("file name", listFile[i].getName());
                    }
                }
            }
        }
    }

    public void getBookMetaDataFromPdf(String bookFile, File directory){


        try (FileInputStream fileInputStream = new FileInputStream(bookFile)) {

            Book book = (new EpubReader()).readEpub(fileInputStream);
            Log.d("book", book.getMetadata().getFormat());
            Log.d("book", String.valueOf(book.getMetadata().getTitles()));
            Log.d("book", String.valueOf(book.getMetadata().getAuthors()));
            BookDetail bookD = new BookDetail();
            bookD.bookName = String.valueOf(book.getMetadata().getTitles());//"Book Name";
            bookD.authorName = String.valueOf(book.getMetadata().getAuthors());//"Author";
            bookD.bookDescription = "Deatialed Book";
            bookD.bookIcon = R.drawable.comic;

            //File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PrayasBook/" + listFile[i].getName() );

            bookD.bookFilePath = directory.getAbsolutePath(); //listFile[i].getName();
            bookDetailArrayList.add(bookD);
        }catch (IOException e){
            Log.d("book", "exception");
        }

    }

    public void getPDFMetaData(String bookFile, File directory){
        try {
            PdfReader reader = new PdfReader(bookFile);
            String st = new String(reader.getMetadata());
            Map info = reader.getInfo();
            for (Iterator i = info.keySet().iterator(); i.hasNext();) {
                String key = (String) i.next();
                String value = (String) info.get(key);
                System.out.println(key + ": " + value);
            }
            Log.d("reader", reader.getMetadata().toString()+"hj"+st);
            BookDetail bookD = new BookDetail();
            bookD.bookName = "j";
            bookD.authorName = "uy";
            bookD.bookDescription = "Deatialed Book";
            bookD.bookIcon = R.drawable.comic;

            //File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PrayasBook/" + listFile[i].getName() );

            bookD.bookFilePath = directory.getAbsolutePath(); //listFile[i].getName();
            bookDetailArrayList.add(bookD);
           // reader.getMetadata();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showBook(File bookPath){
        PackageManager packageManager = getPackageManager();

        Intent   testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");


        List list = packageManager.queryIntentActivities(testIntent,
                PackageManager.MATCH_DEFAULT_ONLY);

       // Intent intent = new Intent();
       // intent.setAction(Intent.ACTION_VIEW);

       // File fileToRead = new File(
            //    "/data/data/com.example.filedownloader/app_books/Book.pdf");
        Uri uri = Uri.fromFile(bookPath.getAbsoluteFile());

        Intent intent = new Intent(Intent.ACTION_VIEW);
         intent.setDataAndType(uri, "application/epub+zip");
       // intent.setDataAndType(uri, "application/pdf");
        startActivity(intent);
    }

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
    private void selectItem(int position) {
        Toast.makeText(this, R.string.app_name, Toast.LENGTH_SHORT).show();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);

        Log.d("position", position + "");

        switch (position) {
            case 0:
                Intent intent = new Intent(MainActivity.this,BookDetailActivity.class);
                Bundle information = new Bundle();

                intent.putExtras(information);
                startActivity(intent);
                break;
            case 1:
                break;
            case 2:
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
            selectItem(position);
        }
    }


}
