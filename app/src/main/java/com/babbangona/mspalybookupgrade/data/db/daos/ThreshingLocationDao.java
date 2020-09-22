package com.babbangona.mspalybookupgrade.data.db.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


//TODO: Modify the queries hear to suit your applications needs
@Dao
public interface ThreshingLocationDao {

    @Query(" select * from threshing_location WHERE sync_flag != '1' ")
    List<ThreshingLocation> getAllUnSyncedLocation();

    @Query(" UPDATE threshing_location SET sync_flag = '1' WHERE unique_member_id = :unique_member_id ")
    void updateThreshingLocation(String unique_member_id);

    /**
     * Insert the object in database
     * @param insert, object to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ThreshingLocation insert);

    /**
     * update the object in database
     * @param update, object to be updated
     */
    @Update
    void update(ThreshingLocation update);

    /**
     * delete the object from database
     * @param delete, object to be deleted
     */
    @Delete
    void delete(ThreshingLocation delete);



}
