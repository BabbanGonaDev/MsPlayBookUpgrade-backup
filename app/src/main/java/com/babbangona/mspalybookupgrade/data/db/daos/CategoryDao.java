package com.babbangona.mspalybookupgrade.data.db.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.babbangona.mspalybookupgrade.data.db.entities.Category;
import com.babbangona.mspalybookupgrade.data.db.entities.Members;

import java.util.List;

@Dao
public interface CategoryDao {

    @Query(" SELECT category FROM category WHERE role = :role ")
    String getRoleCategory(String role);

    /**
     * Insert the object in database
     * @param category, object to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Category category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Category> category);

    /**
     * update the object in database
     * @param category, object to be updated
     */
    @Update
    void update(Category category);

    /**
     * delete the object from database
     * @param category, object to be deleted
     */
    @Delete
    void delete(Category category);



}
