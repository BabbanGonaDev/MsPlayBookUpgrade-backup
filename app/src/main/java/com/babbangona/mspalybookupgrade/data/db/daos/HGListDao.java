package com.babbangona.mspalybookupgrade.data.db.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.babbangona.mspalybookupgrade.data.db.entities.HGList;

import java.util.List;

@Dao
public interface HGListDao {

    @Query(" SELECT hg_type FROM hg_list WHERE deactivated_status = '0' " +
            "AND user_category LIKE :staff_role ")
    List<String> getAllHGs(String staff_role);

    /**
     * Insert the object in database
     * @param hgList, object to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HGList hgList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<HGList> hgList);

    /**
     * update the object in database
     * @param hgList, object to be updated
     */
    @Update
    void update(HGList hgList);

    /**
     * delete the object from database
     * @param hgList, object to be deleted
     */
    @Delete
    void delete(HGList hgList);



}
