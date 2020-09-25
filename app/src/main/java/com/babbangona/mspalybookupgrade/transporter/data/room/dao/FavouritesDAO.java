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
     * <p>
     * https://uniqueandrocode.com/add-to-favourites-and-display-favourites-in-recyclerview/ (Just in case)
     * Also, NB: What's the way out since we not delete from the favourite table when unmarked, how do we reflect this delete on the backend. ?
     */

    @Insert
    void insertFavouritesList(List<FavouritesTable> list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void markFavourite(FavouritesTable favourite);

    /*@Query("UPDATE favourites_table SET active_flag = 0 WHERE phone_number = :phone_number AND staff_id = :staff_id")
    void unMarkFavourite(String phone_number, String staff_id);*/

    @Query("DELETE FROM favourites_table WHERE phone_number = :phone_number AND staff_id = :staff_id")
    void unMarkFavourite(String phone_number, String staff_id);
}
