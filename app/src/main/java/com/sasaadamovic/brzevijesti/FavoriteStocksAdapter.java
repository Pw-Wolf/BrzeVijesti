package com.sasaadamovic.brzevijesti;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoriteStocksAdapter extends RecyclerView.Adapter<FavoriteStocksAdapter.FavoriteStockViewHolder> {

    private List<FavoriteStock> favoriteStocks;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onRemoveClick(FavoriteStock stock);
    }

    public FavoriteStocksAdapter(List<FavoriteStock> favoriteStocks, OnItemClickListener listener) {
        this.favoriteStocks = favoriteStocks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavoriteStockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_stock, parent, false);
        return new FavoriteStockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteStockViewHolder holder, int position) {
        FavoriteStock stock = favoriteStocks.get(position);
        holder.favoriteStockSymbol.setText(stock.getSymbol());
        holder.favoriteCompanyName.setText(stock.getCompanyName());
        holder.removeFavoriteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRemoveClick(stock);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteStocks.size();
    }

    public void setFavoriteStocks(List<FavoriteStock> favoriteStocks) {
        this.favoriteStocks = favoriteStocks;
        notifyDataSetChanged();
    }

    public static class FavoriteStockViewHolder extends RecyclerView.ViewHolder {
        TextView favoriteStockSymbol;
        TextView favoriteCompanyName;
        Button removeFavoriteButton;

        public FavoriteStockViewHolder(@NonNull View itemView) {
            super(itemView);
            favoriteStockSymbol = itemView.findViewById(R.id.favoriteStockSymbol);
            favoriteCompanyName = itemView.findViewById(R.id.favoriteCompanyName);
            removeFavoriteButton = itemView.findViewById(R.id.removeFavoriteButton);
        }
    }
}