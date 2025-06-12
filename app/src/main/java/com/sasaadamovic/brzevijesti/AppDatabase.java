package com.sasaadamovic.brzevijesti;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {FavoriteStock.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FavoriteStockDao favoriteStockDao();
}