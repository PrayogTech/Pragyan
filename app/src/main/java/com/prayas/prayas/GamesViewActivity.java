package com.prayas.prayas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class GamesViewActivity extends AppCompatActivity {

    private Activity activity;

    private ListView gamesListView;

    ArrayList<AppInfo> appInfoArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_view);

        getSupportActionBar().setTitle("Available Games");
        Drawable drawable;
        // int decode = Integer.decode("3F51B5");
        drawable = new ColorDrawable(0xFF3F51B5);
        getSupportActionBar().setBackgroundDrawable(drawable);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        activity = this;

        renderAvailableGames();
        //new FetchList().execute();
        gamesListView = (ListView) findViewById(R.id.gamesListView);

        GamesAdapter gamesAdapter = new GamesAdapter(activity, appInfoArrayList);
        gamesListView.setAdapter(gamesAdapter);
        gamesListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //TODO: on click
            }
        });
    }

    public  void renderAvailableGames(){

        PackageManager packageManager = this.getPackageManager();
        List<PackageInfo> applist = packageManager.getInstalledPackages(0);
        Iterator<PackageInfo> iterator = applist.iterator();
        while (iterator.hasNext()) {
            PackageInfo pk = iterator.next();
            if(!isSystemPackage(pk)){
                String appName = pk.applicationInfo.loadLabel(packageManager).toString();
                String appPackage = pk.applicationInfo.packageName.toString();
                Drawable appIcon = pk.applicationInfo.loadIcon(packageManager);
                String urlToGetInfo = "http://googleplay-jsapi.herokuapp.com/app/" + appPackage;
Log.d("app ifo", urlToGetInfo);
                fechFromVoley(urlToGetInfo);
               // fetchGamesData(urlToGetInfo);
                String appId = "";
                String appPrice = "5 Rs";
            AppInfo appInfo = new AppInfo(appId, appName, appPackage, appIcon,appPrice);
                //TODO: check for app category
                appInfoArrayList.add(appInfo);
            }
        }
    }

    /**
     * Return whether the given PackgeInfo represents a system package or not.
     * User-installed packages (Market or otherwise) should not be denoted as
     * system packages.
     *
     * @param pkgInfo
     * @return
     */
    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    private void  fechFromVoley(String url){
      //  String url = "http://httpbin.org/get?site=code&network=tutsplus";

       // String url = "http://httpbin.org/html";

// Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // Result handling
                        System.out.println(response.substring(0,100));

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Error handling
                System.out.println("Something went wrong!");
                error.printStackTrace();

            }
        });

// Add the request to the queue
        Volley.newRequestQueue(this).add(stringRequest);
       /* JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        try {
                            System.out.print("repsonse:" + response);
                            String icon = response.getString("icon");
                            //String site = response.getString("site"),
                              //      network = response.getString("network");
                            System.out.println( "+icon"+icon);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Log.d("error volley", error.getLocalizedMessage());
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);
*/

    }

    private void fetchGamesData(String urlString){
        HttpURLConnection urlConnection = null;
        URL url = null;
        JSONObject object = null;
        InputStream inStream = null;
        try {
            url = new URL(urlString.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.connect();
            inStream = urlConnection.getInputStream();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
            String temp, response = "";
            while ((temp = bReader.readLine()) != null) {
                response += temp;
            }
            object = (JSONObject) new JSONTokener(response).nextValue();
            Log.d("object out", object.toString());
        } catch (Exception e) {
            Log.d("exc", e.getLocalizedMessage());
            //this.mException = e;
        } finally {
            if (inStream != null) {
                try {
                    // this will close the bReader as well
                    inStream.close();
                } catch (IOException ignored) {
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }
    private class  FetchList extends AsyncTask<String, Void, List<String>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);
            Log.d("on post", "exe");

        }

        @Override
        protected List<String> doInBackground(String... params) {
            renderAvailableGames();
            return null;
        }
    }

    public  void showAlertForPurchase(final AppInfo appInfo){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);

        String message = "Purchase " + appInfo.appName + " for just " + appInfo.appPrice + " !!";
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setPositiveButton("BUY",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        ArrayList<UssageDetail> ussageList = MyUsedData.getInstance().getUsedDataList();
                        Boolean purchaseStatus = false;

                        if (ussageList.size() > 0) {
                            Iterator<UssageDetail> iterator = ussageList.iterator();
                            while (iterator.hasNext()) {
                                UssageDetail ussageInfo = iterator.next();
                                if (ussageInfo.ussageId == appInfo.appId && ussageInfo.dataType == "GAMES_PURCHASE") {
                                    purchaseStatus = true;
                                    Toast.makeText(activity, "You have already purchased this game", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                        }
                        if (!purchaseStatus) {
                            UssageDetail moviePurchaseData = new UssageDetail();
                            moviePurchaseData.ussageId = appInfo.appId;
                            moviePurchaseData.dataType = "GAMES_PURCHASE";
                            moviePurchaseData.dataMessage = "You have purchased " + appInfo.appName + "at " + appInfo.appPrice;
                            Date dateobj = new Date();
                            moviePurchaseData.orderDate = dateobj;

                            //Set in my ussage
                            MyUsedData.getInstance().getUsedDataList().add(moviePurchaseData);

                            //TODO Start
                        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_games_view, menu);
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

    private  class AppInfo{

        String appId;
        String appName;
        String appPackageName;
        Drawable appIcon;
        String appPrice;

        public AppInfo(String appId, String appName, String appPackageName, Drawable appIcon, String appPrice) {
            this.appId = appId;
            this.appName = appName;
            this.appPackageName = appPackageName;
            this.appIcon = appIcon;
            this.appPrice = appPrice;
        }

    }

    private class AppViewHolder{

        TextView appNameTextView;
        ImageView appIconImageView;

        public AppViewHolder(TextView appNameTextView, ImageView appIconImageView) {
            this.appNameTextView = appNameTextView;
            this.appIconImageView = appIconImageView;
        }
    }

    private class GamesAdapter extends ArrayAdapter<AppInfo>{

        private Context mContext;
        private LayoutInflater inflater;

        public GamesAdapter(Context context, ArrayList<AppInfo> games) {
            super(context, R.layout.gameitemlayout, games);
            mContext = context;
            inflater = LayoutInflater.from(mContext);
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            AppInfo item = this.getItem(position);

            TextView nametextView;
            ImageView previewImageView;

            AppViewHolder viewHolder;

            if(convertView == null){
                convertView = inflater.inflate(R.layout.gameitemlayout, null);
                nametextView = (TextView) convertView.findViewById(R.id.appNameTextview);
                previewImageView = (ImageView)convertView.findViewById(R.id.appImageView);

                viewHolder = new AppViewHolder(nametextView, previewImageView);
                convertView.setTag(viewHolder);
            }

            // Reuse existing row view
            viewHolder = (AppViewHolder) convertView.getTag();
            viewHolder.appNameTextView.setText(item.appName.trim());
            viewHolder.appIconImageView.setImageDrawable(item.appIcon);

            convertView.setTag(R.id.folder_holder, item);
            return  convertView;
        }

        }

}
