package com.babbangona.mspalybookupgrade.data.db.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.babbangona.mspalybookupgrade.data.db.entities.NormalActivitiesFlag;

import java.util.List;

@Dao
public abstract class NormalActivitiesFlagDao {

    @Query(" SELECT COUNT(a.unique_field_id) FROM normal_activities_flag a JOIN fields b " +
            "ON a.unique_field_id = b.unique_field_id WHERE a.fertilizer_1_status = '1' " +
            "AND LOWER(b.staff_id || b.mss) LIKE LOWER(:staff_id) ")
    public abstract int getFertilizer1Count(String staff_id);

    @Query(" SELECT COUNT(a.unique_field_id) FROM normal_activities_flag a JOIN fields b " +
            "on a.unique_field_id = b.unique_field_id WHERE a.fertilizer_2_status = '1' " +
            "AND LOWER(b.staff_id || b.mss) LIKE LOWER(:staff_id) ")
    public abstract int getFertilizer2Count(String staff_id);

    @Query(" SELECT COUNT(unique_field_id) FROM fields WHERE ((min_lat+max_lat)/2) > :min_lat AND ((min_lat+max_lat)/2) <= :max_lat " +
            "AND ((min_lng+max_lng)/2) > :min_lng AND ((min_lng+max_lng)/2) <= :max_lng AND LOWER(staff_id || mss) LIKE LOWER(:staff_id) ")
    public abstract int fieldPortionCount(String staff_id, double min_lat, double max_lat, double min_lng, double max_lng);

    @Query(" SELECT COUNT(a.unique_field_id) FROM normal_activities_flag a JOIN fields b " +
            "ON a.unique_field_id = b.unique_field_id WHERE a.fertilizer_1_status = '1' " +
            "AND ((b.min_lat+b.max_lat)/2) > :min_lat AND ((b.min_lat+b.max_lat)/2) <= :max_lat AND ((b.min_lng+b.max_lng)/2) > :min_lng " +
            "AND ((b.min_lng+b.max_lng)/2) <= :max_lng AND LOWER(b.staff_id || b.mss) LIKE LOWER(:staff_id) ")
    public abstract int fieldPortionCountForFertilizer1(String staff_id, double min_lat, double max_lat, double min_lng, double max_lng);

    @Query(" SELECT COUNT(a.unique_field_id) FROM normal_activities_flag a JOIN fields b " +
            "ON a.unique_field_id = b.unique_field_id WHERE a.fertilizer_2_status = '1' " +
            "AND ((b.min_lat+b.max_lat)/2) > :min_lat AND ((b.min_lat+b.max_lat)/2) <= :max_lat AND ((b.min_lng+b.max_lng)/2) > :min_lng " +
            "AND ((b.min_lng+b.max_lng)/2) <= :max_lng AND LOWER(b.staff_id || b.mss) LIKE LOWER(:staff_id) ")
    public abstract int fieldPortionCountForFertilizer2(String staff_id, double min_lat, double max_lat, double min_lng, double max_lng);

    @Query("SELECT COUNT(unique_field_id) FROM normal_activities_flag WHERE unique_field_id = :unique_field_id")
    public abstract int countFieldInNormalActivity(String unique_field_id);

    @Query("UPDATE normal_activities_flag SET fertilizer_1_status = :flag, fertilizer_1_date = :date," +
            "staff_id = :staff_id, sync_flag = '0' " +
            "WHERE unique_field_id = :unique_field_id")
    public abstract void updateFert1Flag(String unique_field_id, String flag, String date, String staff_id);

    @Query("UPDATE normal_activities_flag SET fertilizer_2_status = :flag, fertilizer_2_date = :date, " +
            "staff_id = :staff_id, sync_flag = '0' " +
            "WHERE unique_field_id = :unique_field_id")
    public abstract void updateFert2Flag(String unique_field_id, String flag, String date, String staff_id);

    @Query(" SELECT COUNT(unique_field_id) FROM normal_activities_flag WHERE sync_flag != '1' ")
    public abstract int getUnSyncedNormalActivitiesCount();

    @Query(" SELECT * FROM normal_activities_flag WHERE sync_flag != '1' ")
    public abstract List<NormalActivitiesFlag> getUnSyncedNormalActivities();

    @Query(" UPDATE normal_activities_flag SET sync_flag = '1' WHERE unique_field_id = :unique_field_id ")
    public abstract void updateNormalActivitiesSyncFlag(String unique_field_id);

    @Query(" SELECT fertilizer_1_status FROM normal_activities_flag WHERE unique_field_id = :unique_field_id ")
    public abstract String getFert1Status(String unique_field_id);

    @Query(" SELECT fertilizer_2_status FROM normal_activities_flag WHERE unique_field_id = :unique_field_id ")
    public abstract String getFert2Status(String unique_field_id);

    @Query("UPDATE normal_activities_flag SET cc_harvest = :location_id, sync_flag='0' WHERE unique_field_id = :unique_filed_id" )
    public abstract void updateLocationID(String location_id, String unique_filed_id);

    @Query("SELECT cc_harvest FROM normal_activities_flag WHERE unique_field_id = :unique_field_id")
    public abstract String getHarvestCollectionCenter(String unique_field_id);

    /**
     * Insert the object in database
     * @param normalActivitiesFlag, object to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(NormalActivitiesFlag normalActivitiesFlag);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<NormalActivitiesFlag> normalActivitiesFlag);

    /**
     * update the object in database
     * @param normalActivitiesFlag, object to be updated
     */
    @Update
    public abstract void update(NormalActivitiesFlag normalActivitiesFlag);

    /**
     * delete the object from database
     * @param normalActivitiesFlag, object to be deleted
     */
    @Delete
    public abstract void delete(NormalActivitiesFlag normalActivitiesFlag);



}
