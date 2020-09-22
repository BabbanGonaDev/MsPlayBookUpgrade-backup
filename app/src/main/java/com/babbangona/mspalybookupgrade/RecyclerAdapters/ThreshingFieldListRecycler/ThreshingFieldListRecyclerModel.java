package com.babbangona.mspalybookupgrade.RecyclerAdapters.ThreshingFieldListRecycler;

public class ThreshingFieldListRecyclerModel {

    private String unique_field_id;

    private String village;

    private String field_size;

    public ThreshingFieldListRecyclerModel(String unique_field_id, String village) {
        this.unique_field_id = unique_field_id;
        this.village = village;
        this.field_size = field_size;
    }

    public String getUnique_field_id() {
        return unique_field_id;
    }

    public void setUnique_field_id(String unique_field_id) {
        this.unique_field_id = unique_field_id;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getField_size() {
        return field_size;
    }

    public void setField_size(String field_size) {
        this.field_size = field_size;
    }
}
