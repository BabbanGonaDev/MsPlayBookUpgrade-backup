package com.babbangona.standardtemplate.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitInterface {

    /**
     * This is the interface class that decides
     * */

    @FormUrlEncoded
    @POST("insert")
    Call<List<String>> sync1(@Field("field1") String tgl_data);


    @FormUrlEncoded
    @POST("yourPHPName.php")
    Call<String> sync2();
}
