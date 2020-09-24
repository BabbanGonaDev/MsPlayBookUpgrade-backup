package com.babbangona.mspalybookupgrade.data.db.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.babbangona.mspalybookupgrade.data.db.entities.ConfirmThreshingActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.db.entities.ScheduledThreshingActivitiesFlag;

import java.util.List;

@Dao
public abstract class ConfirmThreshingActivitiesFlagDao {

    @Query(" SELECT COUNT(a.unique_field_id) FROM schedule_threshing_activities_flag a JOIN fields b " +
            "ON a.unique_field_id = b.unique_field_id JOIN members c on b.unique_member_id = c.unique_member_id " +
            "WHERE b.staff_id = :staff_id AND b.deactivate = '0' ")
    public abstract int getAssignedScheduleThreshCount(String staff_id);

    @Query(" SELECT COUNT(a.unique_field_id) FROM schedule_threshing_activities_flag a JOIN fields b " +
            "ON a.unique_field_id = b.unique_field_id JOIN members c on b.unique_member_id = c.unique_member_id " +
            "WHERE b.mss = :mss AND b.deactivate = '0' ")
    public abstract int getCoachScheduleThreshCount(String mss);

    @Query(" SELECT COUNT(b.unique_field_id) FROM fields b JOIN members c on b.unique_member_id = c.unique_member_id " +
            "WHERE b.mss = :mss AND b.deactivate = '0' ")
    public abstract int getCoachTotalFields(String mss);

    @Query(" SELECT SUM(b.field_size) FROM schedule_threshing_activities_flag a JOIN fields b " +
            "ON a.unique_field_id = b.unique_field_id JOIN members c on b.unique_member_id = c.unique_member_id " +
            "WHERE b.staff_id = :staff_id AND b.deactivate = '0' ")
    public abstract double getAssignedScheduleThreshSum(String staff_id);

    @Query(" SELECT SUM(b.field_size) FROM schedule_threshing_activities_flag a JOIN fields b " +
            "ON a.unique_field_id = b.unique_field_id JOIN members c on b.unique_member_id = c.unique_member_id " +
            "WHERE b.mss = :mss AND b.deactivate = '0' ")
    public abstract double getCoachScheduleThreshSum(String mss);

    @Query(" SELECT SUM(b.field_size) FROM fields b JOIN members c on b.unique_member_id = c.unique_member_id " +
            "WHERE b.mss = :mss AND b.deactivate = '0' ")
    public abstract double getCoachTotalFieldsSum(String mss);

    @Query(" SELECT COUNT(unique_field_id) FROM confirm_threshing_activities_flag WHERE sync_flag != '1' ")
    public abstract int getUnSyncedConfirmThreshingActivitiesCount();

    @Query(" SELECT * FROM confirm_threshing_activities_flag WHERE sync_flag != '1' ")
    public abstract List<ConfirmThreshingActivitiesFlag> getUnSyncedConfirmThreshingActivities();

    @Query(" UPDATE confirm_threshing_activities_flag SET sync_flag = '1' WHERE unique_field_id = :unique_field_id ")
    public abstract void updateConfirmThreshingActivitiesSyncFlag(String unique_field_id);

    @Query(" SELECT COUNT(unique_field_id) FROM confirm_threshing_activities_flag " +
            "WHERE unique_field_id = :unique_field_id AND confirm_flag = '1'")
    public abstract int getFieldConfirmStatus(String unique_field_id);

    @Query("UPDATE confirm_threshing_activities_flag SET confirm_flag = '0', " +
            "staff_id = :staff_id, sync_flag = '0', confirm_date = :date_logged " +
            "WHERE unique_field_id = :unique_field_id")
    public abstract void updateConfirmStatus(String unique_field_id, String staff_id, String date_logged);

    /**
     * Insert the object in database
     * @param confirmThreshingActivitiesFlag, object to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(ConfirmThreshingActivitiesFlag confirmThreshingActivitiesFlag);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<ConfirmThreshingActivitiesFlag> confirmThreshingActivitiesFlag);

    /**
     * update the object in database
     * @param confirmThreshingActivitiesFlag, object to be updated
     */
    @Update
    public abstract void update(ConfirmThreshingActivitiesFlag confirmThreshingActivitiesFlag);

    /**
     * delete the object from database
     * @param confirmThreshingActivitiesFlag, object to be deleted
     */
    @Delete
    public abstract void delete(ConfirmThreshingActivitiesFlag confirmThreshingActivitiesFlag);



}
