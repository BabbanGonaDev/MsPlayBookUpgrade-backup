package com.babbangona.mspalybookupgrade.data.db.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.babbangona.mspalybookupgrade.data.db.entities.Logs;

import java.util.List;

@Dao
public abstract class LogsDao {

    @Query(" SELECT COUNT(unique_field_id) FROM logs WHERE sync_flag != '1' ")
    public abstract int getUnSyncedLogsCount();

    @Query(" SELECT * FROM logs WHERE sync_flag != '1' ")
    public abstract List<Logs> getUnSyncedLogs();

    @Query(" UPDATE logs SET sync_flag = '1' WHERE unique_field_id = :unique_field_id " +
            "AND staff_id = :staff_id AND activity_type = :activity_type AND date_logged = :date_logged ")
    public abstract void updateLogs(String unique_field_id, String staff_id, String activity_type, String date_logged);

    /**
     * Insert the object in database
     * @param logs, object to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(Logs logs);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<Logs> logs);

    /**
     * update the object in database
     * @param logs, object to be updated
     */
    @Update
    public abstract void update(Logs logs);

    /**
     * delete the object from database
     * @param logs, object to be deleted
     */
    @Delete
    public abstract void delete(Logs logs);



}
