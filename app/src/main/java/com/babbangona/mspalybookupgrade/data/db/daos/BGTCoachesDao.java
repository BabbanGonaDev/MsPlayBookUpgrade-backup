package com.babbangona.mspalybookupgrade.data.db.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.babbangona.mspalybookupgrade.data.db.entities.BGTCoaches;

import java.util.List;

@Dao
public interface BGTCoachesDao {

    @Query(" SELECT coach_id FROM bgt_coaches WHERE bgt_id = :bgt_id ")
    String getCoach(String bgt_id);

    @Query(" SELECT bgt_name ||'__'|| bgt_id FROM bgt_coaches WHERE coach_id = :coach_id ")
    List<String> getBGTName(String coach_id);

    /**
     * Insert the object in database
     * @param bgtCoaches, object to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BGTCoaches bgtCoaches);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<BGTCoaches> bgtCoaches);

    /**
     * update the object in database
     * @param bgtCoaches, object to be updated
     */
    @Update
    void update(BGTCoaches bgtCoaches);

    /**
     * delete the object from database
     * @param bgtCoaches, object to be deleted
     */
    @Delete
    void delete(BGTCoaches bgtCoaches);



}
