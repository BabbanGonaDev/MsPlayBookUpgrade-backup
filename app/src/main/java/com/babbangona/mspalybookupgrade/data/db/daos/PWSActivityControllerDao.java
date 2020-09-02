package com.babbangona.mspalybookupgrade.data.db.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.babbangona.mspalybookupgrade.data.db.entities.PWSActivityController;

import java.util.List;

@Dao
public interface PWSActivityControllerDao {

    @Query(" SELECT category FROM pws_activity_controller WHERE role = :role ")
    String getRoleCategory(String role);

    /**
     * Insert the object in database
     * @param pwsActivityController, object to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PWSActivityController pwsActivityController);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<PWSActivityController> pwsActivityController);

    /**
     * update the object in database
     * @param pwsActivityController, object to be updated
     */
    @Update
    void update(PWSActivityController pwsActivityController);

    /**
     * delete the object from database
     * @param pwsActivityController, object to be deleted
     */
    @Delete
    void delete(PWSActivityController pwsActivityController);



}
