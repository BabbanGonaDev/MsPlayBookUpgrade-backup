package com.babbangona.mspalybookupgrade.data.db.daos;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.babbangona.mspalybookupgrade.RecyclerAdapters.SetPortfolioRecycler.SetPortfolioRecyclerModel;
import com.babbangona.mspalybookupgrade.data.db.entities.StaffList;

import java.util.List;

@Dao
public abstract class StaffListDao {

    @Query(" SELECT a.staff_id, a.staff_name, a.staff_hub, null as selected FROM staff a " +
            "LEFT JOIN fields b ON b.staff_id = a.staff_id " +
            "WHERE b.staff_id IS NULL ORDER BY a.staff_id ASC")
    public abstract DataSource.Factory<Integer, SetPortfolioRecyclerModel> getAllStaffNotAdded();

    @Query(" SELECT a.staff_id, a.staff_name, a.staff_hub, null as selected FROM staff a " +
            "LEFT JOIN fields b ON b.staff_id = a.staff_id " +
            "WHERE b.staff_id IS NULL AND LOWER(a.staff_id || ' ' || a.staff_name || ' ' || a.staff_hub) LIKE LOWER(:search) ORDER BY a.staff_id ASC")
    public abstract DataSource.Factory<Integer, SetPortfolioRecyclerModel> getAllStaffNotAdded(String search);

    @Query(" SELECT DISTINCT a.staff_id, a.staff_name, a.staff_hub, null as selected FROM staff a " +
            "JOIN fields b ON a.staff_id = b.staff_id WHERE b.deactivate = '0' " +
            "AND LOWER(b.staff_id || b.mss) LIKE LOWER(:staff_id) ORDER BY a.staff_id ASC")
    public abstract DataSource.Factory<Integer, SetPortfolioRecyclerModel> getAllStaffAdded(String staff_id);

    @Query(" SELECT DISTINCT a.staff_id, a.staff_name, a.staff_hub, null as selected FROM staff a " +
            "JOIN fields b ON a.staff_id = b.staff_id WHERE b.deactivate = '0' AND " +
            "LOWER(a.staff_id || ' ' || a.staff_name || ' ' || a.staff_hub) LIKE LOWER(:search) " +
            "AND LOWER(b.staff_id || b.mss) LIKE LOWER(:staff_id) ORDER BY a.staff_id ASC")
    public abstract DataSource.Factory<Integer, SetPortfolioRecyclerModel> getAllStaffAdded(String staff_id, String search);

    @Query("SELECT staff_name FROM staff WHERE staff_id = :staff_id")
    public abstract String getStaffName(String staff_id);

    /**
     * Insert the object in database
     * @param staffList, object to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(StaffList staffList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<StaffList> staffLists);

    /**
     * update the object in database
     * @param staffList, object to be updated
     */
    @Update
    public abstract void update(StaffList staffList);

    /**
     * delete the object from database
     * @param staffList, object to be deleted
     */
    @Delete
    public abstract void delete(StaffList staffList);
}
