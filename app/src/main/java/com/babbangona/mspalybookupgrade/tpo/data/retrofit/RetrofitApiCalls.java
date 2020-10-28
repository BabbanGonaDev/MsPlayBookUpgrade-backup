package com.babbangona.mspalybookupgrade.tpo.data.retrofit;

import com.babbangona.mspalybookupgrade.tpo.data.models.DeliveryDownload;
import com.babbangona.mspalybookupgrade.tpo.data.room.tables.DeliveryAttendance;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitApiCalls {

    @GET("downloadTpoDeliveryAttendance")
    Call<List<DeliveryDownload>> syncDownDeliveryAttendance(@Query("last_sync_time") String last_sync);

    @FormUrlEncoded
    @POST("uploadTpoDeliveryAttendance")
    Call<List<DeliveryAttendance.Response>> syncUpDeliveryAttendance(@Field("delivery_list") String list);
}
