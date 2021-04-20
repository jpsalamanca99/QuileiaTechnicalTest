package com.example.quileia_technical_test.services;

import com.example.quileia_technical_test.models.Medic;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIInterface {

    @POST("post")
    Call<Medic> postMedics(@Body Medic medic);

}
