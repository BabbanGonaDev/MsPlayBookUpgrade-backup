package com.babbangona.mspalybookupgrade.data.db.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.babbangona.mspalybookupgrade.data.db.entities.LocationEntity;

import java.util.List;

@Dao
public interface LocationDao {

    @Query("SELECT DISTINCT state FROM locations_table WHERE STATE = 'Kano' " +
            "OR STATE = 'Kaduna' OR STATE = 'Katsina' OR STATE = 'Jigawa' OR STATE = 'Niger' " +
            "OR STATE = 'Plateau' OR STATE = 'Bauchi'  ORDER BY state ASC")
    List<String> getAllStates();

    @Query("SELECT DISTINCT lga FROM locations_table  WHERE STATE =:State ORDER BY LGA ASC")
    List<String> getAllLgs(String State);

    @Query("SELECT DISTINCT ward FROM locations_table  WHERE STATE =:State AND LGA =:Lga ORDER BY WARD ASC")
    List<String> getAllWards(String State, String Lga);

    @Query("SELECT location_id FROM locations_table WHERE state =:state AND lga =:lga AND WARD=:ward")
    String getId(String state, String lga, String ward);

    @Query("SELECT lga FROM locations_table WHERE location_id =:locationId")
    String getLGA(String locationId);

    @Query("SELECT ward FROM locations_table WHERE location_id =:locationId")
    String getWard(String locationId);

    @Query("SELECT state FROM locations_table WHERE location_id =:locationId")
    String getState(String locationId);




    /*
     * Insert the object in database
     * @param data, object to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LocationEntity location);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<LocationEntity> locations);

    /*
     * update the object in database
     * @param data, object to be updated
     */
    @Update
    void update(LocationEntity location);

    /*
     * delete the object from database
     * @param data, object to be deleted
     */
    @Delete
    void delete(LocationEntity location);

    /*
     * delete list of objects from database
     * @param data, array of objects to be deleted
     */
    @Delete
    void delete(LocationEntity... location);      // data... is varargs, here data is an array
}
