package com.babbangona.mspalybookupgrade.data.db.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.babbangona.mspalybookupgrade.data.db.entities.HarvestLocationsTable;

import java.util.List;

@Dao
public interface HarvestLocationsDao {

    @Query("SELECT DISTINCT state FROM harvest_location WHERE deactivate = '0' ORDER BY state ASC")
    List<String> getAllStates();

    @Query("SELECT DISTINCT lga FROM harvest_location  WHERE state =:state ORDER BY lga ASC")
    List<String> getAllLgs(String state);

    @Query("SELECT DISTINCT ward FROM harvest_location  WHERE state =:State AND lga =:Lga ORDER BY ward ASC")
    List<String> getAllWards(String State, String Lga);

    @Query("SELECT DISTINCT centre FROM harvest_location  WHERE state =:state AND lga =:lga AND WARD=:ward ORDER BY ward ASC")
    List<String> getAllCollectionPoints(String state, String lga, String ward);

    @Query("SELECT DISTINCT centre FROM harvest_location WHERE deactivate = '0' ORDER BY ward ASC")
    List<String> getAllCollectionPointsNoConstraint();


    /**
     * Insert the object in database
     * @param harvestLocationsTable, object to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HarvestLocationsTable harvestLocationsTable);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<HarvestLocationsTable> harvestLocationsTable);

    /**
     * update the object in database
     * @param harvestLocationsTable, object to be updated
     */
    @Update
    void update(HarvestLocationsTable harvestLocationsTable);

    /**
     * delete the object from database
     * @param harvestLocationsTable, object to be deleted
     */
    @Delete
    void delete(HarvestLocationsTable harvestLocationsTable);

    /**
     * delete list of objects from database
     * @param data, array of objects to be deleted
     */
    @Delete
    void delete(HarvestLocationsTable... data);      // data... is varargs, here data is an array
}
