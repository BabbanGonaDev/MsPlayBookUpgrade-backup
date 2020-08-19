package com.babbangona.mspalybookupgrade.data.db.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.babbangona.mspalybookupgrade.RecyclerAdapters.HGFieldListRecycler.HGFieldListRecyclerModel;
import com.babbangona.mspalybookupgrade.data.db.entities.HGActivitiesFlag;

import java.util.List;

@Dao
public abstract class HGActivitiesFlagDao {

    @Query(" SELECT COUNT(DISTINCT a.unique_field_id) FROM hg_activities_flag a JOIN fields b " +
            "ON a.unique_field_id = b.unique_field_id WHERE a.hg_status != '0' " +
            "AND LOWER(b.staff_id || b.mss) LIKE LOWER(:staff_id) AND b.deactivate = '0' ")
    public abstract int getHGCount(String staff_id);

    @Query(" SELECT COUNT(DISTINCT a.unique_field_id) FROM hg_activities_flag a JOIN fields b " +
            "ON a.unique_field_id = b.unique_field_id WHERE a.hg_status != '0' " +
            "AND ((b.min_lat+b.max_lat)/2) > :min_lat AND ((b.min_lat+b.max_lat)/2) <= :max_lat AND ((b.min_lng+b.max_lng)/2) > :min_lng " +
            "AND ((b.min_lng+b.max_lng)/2) <= :max_lng AND LOWER(b.staff_id || b.mss) LIKE LOWER(:staff_id) AND b.deactivate = '0' ")
    public abstract int fieldPortionCountForHG(String staff_id, double min_lat, double max_lat, double min_lng, double max_lng);

    @Query("SELECT COUNT(unique_field_id) FROM hg_activities_flag WHERE unique_field_id = :unique_field_id AND hg_status != '0'")
    public abstract int countFieldInHGActivity(String unique_field_id);

    @Query("SELECT COUNT(unique_field_id) FROM hg_activities_flag " +
            "WHERE unique_field_id = :unique_field_id AND hg_status != '0' AND hg_type = :hg_type")
    public abstract int countFieldSpecificHGActivity(String unique_field_id, String hg_type);

    @Query("UPDATE hg_activities_flag SET hg_status = :status, hg_date = :date," +
            "staff_id = :staff_id, sync_flag = '0', date_logged = :date_logged " +
            "WHERE unique_field_id = :unique_field_id AND hg_type = :hg_type")
    public abstract void updateHGFlag(String unique_field_id, String hg_type, String status,
                                      String date, String staff_id, String date_logged);

    @Query("SELECT hg_type FROM hg_activities_flag WHERE unique_field_id = :unique_field_id AND hg_status != '0'")
    public abstract List<String> getAllFieldHGs(String unique_field_id);

    @Query("SELECT hg_type, hg_status FROM hg_activities_flag WHERE unique_field_id = :unique_field_id AND hg_status != '0'")
    public abstract List<HGFieldListRecyclerModel.HGListModel> getAllActiveHGs(String unique_field_id);

    @Query(" SELECT COUNT(unique_field_id) FROM hg_activities_flag WHERE sync_flag != '1' ")
    public abstract int getUnSyncedHGActivitiesCount();

    @Query(" SELECT * FROM hg_activities_flag WHERE sync_flag != '1' ")
    public abstract List<HGActivitiesFlag> getUnSyncedHGActivities();

    @Query(" UPDATE hg_activities_flag SET sync_flag = '1' WHERE unique_field_id = :unique_field_id AND hg_type = :hg_type ")
    public abstract void updateHGActivitiesSyncFlags(String unique_field_id, String hg_type);

    /**
     * Insert the object in database
     * @param hgActivitiesFlag, object to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(HGActivitiesFlag hgActivitiesFlag);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<HGActivitiesFlag> hgActivitiesFlag);

    /**
     * update the object in database
     * @param hgActivitiesFlag, object to be updated
     */
    @Update
    public abstract void update(HGActivitiesFlag hgActivitiesFlag);

    /**
     * delete the object from database
     * @param hgActivitiesFlag, object to be deleted
     */
    @Delete
    public abstract void delete(HGActivitiesFlag hgActivitiesFlag);



}
