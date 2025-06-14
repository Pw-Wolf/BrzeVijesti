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
    private OnItemLongClickListener longClickListener;
    private OnRemoveClickListener removeClickListener;

    public interface OnItemClickListener {
        void onItemClick(String symbol);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(FavoriteStock stock);
    }

    public interface OnRemoveClickListener {
        void onRemoveClick(FavoriteStock stock);
    }

    public FavoriteStocksAdapter(List<FavoriteStock> favoriteStocks, OnItemClickListener listener, OnItemLongClickListener longClickListener, OnRemoveClickListener removeClickListener) {
        this.favoriteStocks = favoriteStocks;
        this.listener = listener;
        this.longClickListener = longClickListener;
        this.removeClickListener = removeClickListener;
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
        holder.symbolTextView.setText(stock.getSymbol());
        holder.nameTextView.setText(stock.getName());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(stock.getSymbol());
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(stock);
                return true;
            }
            return false;
        });

        holder.removeButton.setOnClickListener(v -> {
            if (removeClickListener != null) {
                removeClickListener.onRemoveClick(stock);
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

    static class FavoriteStockViewHolder extends RecyclerView.ViewHolder {
        TextView symbolTextView;
        TextView nameTextView;
        Button removeButton;

        public FavoriteStockViewHolder(@NonNull View itemView) {
            super(itemView);
            symbolTextView = itemView.findViewById(R.id.favoriteStockSymbol);
            nameTextView = itemView.findViewById(R.id.favoriteCompanyName);
            removeButton = itemView.findViewById(R.id.removeFavoriteButton);
        }
    }
}