package com.babbangona.mspalybookupgrade.RecyclerAdapters.ThreshingFieldListRecycler;

public class ThreshingFieldListRecyclerModel {

    private String unique_field_id;

    private String village;

    private String field_size;

    private String staff_id;

    public ThreshingFieldListRecyclerModel(String unique_field_id, String village, String field_size,
                                           String staff_id) {
        this.unique_field_id = unique_field_id;
        this.village = village;
        this.field_size = field_size;
        this.staff_id = staff_id;
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

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }
}
