package com.sasaadamovic.brzevijesti;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FavoritesFragment extends Fragment implements FavoriteStocksAdapter.OnItemClickListener {

    private RecyclerView favoritesRecyclerView;
    private FavoriteStocksAdapter adapter;
    private List<FavoriteStock> favoriteStockList;
    private AppDatabase db;
    private FavoriteStockDao favoriteStockDao;
    private ExecutorService executorService;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        favoritesRecyclerView = view.findViewById(R.id.favoritesRecyclerView);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        favoriteStockList = new ArrayList<>();
        adapter = new FavoriteStocksAdapter(favoriteStockList, this);
        favoritesRecyclerView.setAdapter(adapter);

        db = Room.databaseBuilder(getContext(),
                AppDatabase.class, "brzevijesti-db").build();
        favoriteStockDao = db.favoriteStockDao();
        executorService = Executors.newSingleThreadExecutor();

        loadFavoriteStocks();

        return view;
    }

    private void loadFavoriteStocks() {
        executorService.execute(() -> {
            List<FavoriteStock> stocks = favoriteStockDao.getAllFavoriteStocks();
            // Provjerite je li fragment još uvijek pripojen aktivnosti prije ažuriranja UI-ja
            if (isAdded() && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (isAdded()) { // Ponovna provjera unutar runOnUiThread
                        favoriteStockList.clear();
                        favoriteStockList.addAll(stocks);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public void onRemoveClick(FavoriteStock stock) {
        executorService.execute(() -> {
            favoriteStockDao.delete(stock);
            if (isAdded() && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (isAdded()) {
                        Toast.makeText(getContext(), stock.getSymbol() + " removed from favorites", Toast.LENGTH_SHORT).show();
                        loadFavoriteStocks(); // Osvježi listu nakon brisanja
                    }
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdownNow(); // Koristite shutdownNow() za prekidanje zadataka odmah
        }
    }
}