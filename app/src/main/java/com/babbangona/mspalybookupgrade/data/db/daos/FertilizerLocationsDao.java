package com.babbangona.mspalybookupgrade.data.db.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.babbangona.mspalybookupgrade.data.db.entities.FertilizerLocationsTable;
import com.babbangona.mspalybookupgrade.data.db.entities.HarvestLocationsTable;

import java.util.List;

@Dao
public interface FertilizerLocationsDao {

    @Query("SELECT DISTINCT distribution_center FROM fertilizer_location WHERE status = '1' ORDER BY distribution_center ASC")
    List<String> getAllFertilizerCollection();


    /**
     * Insert the object in database
     * @param fertilizerLocationsTable, object to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FertilizerLocationsTable fertilizerLocationsTable);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<FertilizerLocationsTable> fertilizerLocationsTable);

    /**
     * update the object in database
     * @param fertilizerLocationsTable, object to be updated
     */
    @Update
    void update(FertilizerLocationsTable fertilizerLocationsTable);

    /**
     * delete the object from database
     * @param fertilizerLocationsTable, object to be deleted
     */
    @Delete
    void delete(FertilizerLocationsTable fertilizerLocationsTable);

    /**
     * delete list of objects from database
     * @param data, array of objects to be deleted
     */
    @Delete
    void delete(FertilizerLocationsTable... data);      // data... is varargs, here data is an array
}
