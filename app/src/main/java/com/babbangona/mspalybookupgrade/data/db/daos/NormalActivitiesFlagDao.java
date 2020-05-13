package com.babbangona.mspalybookupgrade.data.db.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.babbangona.mspalybookupgrade.data.db.entities.ActivityList;
import com.babbangona.mspalybookupgrade.data.db.entities.NormalActivitiesFlag;

import java.util.List;

@Dao
public interface NormalActivitiesFlagDao {

    @Query(" SELECT COUNT(unique_field_id) FROM normal_activities_flag WHERE fertilizer_1_status = '1' " +
            "AND staff_id = :staff_id ")
    int getFertilizer1Count(String staff_id);

    @Query(" SELECT COUNT(unique_field_id) FROM normal_activities_flag WHERE fertilizer_2_status = '1' " +
            "AND staff_id = :staff_id ")
    int getFertilizer2Count(String staff_id);

    /**
     * Insert the object in database
     * @param normalActivitiesFlag, object to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(NormalActivitiesFlag normalActivitiesFlag);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<NormalActivitiesFlag> normalActivitiesFlag);

    /**
     * update the object in database
     * @param normalActivitiesFlag, object to be updated
     */
    @Update
    void update(NormalActivitiesFlag normalActivitiesFlag);

    /**
     * delete the object from database
     * @param normalActivitiesFlag, object to be deleted
     */
    @Delete
    void delete(NormalActivitiesFlag normalActivitiesFlag);



}
