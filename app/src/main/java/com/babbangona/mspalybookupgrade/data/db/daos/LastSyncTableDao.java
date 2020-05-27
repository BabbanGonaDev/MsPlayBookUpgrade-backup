package com.babbangona.mspalybookupgrade.data.db.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.babbangona.mspalybookupgrade.data.db.entities.LastSyncTable;


@Dao
public interface LastSyncTableDao {

    @Query("SELECT COUNT(staff_id) FROM last_sync where staff_id = :staff_id")
    int getStaffCount(String staff_id);

    @Query("SELECT last_sync_activity_list FROM last_sync WHERE staff_id = :staff_id")
    String getLastSyncActivityList(String staff_id);

    @Query("UPDATE last_sync SET last_sync_activity_list =:last_sync_activity_list WHERE staff_id = :staff_id")
    void updateLastSyncActivityList(String staff_id, String last_sync_activity_list);

    @Query("SELECT last_sync_down_normal_activities_flag FROM last_sync WHERE staff_id = :staff_id")
    String getLastSyncDownNormalActivitiesFlag(String staff_id);

    @Query("UPDATE last_sync SET last_sync_down_normal_activities_flag =:last_sync_down_normal_activities_flag WHERE staff_id = :staff_id")
    void updateLastSyncDownNormalActivitiesFlag(String staff_id, String last_sync_down_normal_activities_flag);

    @Query("SELECT last_sync_up_normal_activities_flag FROM last_sync WHERE staff_id = :staff_id")
    String getLastSyncUpNormalActivitiesFlag(String staff_id);

    @Query("UPDATE last_sync SET last_sync_up_normal_activities_flag =:last_sync_up_normal_activities_flag WHERE staff_id = :staff_id")
    void updateLastSyncUpNormalActivitiesFlag(String staff_id, String last_sync_up_normal_activities_flag);

    @Query("SELECT last_sync_fields FROM last_sync WHERE staff_id = :staff_id")
    String getLastSyncFields(String staff_id);

    @Query("UPDATE last_sync SET last_sync_fields =:last_sync_fields WHERE staff_id = :staff_id")
    void updateLastSyncFields(String staff_id, String last_sync_fields);

    @Query("SELECT last_sync_staff FROM last_sync WHERE staff_id = :staff_id")
    String getLastSyncStaff(String staff_id);

    @Query("UPDATE last_sync SET last_sync_staff =:last_sync_staff WHERE staff_id = :staff_id")
    void updateLastSyncStaff(String staff_id, String last_sync_staff);

    @Query("SELECT last_sync_members FROM last_sync WHERE staff_id = :staff_id")
    String getLastSyncMembers(String staff_id);

    @Query("UPDATE last_sync SET last_sync_members =:last_sync_members WHERE staff_id = :staff_id")
    void updateLastSyncMembers(String staff_id, String last_sync_members);

    @Query("SELECT last_sync_up_hg_activities_flag FROM last_sync WHERE staff_id = :staff_id")
    String getLastSyncUpHGActivitiesFlag(String staff_id);

    @Query("UPDATE last_sync SET last_sync_up_hg_activities_flag =:last_sync_up_hg_activities_flag WHERE staff_id = :staff_id")
    void updateLastSyncUpHGActivitiesFlag(String staff_id, String last_sync_up_hg_activities_flag);

    @Query("SELECT last_sync_down_hg_activities_flag FROM last_sync WHERE staff_id = :staff_id")
    String getLastSyncDownHGActivitiesFlag(String staff_id);

    @Query("UPDATE last_sync SET last_sync_down_hg_activities_flag =:last_sync_down_hg_activities_flag WHERE staff_id = :staff_id")
    void updateLastSyncDownHGActivitiesFlag(String staff_id, String last_sync_down_hg_activities_flag);

    @Query("SELECT last_sync_hg_list FROM last_sync WHERE staff_id = :staff_id")
    String getLastSyncHGList(String staff_id);

    @Query("UPDATE last_sync SET last_sync_hg_list =:last_sync_hg_list WHERE staff_id = :staff_id")
    void updateLastSyncHGList(String staff_id, String last_sync_hg_list);

    @Query("SELECT last_sync_up_logs FROM last_sync WHERE staff_id = :staff_id")
    String getLastSyncUpLogs(String staff_id);

    @Query("UPDATE last_sync SET last_sync_up_logs =:last_sync_up_logs WHERE staff_id = :staff_id")
    void updateLastSyncUpLogs(String staff_id, String last_sync_up_logs);

    @Query("SELECT last_sync_down_logs FROM last_sync WHERE staff_id = :staff_id")
    String getLastSyncDownLogs(String staff_id);

    @Query("UPDATE last_sync SET last_sync_down_logs =:last_sync_down_logs WHERE staff_id = :staff_id")
    void updateLastSyncDownLogs(String staff_id, String last_sync_down_logs);

    @Query("SELECT last_sync_category FROM last_sync WHERE staff_id = :staff_id")
    String getLastSyncCategory(String staff_id);

    @Query("UPDATE last_sync SET last_sync_category =:last_sync_down_category WHERE staff_id = :staff_id")
    void updateLastSyncCategory(String staff_id, String last_sync_down_category);

    /**
     * Insert the object in database
     * @param lastSyncTable, object to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LastSyncTable lastSyncTable);

    /**
     * update the object in database
     * @param lastSyncTable, object to be updated
     */
    @Update
    void update(LastSyncTable lastSyncTable);

    /**
     * delete the object from database
     * @param lastSyncTable, object to be deleted
     */
    @Delete
    void delete(LastSyncTable lastSyncTable);

    /**
     * delete list of objects from database
     * @param data, array of objects to be deleted
     */
    @Delete
    void delete(LastSyncTable... data);      // data... is varargs, here data is an array
}
