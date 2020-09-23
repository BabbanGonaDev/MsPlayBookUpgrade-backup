package com.babbangona.mspalybookupgrade.data.db.daos;

import androidx.paging.DataSource;
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

    @Query(" SELECT template FROM members WHERE unique_member_id = :unique_member_id ")
    String getMemberTemplate(String unique_member_id);

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
     *
     * @param members, object to be deleted
     */
    @Delete
    void delete(Members members);

    /**
     * =============================================
     * Functions below this line are used in the Transporter application module.
     * =============================================
     */
    @Query("SELECT a.unique_member_id AS uniqueMemberId, a.first_name || ' ' || a.last_name AS fullName," +
            " a.ik_number AS ikNumber, a.member_id AS memberId, a.village_name AS villageName, SUM(b.field_size) AS fieldSize FROM members AS a" +
            " JOIN fields AS b ON a.unique_member_id = b.unique_member_id" +
            " WHERE b.deactivate = '0' GROUP BY b.unique_member_id")
    DataSource.Factory<Integer, com.babbangona.mspalybookupgrade.transporter.data.models.Members> getAllMembers();

    @Query("SELECT a.unique_member_id AS uniqueMemberId, a.first_name || ' ' || a.last_name AS fullName," +
            " a.ik_number AS ikNumber, a.member_id AS memberId, a.village_name AS villageName, SUM(b.field_size) AS fieldSize FROM members AS a" +
            " JOIN fields AS b ON a.unique_member_id = b.unique_member_id" +
            " WHERE b.deactivate = '0' AND (LOWER(a.ik_number) LIKE :filter OR LOWER(a.first_name) LIKE :filter OR LOWER(a.last_name) LIKE :filter) GROUP BY b.unique_member_id")
    DataSource.Factory<Integer, com.babbangona.mspalybookupgrade.transporter.data.models.Members> getAllMembersFilter(String filter);
}
