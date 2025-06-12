package com.sasaadamovic.brzevijesti;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoriteStockDao {
    @Insert
    void insert(FavoriteStock stock);

    @Delete
    void delete(FavoriteStock stock);

    @Query("SELECT * FROM favorite_stocks")
    List<FavoriteStock> getAllFavoriteStocks();

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_stocks WHERE symbol = :symbol LIMIT 1)")
    boolean isFavorite(String symbol);
}