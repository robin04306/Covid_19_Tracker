package com.example.covid_19tracker;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api_interface {

    String base_url ="https://corona.lmao.ninja/v2/";
    @GET("countries")
    Call<List<model_class>> getcountrydata();

}
