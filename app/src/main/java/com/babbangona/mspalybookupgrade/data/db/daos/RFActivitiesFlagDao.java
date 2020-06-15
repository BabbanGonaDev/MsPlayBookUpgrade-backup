package com.babbangona.mspalybookupgrade.data.db.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.babbangona.mspalybookupgrade.RecyclerAdapters.HGFieldListRecycler.HGFieldListRecyclerModel;
import com.babbangona.mspalybookupgrade.data.db.entities.RFActivitiesFlag;

import java.util.List;

@Dao
public abstract class RFActivitiesFlagDao {

    @Query(" SELECT COUNT(DISTINCT a.unique_field_id) FROM rf_activities_flag a JOIN fields b " +
            "ON a.unique_field_id = b.unique_field_id WHERE a.rf_status != '0' " +
            "AND LOWER(b.staff_id || b.mss) LIKE LOWER(:staff_id) ")
    public abstract int getRFCount(String staff_id);

    @Query(" SELECT COUNT(DISTINCT a.unique_field_id) FROM rf_activities_flag a JOIN fields b " +
            "ON a.unique_field_id = b.unique_field_id WHERE a.rf_status != '0' " +
            "AND ((b.min_lat+b.max_lat)/2) > :min_lat AND ((b.min_lat+b.max_lat)/2) <= :max_lat AND ((b.min_lng+b.max_lng)/2) > :min_lng " +
            "AND ((b.min_lng+b.max_lng)/2) <= :max_lng AND LOWER(b.staff_id || b.mss) LIKE LOWER(:staff_id) ")
    public abstract int fieldPortionCountForRF(String staff_id, double min_lat, double max_lat, double min_lng, double max_lng);

    @Query("SELECT COUNT(unique_field_id) FROM rf_activities_flag WHERE unique_field_id = :unique_field_id AND rf_status != '0'")
    public abstract int countFieldInRFActivity(String unique_field_id);

    @Query("SELECT COUNT(unique_field_id) FROM rf_activities_flag " +
            "WHERE unique_field_id = :unique_field_id AND rf_status != '0' AND rf_type = :hg_type")
    public abstract int countFieldSpecificRFActivity(String unique_field_id, String hg_type);

    @Query("UPDATE rf_activities_flag SET rf_status = :status, rf_date = :date," +
            "staff_id = :staff_id, sync_flag = '0' " +
            "WHERE unique_field_id = :unique_field_id AND rf_type = :rf_type")
    public abstract void updateRFFlag(String unique_field_id, String rf_type, String status, String date, String staff_id);

    @Query("SELECT rf_type FROM rf_activities_flag WHERE unique_field_id = :unique_field_id AND rf_status != '0'")
    public abstract List<String> getAllFieldRFs(String unique_field_id);

    @Query("SELECT rf_type, rf_status FROM rf_activities_flag WHERE unique_field_id = :unique_field_id AND rf_status != '0'")
    public abstract List<HGFieldListRecyclerModel.RFListModel> getAllActiveRFs(String unique_field_id);

    @Query(" SELECT COUNT(unique_field_id) FROM rf_activities_flag WHERE sync_flag != '1' ")
    public abstract int getUnSyncedRFActivitiesCount();

    @Query(" SELECT * FROM rf_activities_flag WHERE sync_flag != '1' ")
    public abstract List<RFActivitiesFlag> getUnSyncedRFActivities();

    @Query(" UPDATE rf_activities_flag SET sync_flag = '1' WHERE unique_field_id = :unique_field_id AND rf_type = :hg_type ")
    public abstract void updateRFActivitiesSyncFlags(String unique_field_id, String hg_type);

    /**
     * Insert the object in database
     * @param rfActivitiesFlag, object to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(RFActivitiesFlag rfActivitiesFlag);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<RFActivitiesFlag> rfActivitiesFlag);

    /**
     * update the object in database
     * @param rfActivitiesFlag, object to be updated
     */
    @Update
    public abstract void update(RFActivitiesFlag rfActivitiesFlag);

    /**
     * delete the object from database
     * @param rfActivitiesFlag, object to be deleted
     */
    @Delete
    public abstract void delete(RFActivitiesFlag rfActivitiesFlag);



}
