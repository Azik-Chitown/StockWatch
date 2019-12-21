package edu.depaul.stockwatch;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class JsonIO {
public static void writeToFile(List<Stock> stockList, Context ctx){
    JSONArray jsonArray = new JSONArray();

    for (Stock s:stockList){
        try{
            JSONObject stockJson=new JSONObject();
            stockJson.put("symbol",s.getSymbol());
            stockJson.put("companyName", s.getCompanyName());
            stockJson.put("latestPrice", s.getLatestPrice());
            stockJson.put("change", s.getChangeAmount());
            stockJson.put("changePercentage",s.getChangePercentage());
            //stockJson.put("changeAggregate",s.getChangeAggregate());


        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
    String jsonText = jsonArray.toString();



    try {
        OutputStreamWriter outputStreamWriter =
                new OutputStreamWriter(ctx.openFileOutput("mydata.txt", ctx.MODE_PRIVATE)
                );

        outputStreamWriter.write(jsonText);
        outputStreamWriter.close();

    } catch (IOException e) {
        e.getMessage();
    }
}
}
