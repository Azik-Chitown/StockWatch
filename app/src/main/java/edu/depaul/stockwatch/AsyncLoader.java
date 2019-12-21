package edu.depaul.stockwatch;

import android.os.AsyncTask;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.HashMap;

public class AsyncLoader extends AsyncTask<String, Void, String> {
    private static final String TAG = "AsyncLoaderTask";

    private MainActivity mainAct;
    private static final String stcckURL="https://cloud.iexapis.com/stable/stock/";
    private static final String tokenKey="sk_4a05a3e779f24570870dc77d8695f21c";
    private String symbolSymbol;

    private HashMap<String, String>stockData=new HashMap<>();
    AsyncLoader(MainActivity ma){mainAct=ma;}


    @Override
    protected void onPostExecute(String s) {
        mainAct.updateStock(stockData);
    }


    @Override
    protected String doInBackground(String... params) {

        symbolSymbol = params[0];
        String urlToUse = stcckURL + symbolSymbol + "/quote?token=" + tokenKey;

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "doInBackground: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);

            return null;
        }
        parseJSON(sb.toString());
        return null;
    }


    private void parseJSON(String s) {
        try{
            JSONObject jObj=new JSONObject(s);
            stockData.put("SYM", jObj.getString("symbol"));
            stockData.put("NAME", jObj.getString("companyName"));
            stockData.put("PRICE", Double.toString(jObj.getDouble("latestPrice")));
            stockData.put("CANGE", Double.toString(jObj.getDouble("change")));
            stockData.put("CANGEP", Double.toString(jObj.getDouble("changePercent")));

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
