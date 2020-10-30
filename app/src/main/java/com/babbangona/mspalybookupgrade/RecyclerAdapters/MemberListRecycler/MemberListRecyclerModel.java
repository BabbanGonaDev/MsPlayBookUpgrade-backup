package com.babbangona.mspalybookupgrade.RecyclerAdapters.MemberListRecycler;

import android.os.Parcel;
import android.os.Parcelable;

public class MemberListRecyclerModel {

    private String unique_member_id;

    private String member_name;

    private String role;

    private String village;

    private String ik_number;

    private String member_r_id;

    private String staff_id;

    private String phone_number;

    public MemberListRecyclerModel(String unique_member_id, String member_name, String role,
                                   String village, String ik_number, String member_r_id,
                                   String staff_id, String phone_number) {
        this.unique_member_id = unique_member_id;
        this.member_name = member_name;
        this.role = role;
        this.village = village;
        this.ik_number = ik_number;
        this.member_r_id = member_r_id;
        this.staff_id = staff_id;
        this.phone_number = phone_number;
    }

    public String getUnique_member_id() {
        return unique_member_id;
    }

    public void setUnique_member_id(String unique_member_id) {
        this.unique_member_id = unique_member_id;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getIk_number() {
        return ik_number;
    }

    public void setIk_number(String ik_number) {
        this.ik_number = ik_number;
    }

    public String getMember_r_id() {
        return member_r_id;
    }

    public void setMember_r_id(String member_r_id) {
        this.member_r_id = member_r_id;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public static class TemplateModel implements Parcelable {

        private String template;
        private String image_person_small;
        private String image_person_large;
        private String result;

        public TemplateModel() {
            this.template = "";
            this.image_person_small = "";
            this.image_person_large = "";
            this.result = "0";
        }

        public TemplateModel(String template, String image_person_small, String image_person_large, String result) {
            this.template = template;
            this.image_person_small = image_person_small;
            this.image_person_large = image_person_large;
            this.result = result;
        }

        public TemplateModel(Parcel in){
            String[] data = new String[4];

            in.readStringArray(data);
            // the order needs to be the same as in writeToParcel() method
            this.template  = data[0];
            this.image_person_small  = data[1];
            this.image_person_large = data[2];
            this.result = data[3];
        }

        public String getTemplate() {
            return template;
        }

        public void setTemplate(String template) {
            this.template = template;
        }

        public String getImage_person_small() {
            return image_person_small;
        }

        public void setImage_person_small(String image_person_small) {
            this.image_person_small = image_person_small;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getImage_person_large() {
            return image_person_large;
        }

        public void setImage_person_large(String image_person_large) {
            this.image_person_large = image_person_large;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeStringArray(new String[] {
                    this.template,
                    this.image_person_small,
                    this.image_person_large,
                    this.result
            });
        }

        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
            public TemplateModel createFromParcel(Parcel in) {
                return new TemplateModel(in);
            }

            public TemplateModel[] newArray(int size) {
                return new TemplateModel[size];
            }
        };
    }
}
