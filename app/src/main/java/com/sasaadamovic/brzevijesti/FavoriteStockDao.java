package com.sasaadamovic.brzevijesti;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface FavoriteStockDao {
    @Query("SELECT * FROM favorite_stocks")
    List<FavoriteStock> getAll();

    @Insert
    void insert(FavoriteStock stock);

    @Update
    void update(FavoriteStock stock);


    @Query("DELETE FROM favorite_stocks WHERE symbol = :symbol")
    void deleteBySymbol(String symbol);

    @Query("SELECT COUNT(*) > 0 FROM favorite_stocks WHERE symbol = :symbol")
    boolean isFavorite(String symbol);
}