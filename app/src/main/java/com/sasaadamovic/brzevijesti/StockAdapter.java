package com.sasaadamovic.brzevijesti;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> {

    private List<String> stockSymbols;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onFavoriteClick(String symbol, int position);
    }

    public StockAdapter(List<String> stockSymbols, OnItemClickListener listener) {
        this.stockSymbols = stockSymbols;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stock, parent, false);
        return new StockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
        String symbol = stockSymbols.get(position);
        holder.stockSymbol.setText(symbol);


        holder.stockPrice.setText("Loading price...");

        holder.favoriteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFavoriteClick(symbol, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stockSymbols.size();
    }

    public void updateStockPrice(int position, String price) {
    }

    public static class StockViewHolder extends RecyclerView.ViewHolder {
        TextView stockSymbol;
        TextView stockPrice;
        Button favoriteButton;

        public StockViewHolder(@NonNull View itemView) {
            super(itemView);
            stockSymbol = itemView.findViewById(R.id.stockItemSymbol);
            stockPrice = itemView.findViewById(R.id.stockItemPrice);
            favoriteButton = itemView.findViewById(R.id.stockItemFavoriteButton);
        }
    }
}