package com.example.quileia_technical_test.services;

import com.example.quileia_technical_test.models.Appointment;
import com.example.quileia_technical_test.models.Medic;
import com.example.quileia_technical_test.models.Patient;

import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIInterface {

    @POST("post")
    Call<RealmResults<Medic>> postMedics(@Body RealmResults<Medic> medics);

    @POST("post")
    Call<RealmResults<Patient>> postPatients(@Body RealmResults<Patient> patients);

    @POST("post")
    Call<RealmResults<Appointment>> postAppointments(@Body RealmResults<Appointment> appointments);

}
