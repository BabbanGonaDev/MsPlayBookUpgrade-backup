package com.babbangona.mspalybookupgrade.network;

import com.babbangona.mspalybookupgrade.network.object.ActivityListDownload;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitInterface {

    /**
     * This is the interface class that decides
     * */

    @GET("downloadActivityList")
    Call<ActivityListDownload> getActivityListDownload(@Query("last_synced_time") String last_synced_time);

    @FormUrlEncoded
    @POST("insert")
    Call<List<String>> sync1(@Field("field1") String tgl_data);

    @FormUrlEncoded
    @POST("yourPHPName.php")
    Call<String> sync2();
}
