package com.babbangona.mspalybookupgrade.data.db.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.babbangona.mspalybookupgrade.data.db.entities.PWSCategoryList;
import com.babbangona.mspalybookupgrade.data.db.entities.RFList;

import java.util.List;

@Dao
public interface PWSCategoryListDao {

    @Query(" SELECT DISTINCT pws_category FROM pws_category_list WHERE deactivated_status = '0' " +
            "AND LOWER(user_category) = :role ")
    List<String> getAllPWSCategories(String role);

    @Query(" SELECT DISTINCT user_category FROM rf_list WHERE rf_type = :rf_type ")
    String getHGRoleCategory(String rf_type);

    /**
     * Insert the object in database
     * @param pwsCategoryList, object to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PWSCategoryList pwsCategoryList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<PWSCategoryList> pwsCategoryLists);

    /**
     * update the object in database
     * @param rfList, object to be updated
     */
    @Update
    void update(RFList rfList);

    /**
     * delete the object from database
     * @param rfList, object to be deleted
     */
    @Delete
    void delete(RFList rfList);



}
