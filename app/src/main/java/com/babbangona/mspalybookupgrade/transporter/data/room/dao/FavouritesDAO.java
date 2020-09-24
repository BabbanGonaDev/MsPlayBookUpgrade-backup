package com.babbangona.mspalybookupgrade.transporter.data.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.babbangona.mspalybookupgrade.transporter.data.room.tables.FavouritesTable;

import java.util.List;

@Dao
public interface FavouritesDAO {

    /**
     * Sync up of the table should upload all columns (including status).
     * While sync down of the table would also download all columns BUT NOT overwrite anything.
     */

    @Insert
    void insertFavouritesList(List<FavouritesTable> list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void markFavourite(FavouritesTable favourite);

    @Query("UPDATE favourites_table SET active_flag = 0 WHERE phone_number = :phone_number AND staff_id = :staff_id")
    void unMarkFavourite(String phone_number, String staff_id);
}
