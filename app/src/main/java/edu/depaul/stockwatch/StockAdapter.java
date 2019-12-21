package edu.depaul.stockwatch;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private List<Stock> stockList;
    private MainActivity mainAct;
    StockAdapter(List<Stock> stockList, MainActivity mainAct){
        this.stockList=stockList;
        this.mainAct=mainAct;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_list_row, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //Setting  text fields

        Stock stock =stockList.get(position);
        holder.symbol.setText(stock.getSymbol());
        holder.latestPrice.setText(Double.toString(stock.getLatestPrice()));
        holder.changeAggregate.setText(stock.getChangeAggregate());
        holder.name.setText(stock.getCompanyName());


        //Setting text colors depending on changeAmount value
        if(stock.getDirection()=="▲"){
            holder.symbol.setTextColor(Color.GREEN);
            holder.latestPrice.setTextColor((Color.GREEN));
            holder.changeAggregate.setTextColor(Color.GREEN);
            holder.name.setTextColor(Color.GREEN);
        }
        else {
            holder.symbol.setTextColor(Color.RED);
            holder.latestPrice.setTextColor(Color.RED);
            holder.changeAggregate.setTextColor(Color.RED);
            holder.name.setTextColor(Color.RED);
        }


    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }
}
