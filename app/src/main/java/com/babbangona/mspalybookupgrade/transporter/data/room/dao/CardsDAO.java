package com.babbangona.mspalybookupgrade.transporter.data.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.babbangona.mspalybookupgrade.transporter.data.room.tables.CardsTable;

import java.util.List;

@Dao
public interface CardsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCardsList(List<CardsTable> cards_list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSingleCard(CardsTable card);

    @Query("SELECT * FROM cards_table")
    List<CardsTable> getAllCards();

    @Query("SELECT * FROM cards_table WHERE card_number = :cardNumber")
    CardsTable getSingleCard(String cardNumber);

    @Query("SELECT COUNT(*) FROM cards_table LIMIT 500")
    Integer getCardsTableCount();

}
