package com.babbangona.mspalybookupgrade.data.db.daos;

import androidx.lifecycle.LiveData;
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


    @Query(" SELECT * FROM activity_list WHERE deactivated = '0' AND user_category LIKE :app_role " +
            "AND language_id = :language_id ORDER BY activity_id ASC ")
    LiveData<List<ActivityList>> getAllActivityList(String language_id, String app_role);

    @Query(" SELECT COUNT(activity_id) FROM activity_list")
    int countActivities();

    /**
     * Insert the object in database
     * @param activityList, object to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ActivityList activityList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<ActivityList> activityList);

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
