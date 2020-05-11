package com.babbangona.mspalybookupgrade.data.db.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.babbangona.mspalybookupgrade.data.db.entities.ActivityList;

import java.util.List;

@Dao
public interface ActivityListDao {


    @Query(" select * from activity_list ")
    List<ActivityList> sel();

    /**
     * Insert the object in database
     * @param activityList, object to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ActivityList activityList);

    /**
     * update the object in database
     * @param activityList, object to be updated
     */
    @Update
    void update(ActivityList activityList);

    /**
     * delete the object from database
     * @param activityList, object to be deleted
     */
    @Delete
    void delete(ActivityList activityList);



}
