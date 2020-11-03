package com.babbangona.mspalybookupgrade.LocationTraker.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
The API client class, is the builder of the Api Client which is first called, before the POST AND GET COMMANDS
The only things that should be changed is the base url, remember to begin with the http://
 */

public class ApiClient {

    private static final String BASE_URL = "https://apps.babbangona.com/bgt_location_tracker_slim/public/api/v1/";
    private static Retrofit retrofit = null;


    public static Retrofit getApiClient() {

        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .setDateFormat("yyyy-MM-dd")
                    .create();
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();


            retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build(); //.client(client)

        }

        return retrofit;
    }
}
