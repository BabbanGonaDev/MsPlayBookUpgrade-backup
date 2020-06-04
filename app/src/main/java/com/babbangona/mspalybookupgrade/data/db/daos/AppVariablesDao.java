package com.babbangona.mspalybookupgrade.data.db.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.babbangona.mspalybookupgrade.data.db.entities.AppVariables;

import java.util.List;

@Dao
public interface AppVariablesDao {

    @Query("SELECT edit_harvest_location_flag FROM app_variables WHERE variable_id = :variable_id")
    String getEditHarvestLocationFlag(String variable_id);

    /**
     * Insert the object in database
     * @param appVariables, object to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AppVariables appVariables);

    /**
     * Insert the object in database
     * @param appVariables, object to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<AppVariables> appVariables);

    /**
     * update the object in database
     * @param appVariables, object to be updated
     */
    @Update
    void update(AppVariables appVariables);

    /**
     * delete the object from database
     * @param appVariables, object to be deleted
     */
    @Delete
    void delete(AppVariables appVariables);

    /**
     * delete list of objects from database
     * @param data, array of objects to be deleted
     */
    @Delete
    void delete(AppVariables... data);      // data... is varargs, here data is an array
}
