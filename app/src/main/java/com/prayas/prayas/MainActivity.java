package com.prayas.prayas;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.content.res.Configuration;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
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

import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;


import com.itextpdf.awt.geom.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import  com.itextpdf.*;
import com.itextpdf.xmp.XMPException;
import com.itextpdf.xmp.XMPMeta;
import com.itextpdf.xmp.XMPMetaFactory;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFParser;

import java.io.ByteArrayOutputStream;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;


public class MainActivity extends AppCompatActivity {

    private ArrayList<BookDetail> bookDetailArrayList = new ArrayList<>();

    private BookGridAdapter bookGridAdapter;
    private GridView bookGridView;

    Activity activity;

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

    GetBooksAsyncTask fetchBooksAsynch;
   // private static final String SECTION = "SECTION";
   // private static final String ITEM  = "ITEM";

    @Override
    protected void onResume() {
        super.onResume();
        setTitle("Books");
    }

    @Override
    protected void onStop() {
        super.onStop();
        //if previous task running, cancel
        if(fetchBooksAsynch != null && fetchBooksAsynch.getStatus() == android.os.AsyncTask.Status.RUNNING){
            fetchBooksAsynch.cancel(true);
            fetchBooksAsynch = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sidebar_layout);

        mTitle = "Books";
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this;

        /*mActivities = new String[]{"My Store", "My Ussage"};
        mStoreTitles = new String[]{"Books", "Movies", "Games", "Food" };
        mAppTitles = new String[]{"About", "Send Feedback", "Contact" };
        mSectionTitles = new String []{"ACTIVITIES", "EXPLORE STORE", "PRAYAS"};

        mMenuItem = new String []{"My Store", "My Order","Books", "Movies", "Games", "Food","About", "Send Feedback", "Contact" };
*/
        mActivities = new String[]{ "My Ussage"};
        mStoreTitles = new String[]{"Books", "Movies", "Food" };
        mAppTitles = new String[]{"Admin Settings"};
        mSectionTitles = new String []{"ACTIVITIES", "EXPLORE STORE", "FUN SPACE"};

        mMenuItem = new String []{ "My Order","Books", "Movies", "Food","Admin Settings"};
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

      /*  getSupportActionBar().setTitle("Books");
        Drawable drawable;
        // int decode = Integer.decode("3F51B5");
        drawable = new ColorDrawable(0xFF3F51B5);
        getSupportActionBar().setBackgroundDrawable(drawable);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
*/
        activity = this;

        bookGridView = (GridView)findViewById(R.id.grid);

        loadBookGridView();
    }


    public  void  loadBookGridView(){
        if(fetchBooksAsynch !=null){

        }else {
            fetchBooksAsynch = new GetBooksAsyncTask(this);
            fetchBooksAsynch.execute();
        }

    }

     class GetBooksAsyncTask extends AsyncTask<String, String, String>{

         /** progress dialog to show user that the backup is processing. */
         private ProgressDialog dialog;
         /** application context. */
         private MainActivity activity;

         public GetBooksAsyncTask(MainActivity mActivity) {
             activity = mActivity;
             dialog = new ProgressDialog(activity);
         }

         @Override
         protected void onPreExecute() {
             dialog.setMessage("Fetching Books...");
             dialog.show();
         }

         @Override
         protected String doInBackground(String... params) {

             getBooksFromDevice();
             return null;
         }

         @Override
         protected void onPostExecute(String s) {
             super.onPostExecute(s);

             if (dialog.isShowing()) {
                 dialog.dismiss();
             }

             if (bookDetailArrayList.size() > 0){

                 if (bookGridAdapter == null){
                     bookGridAdapter = new BookGridAdapter(activity, bookDetailArrayList);
                     bookGridView.setAdapter(bookGridAdapter);
                     bookGridView.setOnItemClickListener(new OnItemClickListener() {

                         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                             BookDetail viewHolder = (BookDetail) view.getTag(R.id.folder_holder);
                             // File directory = new File(viewHolder.bookFilePath.getFile());
                             showBook(viewHolder);

                         }
                     });
                 }else {
                   bookGridAdapter = (BookGridAdapter) bookGridView.getAdapter();
                     bookGridAdapter.notifyDataSetChanged();
                 }
             }else {
                 Toast.makeText(activity, "Books Not Available", Toast.LENGTH_SHORT).show();
             }
         }
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
                        Log.d("file name", listFile[i].getName());
                        File sampleDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PrayasBookSample/" + listFile[i].getName() );

                        try {

                            URL url = directory.toURI().toURL();
                            Log.d("url Path", url.getPath());
                            //getPDFMetaData(url, directory, sampleDirectory);

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }


                    }else if(listFile[i].getName().endsWith(epubPattern)){
                        File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PrayasBook/" + listFile[i].getName() );

                        File sampleDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PrayasBookSample/" + listFile[i].getName() );

                        URL url = null;
                        try {
                            url = directory.toURI().toURL();
                            Log.d("url Path", url.getPath());

                            getBookMetaDataFromPdf(directory.getAbsolutePath(), directory, sampleDirectory);
                            Log.d("file name", listFile[i].getName());
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }
    }

    public void getBookMetaDataFromPdf(String bookFile, File directory, File sampleDirectory){

        try
        {
            FileInputStream fileInputStream = new FileInputStream(bookFile);

                Book book = (new EpubReader()).readEpub(fileInputStream);
               // Log.d("book", book.getMetadata().getFormat());
               // Log.d("book", String.valueOf(book.getMetadata().getFirstTitle()));
                //Log.d("book", String.valueOf(book.getMetadata().getAuthors()));
                BookDetail bookD = new BookDetail();
            if (book.getMetadata().getFirstTitle() != null && !book.getMetadata().getFirstTitle().equals("")) {
                bookD.bookName = String.valueOf(book.getMetadata().getFirstTitle());//"Book Name";
            }

            if(book.getMetadata().getAuthors().size() > 0) {
                bookD.authorName = String.valueOf(book.getMetadata().getAuthors().toString());//"Author";
            }
            if(book.getMetadata().getDescriptions().size() > 0) {
                bookD.bookDescription = String.valueOf(book.getMetadata().getDescriptions().toString());//"Deatialed Book";
            }  //  bookD.bookIcon = R.drawable.comic;

            // Log the book's coverimage property

            Bitmap coverImage = BitmapFactory.decodeStream(book.getCoverImage().getInputStream());
           // Drawable drawable = new BitmapDrawable(getResources(), coverImage);

            //ByteArrayOutputStream stream = new ByteArrayOutputStream();
            //coverImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
            //byte[] byteArray = stream.toByteArray();

            bookD.bookBitmap = coverImage;
            bookD.bookPrice = "5 Rs";
            if (book.getMetadata().getIdentifiers().size() > 0) {
                bookD.bookISBN = String.valueOf(book.getMetadata().getIdentifiers().get(0));//"ISBN Number";
            }
           // bookD.bookPublishedDate = "22/3/15";
            if (book.getMetadata().getPublishers().size() > 0) {
                bookD.bookPublisher = String.valueOf(book.getMetadata().getPublishers().get(0)); //"Vipul and Co.";
            }
                //File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PrayasBook/" + listFile[i].getName() );

                URL url = directory.toURI().toURL();
                bookD.bookFilePath = url; //directory.getAbsolutePath(); //listFile[i].getName();
                bookD.bookDiretory = directory.getAbsolutePath();

                URL urlSample = sampleDirectory.toURI().toURL();
                bookD.bookSampeFilePath = urlSample;
                bookD.bookSampleDirectory = sampleDirectory.getAbsolutePath();

                // bookD.bookFilePath = directory.getAbsolutePath(); //listFile[i].getName();
                bookDetailArrayList.add(bookD);

        }catch (IOException e){
            Log.d("book", "exception");
        }

    }

    public void getPDFMetaData(URL bookFile, File directory, File sampleDirectory){


        //storage/emulated/0/PrayasBook/Swift%20Quick%20Syntax%20Reference.pdf
        try {
            Log.d("url name", bookFile.toString()+ "::file::"+ directory.toString());
            PdfReader reader = new PdfReader(bookFile);

            RandomAccessFile raf = new RandomAccessFile(directory, "r");

            FileChannel channel = raf.getChannel();

           net.sf.andpdf.nio.ByteBuffer buf = net.sf.andpdf.nio.ByteBuffer.NEW(channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size()));
            PDFFile pdffile = new PDFFile(buf);

            // draw the first page to an image
            PDFPage page = pdffile.getPage(0);

            Log.d("metadata", "check"+page.getBBox());
            //get the width and height for the doc at the default zoom
            Rectangle rect = new Rectangle(0,0,
                    (int)page.getBBox().width(),
                    (int)page.getBBox().height());

            //generate the image
//            Bitmap img = page.getImage((int) rect.width, (int) rect.height, null, true, true);

            Log.d("metadata", "check2");
            if (reader.getMetadata() != null){
                String st = new String(reader.getMetadata());
                Log.d("metadata:", st);
            }
            //
            BookDetail bookD = new BookDetail();
            Map info = reader.getInfo();
            for (Iterator i = info.keySet().iterator(); i.hasNext();) {
                String key = (String) i.next();
                String value = (String) info.get(key);
                if(key.equals("Author")){
                    bookD.authorName = value;
                }else if(key.equals("Title")){
                    bookD.bookName = value;
                }

               System.out.println(key + ": " + value);
            }

           // Log.d("reader", reader.getMetadata().toString()+"hj"+st);
            //Log.d("catalog", reader.getCatalog() + "jhf");
          //  System.out.println("reader.getPageResources(0)"+reader.getPageResources(0));

           // bookD.bookName = "Book Name";
            //bookD.authorName = "Vijay Rastogi";
            bookD.bookDescription = "Comic Book";
            bookD.bookIcon = R.drawable.comic;
            bookD.bookPrice = "5 Rs";
            bookD.bookISBN = "ISBN Number";
           // bookD.bookPublishedDate = "22/3/15";
            bookD.bookPublisher = "Vipul and Co.";
            //File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PrayasBook/" + listFile[i].getName() );

            URL url = directory.toURI().toURL();
            bookD.bookFilePath = url; //directory.getAbsolutePath(); //listFile[i].getName();
            bookD.bookDiretory = directory.getAbsolutePath();

            URL urlSample = sampleDirectory.toURI().toURL();
            bookD.bookSampeFilePath = urlSample;
            bookD.bookSampleDirectory = sampleDirectory.getAbsolutePath();

            bookDetailArrayList.add(bookD);
           // reader.getMetadata();
        } catch (IOException e) {
            Log.d("url exception", bookFile.toString());
            e.printStackTrace();
        }

    }

    public void showBook(BookDetail bookDetail){
        Intent intent = new Intent(MainActivity.this,BookDetailActivity.class);
        Bundle information = new Bundle();
//information.putParcelable("bitm", bookDetail.bookBitmap);
        MyUsedData.getInstance().setBookCoverBitmap(bookDetail.bookBitmap);
        information.putSerializable("BookDetail", bookDetail);

        intent.putExtras(information);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       /* int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }*/
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
                intent = new Intent(MainActivity.this,MyStoresActivity.class);

                intent.putExtras(information);
                startActivity(intent);
                break;
            case "My Ussage":
                intent = new Intent(MainActivity.this,MyOrdersActivity.class);

                intent.putExtras(information);
                startActivity(intent);
                break;
            case "Books":

                break;
            case "Movies":
                intent  = new Intent(MainActivity.this,MoviesViewActivity.class);

                intent.putExtras(information);
                startActivity(intent);
                break;
            case "Games":
                intent = new Intent(MainActivity.this,GamesViewActivity.class);

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
