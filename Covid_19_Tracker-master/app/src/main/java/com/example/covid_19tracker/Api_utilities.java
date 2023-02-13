package com.example.covid_19tracker;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api_utilities {

    public static Retrofit retrofit = null;

    public static Api_interface getApiInterface()
    {
        if (retrofit==null)
        {
            retrofit= new Retrofit.Builder().baseUrl(Api_interface.base_url).addConverterFactory(GsonConverterFactory.create()).build();

        }
        return retrofit.create(Api_interface.class);
    }


}
