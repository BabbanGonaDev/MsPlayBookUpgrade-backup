package com.babbangona.mspalybookupgrade.data.db.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.babbangona.mspalybookupgrade.data.db.entities.Fields;
import com.babbangona.mspalybookupgrade.data.db.entities.NormalActivitiesFlag;

import java.util.List;

@Dao
public interface FieldsDao {

    @Query(" SELECT COUNT(unique_field_id) FROM fields WHERE deactivate = '0' " +
            "AND staff_id = :staff_id ")
    int getTotalFieldsCount(String staff_id);

    /**
     * Insert the object in database
     * @param fields, object to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Fields fields);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Fields> fields);

    /**
     * update the object in database
     * @param fields, object to be updated
     */
    @Update
    void update(Fields fields);

    /**
     * delete the object from database
     * @param fields, object to be deleted
     */
    @Delete
    void delete(Fields fields);



}
