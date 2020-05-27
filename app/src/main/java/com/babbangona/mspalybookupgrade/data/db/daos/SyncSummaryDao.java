package com.babbangona.mspalybookupgrade.data.db.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.babbangona.mspalybookupgrade.data.db.entities.Members;
import com.babbangona.mspalybookupgrade.data.db.entities.SyncSummary;

import java.util.List;

@Dao
public interface SyncSummaryDao {

    @Query("UPDATE sync_summary SET remarks = :remarks, status = :status WHERE table_id = :table_id AND staff_id = :staff_id")
    void updateStatus(String table_id, String staff_id, String remarks, String status);

    @Query("SELECT COUNT(table_id) FROM sync_summary WHERE table_id = :table_id and staff_id = :staff_id")
    int countTableID(String table_id, String staff_id);

    @Query(" SELECT * FROM sync_summary WHERE staff_id = :staff_id ORDER BY table_id ASC ")
    LiveData<List<SyncSummary>> getAllSyncSummary(String staff_id);

    /**
     * Insert the object in database
     * @param syncSummary, object to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SyncSummary syncSummary);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<SyncSummary> syncSummaries);

    /**
     * update the object in database
     * @param syncSummary, object to be updated
     */
    @Update
    void update(SyncSummary syncSummary);

    /**
     * delete the object from database
     * @param syncSummary, object to be deleted
     */
    @Delete
    void delete(SyncSummary syncSummary);



}
