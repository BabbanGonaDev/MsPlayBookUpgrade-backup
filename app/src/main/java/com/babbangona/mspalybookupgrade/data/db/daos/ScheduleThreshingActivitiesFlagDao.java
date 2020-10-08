package com.babbangona.mspalybookupgrade.data.db.daos;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.babbangona.mspalybookupgrade.RecyclerAdapters.ThreshingFieldListRecycler.ViewScheduleRecyclerModel;
import com.babbangona.mspalybookupgrade.data.db.entities.NormalActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.db.entities.ScheduledThreshingActivitiesFlag;

import java.util.List;

@Dao
public abstract class ScheduleThreshingActivitiesFlagDao {

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

    @Query(" SELECT SUM(b.field_size) FROM confirm_threshing_activities_flag a JOIN fields b " +
            "ON a.unique_field_id = b.unique_field_id JOIN members c on b.unique_member_id = c.unique_member_id " +
            "WHERE b.staff_id = :staff_id AND b.deactivate = '0' AND a.confirm_flag = '1' ")
    public abstract float getAssignedScheduleThreshSum(String staff_id);

    @Query(" SELECT SUM(b.field_size) FROM confirm_threshing_activities_flag a JOIN fields b " +
            "ON a.unique_field_id = b.unique_field_id JOIN members c on b.unique_member_id = c.unique_member_id " +
            "WHERE a.staff_id = :staff_id AND b.deactivate = '0' AND a.confirm_flag = '1' ")
    public abstract float getScheduleThreshSum(String staff_id);

    @Query(" SELECT SUM(b.field_size) FROM fields b JOIN members c on b.unique_member_id = c.unique_member_id " +
            "WHERE b.staff_id = :staff_id AND b.deactivate = '0' ")
    public abstract float getAssignedTotalFieldsSum(String staff_id);

    @Query(" SELECT SUM(b.field_size) FROM schedule_threshing_activities_flag a JOIN fields b " +
            "ON a.unique_field_id = b.unique_field_id JOIN members c on b.unique_member_id = c.unique_member_id " +
            "WHERE b.mss = :mss AND b.deactivate = '0' ")
    public abstract float getCoachScheduleThreshSum(String mss);

    @Query(" SELECT SUM(b.field_size) FROM fields b JOIN members c on b.unique_member_id = c.unique_member_id " +
            "WHERE b.mss = :mss AND b.deactivate = '0' ")
    public abstract double getCoachTotalFieldsSum(String mss);

    //TODO: HTA-182 Completed
    @Query("UPDATE schedule_threshing_activities_flag SET schedule_date = :schedule_date, reschedule_reason = :reschedule_reason," +
            "staff_id = :staff_id, sync_flag = '0', date_logged = :date_logged, reschedule_flag = '1', urgent_flag = '0', schedule_flag = '1' " +
            "WHERE unique_field_id = :unique_field_id")
    public abstract void updateScheduleDate(String unique_field_id, String schedule_date,
                                            String reschedule_reason, String staff_id, String date_logged);

    @Query("UPDATE schedule_threshing_activities_flag SET schedule_date = :schedule_date, reschedule_reason = :reschedule_reason," +
            "staff_id = :staff_id, sync_flag = '0', date_logged = :date_logged, schedule_flag = '0', urgent_flag = '1' " +
            "WHERE unique_field_id = :unique_field_id")
    public abstract void updateUrgentScheduleDate(String unique_field_id, String schedule_date,
                                            String reschedule_reason, String staff_id, String date_logged);

    @Query(" SELECT COUNT(unique_field_id) FROM schedule_threshing_activities_flag WHERE sync_flag != '1' ")
    public abstract int getUnSyncedScheduleThreshingActivitiesCount();

    @Query(" SELECT * FROM schedule_threshing_activities_flag WHERE sync_flag != '1' ")
    public abstract List<ScheduledThreshingActivitiesFlag> getUnSyncedScheduleThreshingActivities();

    @Query(" UPDATE schedule_threshing_activities_flag SET sync_flag = '1' WHERE unique_field_id = :unique_field_id ")
    public abstract void updateScheduledThreshingActivitiesSyncFlag(String unique_field_id);

    @Query(" SELECT COUNT(unique_field_id) FROM schedule_threshing_activities_flag WHERE unique_field_id = :unique_field_id AND schedule_flag = '1' ")
    public abstract int getFieldScheduleStatus(String unique_field_id);

    @Query(" SELECT COUNT(unique_field_id) FROM schedule_threshing_activities_flag WHERE unique_field_id = :unique_field_id AND urgent_flag = '1'  ")
    public abstract int getFieldUrgentScheduleStatus(String unique_field_id);

    @Query(" SELECT a.unique_field_id, b.field_size FROM schedule_threshing_activities_flag a " +
            "JOIN fields b ON a.unique_field_id = b.unique_field_id " +
            "WHERE a.staff_id = :staff_id AND a.schedule_date = :schedule_date AND b.deactivate = '0'")
    public abstract List<ScheduledThreshingActivitiesFlag.ScheduleCalculationModel> getAllScheduledFields(String staff_id, String schedule_date);

    @Query(" SELECT schedule_date FROM schedule_threshing_activities_flag WHERE unique_field_id = :unique_field_id ")
    public abstract String getFieldSchedule(String unique_field_id);

    @Query(" SELECT DISTINCT schedule_date FROM schedule_threshing_activities_flag WHERE staff_id = :staff_id AND schedule_flag = '1' ")
    public abstract List<String> getScheduleDateCount(String staff_id);

    @Query(" SELECT COUNT(schedule_date) FROM schedule_threshing_activities_flag WHERE staff_id = :staff_id AND schedule_date = :schedule_date ")
    public abstract int getDateCount(String staff_id, String schedule_date);


    @Query(" SELECT  c.first_name||' '||c.last_name as member_name, a.phone_number as phone_number, c.village_name as location,  a.unique_field_id as field_id,  a.schedule_date as threshing_date, b.field_size as field_size  FROM schedule_threshing_activities_flag a " +
            "JOIN fields b ON a.unique_field_id = b.unique_field_id  JOIN members c on b.unique_member_id = c.unique_member_id " +
            "WHERE  (a.staff_id = :staff_id or a.staff_id != :staff_id)   AND b.deactivate = '0'")
    public abstract List<ViewScheduleRecyclerModel> viewAllScheduledFields(String staff_id);


    @Query(" SELECT c.first_name||' '||c.last_name as member_name, a.phone_number as phone_number, c.village_name as location,  a.unique_field_id as field_id,  a.schedule_date as threshing_date, b.field_size as field_size FROM schedule_threshing_activities_flag a " +
            "JOIN fields b ON a.unique_field_id = b.unique_field_id  JOIN members c on b.unique_member_id = c.unique_member_id " +
            "WHERE  (a.staff_id = :staff_id or a.staff_id != :staff_id )  AND b.deactivate = '0' and a.urgent_flag = '1' ")
    public abstract List<ViewScheduleRecyclerModel> viewAllUrgentScheduledFields(String staff_id);


    /**
     * Insert the object in database
     * @param scheduledThreshingActivitiesFlag, object to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(ScheduledThreshingActivitiesFlag scheduledThreshingActivitiesFlag);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<ScheduledThreshingActivitiesFlag> scheduledThreshingActivitiesFlag);

    /**
     * update the object in database
     * @param scheduledThreshingActivitiesFlag, object to be updated
     */
    @Update
    public abstract void update(ScheduledThreshingActivitiesFlag scheduledThreshingActivitiesFlag);

    /**
     * delete the object from database
     * @param scheduledThreshingActivitiesFlag, object to be deleted
     */
    @Delete
    public abstract void delete(ScheduledThreshingActivitiesFlag scheduledThreshingActivitiesFlag);



}
