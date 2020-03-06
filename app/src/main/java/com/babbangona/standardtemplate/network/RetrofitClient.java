package com.babbangona.standardtemplate.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {



    private static Retrofit retrofit;

    /**
     *  Creation of retrofit client for network calls
     * */
    public static RetrofitInterface getRetrofit(){
        if(retrofit==null){


            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.addInterceptor(httpLoggingInterceptor);
            OkHttpClient okHttpClient = builder.build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit= new Retrofit.Builder()
                    //TODO: Change the base URL below to the one specific to your application
                    .baseUrl("https://apps.yoururl.com/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
        }
        return retrofit.create(RetrofitInterface.class);
    }
}
