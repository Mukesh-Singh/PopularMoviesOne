package com.movies.popular.api;

import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.lang.reflect.Modifier;
import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by mukesh on 17/2/16.
 */
public class RestClient {

    private static RestClient mInstance;
    private ApiServices apiService;

    private RestClient(){

    }
    public static synchronized RestClient getInstance() {
        if (mInstance == null)
            mInstance = new RestClient();
        return mInstance;
    }

    public ApiServices getApiServices() {
        if (apiService==null) {
            OkHttpClient client = new OkHttpClient();
            client.setReadTimeout(2, TimeUnit.MINUTES);
            client.setConnectTimeout(1, TimeUnit.MINUTES);
//rest adapter
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiConstants.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                            .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                        /*.serializeNulls()*/
                            .create()))
                    .build();

            apiService = retrofit.create(ApiServices.class);
        }
        return apiService;
    }
}
