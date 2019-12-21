package edu.depaul.stockwatch;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView symbol;
    public TextView latestPrice;
    public TextView changeAggregate;
    public TextView name;
    MyViewHolder(View view) {
        super(view);
        symbol=view.findViewById(R.id.symbol);
        latestPrice=view.findViewById(R.id.latestPrice);
        changeAggregate=view.findViewById(R.id.changeAgg);
        name=view.findViewById(R.id.name);
    }
}
