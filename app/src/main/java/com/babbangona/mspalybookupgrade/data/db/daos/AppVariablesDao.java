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

    @Query("SELECT minimum_log_date FROM app_variables WHERE variable_id = :variable_id")
    String getMinimumLogDate(String variable_id);

    @Query("SELECT maximum_log_date FROM app_variables WHERE variable_id = :variable_id")
    String getMaximumLogDate(String variable_id);

    @Query("SELECT fields_travel_time FROM app_variables WHERE variable_id = :variable_id")
    String getFieldsTravelTime(String variable_id);

    @Query("SELECT average_transition_time FROM app_variables WHERE variable_id = :variable_id")
    String getAverageTransitionTime(String variable_id);

    @Query("SELECT time_per_ha FROM app_variables WHERE variable_id = :variable_id")
    String getTimePerHa(String variable_id);

    @Query("SELECT maximum_schedule_date FROM app_variables WHERE variable_id = :variable_id")
    String getMaximumScheduleDate(String variable_id);

    @Query("SELECT luxand_flag FROM app_variables WHERE variable_id = :variable_id")
    String getLuxandFlag(String variable_id);

    @Query("SELECT fertilizer_luxand_flag FROM app_variables WHERE variable_id = :variable_id")
    String getFertilizerLuxandFlag(String variable_id);

    @Query("SELECT issues_list FROM app_variables WHERE variable_id = :variable_id")
    String getIssuesList(String variable_id);

    @Query("SELECT bgt_location_tracker_flag FROM app_variables WHERE variable_id = :variable_id")
    String getBgtLocationTrackerFlag(String variable_id);

    @Query("SELECT bgt_location_tracker_days FROM app_variables WHERE variable_id = :variable_id")
    String getBgtLocationTrackerDays(String variable_id);

    @Query("SELECT bgt_location_tracker_hours FROM app_variables WHERE variable_id = :variable_id")
    String getBgtLocationTrackerHours(String variable_id);

    /**
     * Insert the object in database
     *
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
