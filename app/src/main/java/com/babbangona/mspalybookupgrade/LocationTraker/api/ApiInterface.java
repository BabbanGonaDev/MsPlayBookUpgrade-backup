package com.babbangona.mspalybookupgrade.LocationTraker.api;

import com.babbangona.mspalybookupgrade.LocationTraker.database.entity.StaffDetails;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("locationUpdate")
    Call<ArrayList<StaffDetails>> updateStaff(@Field("location_list") String data);
}
