package com.babbangona.mspalybookupgrade.transporter.data.room.tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(tableName = "cards_table", primaryKeys = {"account_number", "card_name", "card_number"})
public class CardsTable {

    private String id;
    @NonNull
    private String account_number;
    @NonNull
    private String card_name;
    private String product_code;
    private String branch_number;
    @NonNull
    private String card_number;

    public CardsTable(String id, @NonNull String account_number, @NonNull String card_name, String product_code, String branch_number, @NonNull String card_number) {
        this.id = id;
        this.account_number = account_number;
        this.card_name = card_name;
        this.product_code = product_code;
        this.branch_number = branch_number;
        this.card_number = card_number;
    }

    public String getId() {
        return id;
    }

    @NonNull
    public String getAccount_number() {
        return account_number;
    }

    @NonNull
    public String getCard_name() {
        return card_name;
    }

    public String getProduct_code() {
        return product_code;
    }

    public String getBranch_number() {
        return branch_number;
    }

    @NonNull
    public String getCard_number() {
        return card_number;
    }

    /*
     * Tuple for sync down
     */

    public static class Download {
        public String id;
        public String account_number;
        public String card_name;
        public String product_code;
        public String branch_number;
        public String card_number;
        public String last_sync;
    }
}
