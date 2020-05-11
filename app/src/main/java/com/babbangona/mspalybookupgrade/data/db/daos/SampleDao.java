package com.babbangona.mspalybookupgrade.data.db.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.babbangona.mspalybookupgrade.data.db.entities.SampleEntity;

import java.util.List;


//TODO: Modify the queries hear to suit your applications needs
@Dao
public interface SampleDao {


    @Query(" select * from table1 ")
    List<SampleEntity> sel();



    /*
     * Insert the object in database
     * @param data, object to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SampleEntity insert);

    /*
     * update the object in database
     * @param data, object to be updated
     */
    @Update
    void update(SampleEntity update);

    /*
     * delete the object from database
     * @param data, object to be deleted
     */
    @Delete
    void delete(SampleEntity delete);



}
