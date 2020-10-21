package com.babbangona.mspalybookupgrade.donotpay.data.retrofit;

import com.babbangona.mspalybookupgrade.donotpay.data.room.tables.DoNotPayReasonsTable;
import com.babbangona.mspalybookupgrade.donotpay.data.room.tables.DoNotPayTable;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitApiCalls {

    @GET("downloadDoNotPayReason")
    Call<List<DoNotPayReasonsTable>> syncDownDoNotPayReasons(@Query("last_sync_time") String last_sync);

    @GET("downloadDoNotPayRecords")
    Call<List<DoNotPayTable>> syncDownDoNotPay(@Query("last_sync_time") String last_sync);

    @FormUrlEncoded
    @POST("uploadDoNotPayRecords")
    Call<List<DoNotPayTable.Response>> syncUpDoNotPay(@Field("donotpay_list") String list);

}
