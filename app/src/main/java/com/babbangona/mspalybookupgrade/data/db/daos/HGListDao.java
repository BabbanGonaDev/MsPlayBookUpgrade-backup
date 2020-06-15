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

    @Query(" SELECT DISTINCT hg_type FROM hg_list WHERE deactivated_status = '0' " +
            "AND user_category LIKE :role_category ")
    List<String> getAllHGs(String role_category);

    @Query(" SELECT DISTINCT sub_hg_type FROM hg_list WHERE deactivated_status = '0' " +
            "AND hg_type LIKE :hg_type ")
    List<String> getHGSubTypes(String hg_type);

    @Query(" SELECT user_category FROM hg_list WHERE hg_type = :hg_type ")
    String getHGRoleCategory(String hg_type);

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
