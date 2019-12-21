package edu.depaul.stockwatch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener  {

    private static final String TAG = "MainActivity";
    private List<Stock>stockList = new ArrayList<>();  // Main content is here
    private List<String>symbolList=new ArrayList<String>(); //list of symbols is stored in bundle and used for reloading data
    private RecyclerView recyclerView; // Layout's recyclerview
    private SwipeRefreshLayout swiper; // The SwipeRefreshLayout
    private String inputSymbol;
    private StockAdapter mAdapter; // Data to recyclerview adapter*/



        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler);
        mAdapter = new StockAdapter(stockList, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swiper = findViewById(R.id.swiper);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }

        });
        if(hasNetworkConnection())
            readFromFile();
        else
            showNoNetworkDialog();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item:
                Toast.makeText(this, "Add item selected ", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Stock Selection");
                builder.setMessage("Please enter a Stock Symbol");
                EditText input = new EditText(this);
                input.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        inputSymbol = input.getText().toString().trim();
                        doAsyncTask();
                    }

                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }

                });
                builder.create();
                builder.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }
    }


    private void doAsyncTask(){

            new AsyncLoader(this).execute(inputSymbol);

    }

    private void loadData(String s){
        new AsyncLoader(this).execute(s);

    }

    public void updateStock(HashMap<String, String>data){
            if(data.isEmpty()){

                    Toast.makeText(this, "Please Enter a Valid stock  symbol", Toast.LENGTH_SHORT).show();
                    return;
                }
            String symbol=data.get("SYM");
            String companyName=data.get("NAME");
            double latestPrice=Double.parseDouble(data.get("PRICE"));
            double change=Double.parseDouble(data.get("CANGE"));
            double changePercent=Double.parseDouble(data.get("CANGEP"));
            Stock st=new Stock(symbol,companyName, latestPrice, change, changePercent);
            stockList.add(st);
            mAdapter.notifyDataSetChanged();
    }




    private void doRefresh() {


        swiper.setRefreshing(false);
        if(!hasNetworkConnection())
            showNoNetworkDialog();


       // writeToFile();
    }

        @Override
        public void onClick(View v) {  // click listener called by ViewHolder clicks

        int pos = recyclerView.getChildLayoutPosition(v);
        Stock m = stockList.get(pos);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Open Browser");
            builder.setMessage("Do you want to see more details about stock of "+m.getCompanyName());

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Intent intent =new Intent(Intent.ACTION_VIEW);
                    String url="https://www.marketwatch.com/investing/stock/"+m.getSymbol();
                    intent.setData(Uri.parse(url));
                    startActivity(intent);

                }

            });


            builder.setNegativeButton("DISMISS", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();

                }

            });
            builder.create();
            builder.show();


        Toast.makeText(v.getContext(), "SHORT " + m.toString(), Toast.LENGTH_SHORT).show();
    }

        @Override
        public boolean onLongClick(View v) {  // long click listener called by ViewHolder long clicks

            int pos = recyclerView.getChildLayoutPosition(v);
            Stock m = stockList.get(pos);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("WARNING!");
            builder.setMessage("Are you sure you want to delete "+m.getCompanyName()+" from Stock Watch");

            builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    stockList.remove(m);
                    mAdapter.notifyDataSetChanged();
                }

            });

            builder.setNegativeButton("DISMISS", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();

                }

            });
            builder.create();
            builder.show();


        return false;
    }


    public void writeToFile()
    {
        JSONArray jsonArray = new JSONArray();
        for (Stock s:stockList)
        {
            try{
                JSONObject stockJson=new JSONObject();
                stockJson.put("symbol",s.getSymbol());
                /*stockJson.put("companyName", s.getCompanyName());
                stockJson.put("latestPrice", s.getLatestPrice());
                stockJson.put("change", s.getChangeAmount());
                stockJson.put("changePercentage",s.getChangePercentage());*/
                //stockJson.put("changeAggregate",s.getChangeAggregate());
                jsonArray.put(stockJson);

            }catch (JSONException e) {e.printStackTrace();}
        }
        String jsonText = jsonArray.toString();
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("mydata.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(jsonText);
            outputStreamWriter.close();
            Toast.makeText(this, "File write success!", Toast.LENGTH_LONG).show();
        } catch (IOException e) { e.getMessage();}
    }
    public void readFromFile()
    {
     //stockList.clear();
      try {
            InputStream inputStream = openFileInput("mydata.txt");

            if ( inputStream != null )
            {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                String jsonText = stringBuilder.toString();

                try {
                    JSONArray jsonArray = new JSONArray(jsonText);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String symbol = jsonObject.getString("symbol");
                        /*String companyName = jsonObject.getString("companyName");
                        double latestPrice=jsonObject.getDouble("latestPrice");
                        double change=jsonObject.getDouble("change");
                        double changePercent=jsonObject.getDouble("changePercent");
                        Stock s = new Stock(symbol, companyName, latestPrice,change,changePercent);
                        stockList.add(s);
                        Toast.makeText(this, "File Read success!", Toast.LENGTH_LONG).show();*/
                        loadData(symbol);
                        //mAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "JSON read fail", Toast.LENGTH_LONG).show();
                }

            }
        }
        catch (FileNotFoundException e) {
            Log.d(TAG, "readFromFile: File not found: \" + e.toString()");
            Toast.makeText(this, "File not found!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.d(TAG, "readFromFile: Can not read file: " + e.toString());
          Toast.makeText(this, "Can not read file!", Toast.LENGTH_LONG).show();
        }


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        symbolList.clear();
        /*for (int i=0; i<stockList.size(); i++ ){
            symbolList.add(stockList.get(i).getSymbol());
        }
        outState.putStringArrayList("savedList", (ArrayList<String>) symbolList);*/

    }

    @Override
    protected void onPause() {
        super.onPause();
        writeToFile();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
       /* symbolList.addAll(savedInstanceState.getStringArrayList("savedList"));
        if(!symbolList.isEmpty()){
            for (String str: symbolList){
                loadData(str);
                //mAdapter.notifyDataSetChanged();
            }
        }*/


    }

    private boolean hasNetworkConnection()
    {

        boolean wifi_con=false;
        boolean mobile_con=false;
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo=connectivityManager.getAllNetworkInfo();
        for (NetworkInfo info: networkInfo){
            if (info.getTypeName().equalsIgnoreCase("WIFI")){
                if(info.isConnected())
                    wifi_con=true;
            }

            if (info.getTypeName().equalsIgnoreCase("MOBILE")){
                if(info.isConnected())
                    mobile_con=true;
            }
        }

        return wifi_con||mobile_con;

    }

    private void showNoNetworkDialog()
    {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("NO NETWORK CONNECT");
            builder.setMessage("Stocks can not be updated without a network connection");
        builder.create();
        builder.show();


    }
}