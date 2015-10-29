package com.prayas.prayas;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MyOrdersActivity extends AppCompatActivity {

    Activity activity;
    ListView orderListView;

    ArrayList<OrderInfo> orderInfoArrayList = new ArrayList<>();


    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        getSupportActionBar().setTitle("My Order History");
        Drawable drawable;
        // int decode = Integer.decode("3F51B5");
        drawable = new ColorDrawable(0xFF3F51B5);
        getSupportActionBar().setBackgroundDrawable(drawable);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        activity = this;

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        renderOrderList();
    }

    public void renderOrderList(){

       // ArrayList<UssageDetail> ussageDetailArrayList =  MyUsedData.getInstance().getUsedDataList();


        Gson gson = new Gson();

       String jsonCartList =  sharedpreferences.getString("order", "");
        if (!jsonCartList.equals("")) {
            //ArrayList ussageDetailArrayList = gson.fromJson(jsonCartList, ArrayList.class);
            Type t = new TypeToken<List<UssageDetail>>() {}.getType();
            ArrayList<UssageDetail> ussageDetailArrayList =  (ArrayList<UssageDetail>)gson.fromJson(jsonCartList, t);
            Log.d("mook ", ussageDetailArrayList.toString());
            Log.d("ussageDetail", ussageDetailArrayList.toString());
            Iterator iterator = ussageDetailArrayList.iterator();
            while (iterator.hasNext()) {

                UssageDetail detail = (UssageDetail) iterator.next();
              //  Log.d("ussage", detail.bookInfo.bookName);
                OrderInfo orderInfo = new OrderInfo(detail.dataType, detail.dataMessage, detail.orderDate);
                orderInfoArrayList.add(orderInfo);
            }
            if (orderInfoArrayList.size() > 0) {
                orderListView = (ListView) findViewById(R.id.orderListView);
                OrderAdapter gamesAdapter = new OrderAdapter(activity, orderInfoArrayList);
                orderListView.setAdapter(gamesAdapter);
            } else {
                Toast.makeText(activity, "You have not purchased Anything", Toast.LENGTH_LONG).show();
            }
        }else {

            Toast.makeText(activity, "You have not purchased Anything", Toast.LENGTH_LONG).show();
        }
    }

    private  class OrderInfo{

        String orderTag;
        String orderMessage;
        Date orderDate;

        public OrderInfo(String orderTag, String orderMessage, Date orderDate) {
            this.orderTag = orderTag;
            this.orderMessage = orderMessage;
            this.orderDate = orderDate;
        }
    }

    private  class OrderViewHolder{

        TextView orderTagTextView;
        TextView orderMsgTextView;
        TextView orderDateTextView;

        public OrderViewHolder(TextView orderTagTextView, TextView orderMsgTextView, TextView orderDateTextView) {
            this.orderTagTextView = orderTagTextView;
            this.orderMsgTextView = orderMsgTextView;
            this.orderDateTextView = orderDateTextView;
        }
    }

    private class  OrderAdapter extends ArrayAdapter<OrderInfo>{
        private Context mContext;
        private LayoutInflater inflater;

        public OrderAdapter(Context context, ArrayList<OrderInfo> orderInfo) {
            super(context, R.layout.gameitemlayout, orderInfo);
            mContext = context;
            inflater = LayoutInflater.from(mContext);
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            OrderInfo item = this.getItem(position);

            TextView orderTagTextView;
            TextView orderMsgTextView;
            TextView orderDateTextView;


            OrderViewHolder viewHolder;

            if(convertView == null){
                convertView = inflater.inflate(R.layout.orderitemlayout, null);
                orderTagTextView = (TextView) convertView.findViewById(R.id.tagTextView);
                orderMsgTextView = (TextView) convertView.findViewById(R.id.orderMessageTextView);
                orderDateTextView = (TextView) convertView.findViewById(R.id.orderDateTextView);

                viewHolder = new OrderViewHolder(orderTagTextView,orderMsgTextView, orderDateTextView);
                convertView.setTag(viewHolder);
            }

            // Reuse existing row view
            viewHolder = (OrderViewHolder) convertView.getTag();
            viewHolder.orderTagTextView.setText(item.orderTag);
            viewHolder.orderMsgTextView.setText(item.orderMessage);
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String reportDate = df.format(item.orderDate);
            viewHolder.orderDateTextView.setText(reportDate);

            convertView.setTag(R.id.folder_holder, item);
            return  convertView;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_orders, menu);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
