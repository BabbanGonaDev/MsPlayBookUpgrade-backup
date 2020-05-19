package com.babbangona.mspalybookupgrade.data.db.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.babbangona.mspalybookupgrade.data.db.entities.Members;

import java.util.List;

@Dao
public interface MembersDao {

    @Query(" SELECT COUNT(unique_field_id) FROM fields WHERE deactivate = '0' " +
            "AND staff_id = :staff_id ")
    int getTotalFieldsCount(String staff_id);

    /**
     * Insert the object in database
     * @param members, object to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Members members);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Members> members);

    /**
     * update the object in database
     * @param members, object to be updated
     */
    @Update
    void update(Members members);

    /**
     * delete the object from database
     * @param members, object to be deleted
     */
    @Delete
    void delete(Members members);



}
