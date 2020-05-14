package com.babbangona.mspalybookupgrade.RecyclerAdapters.SetPortfolioRecycler;

public class SetPortfolioRecyclerModel {

    private String staff_id;

    private String staff_name;

    private String staff_hub;

    private boolean selected;

    public SetPortfolioRecyclerModel(String staff_id, String staff_name, String staff_hub) {
        this.staff_id = staff_id;
        this.staff_name = staff_name;
        this.staff_hub = staff_hub;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    public String getStaff_hub() {
        return staff_hub;
    }

    public void setStaff_hub(String staff_hub) {
        this.staff_hub = staff_hub;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
