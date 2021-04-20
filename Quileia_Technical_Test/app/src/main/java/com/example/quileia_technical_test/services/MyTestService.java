package com.example.quileia_technical_test.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.quileia_technical_test.models.Medic;
import com.example.quileia_technical_test.serializers.MedicSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyTestService extends IntentService {

    private Realm realm;
    private Gson gson;
    private Retrofit retrofit;
    private APIInterface service;
    private Call<Medic> medicsCall;

    public MyTestService() {
        super("MyTestService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        setUpProperties();
        postMedics();
    }

    public void setUpProperties(){
        if (realm == null) realm = Realm.getDefaultInstance();
        if (gson == null) gson = new GsonBuilder()
                .registerTypeAdapter(Medic.class, new MedicSerializer())
                .create();
        if (retrofit == null) retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.12:3000/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        if (service == null) service = retrofit.create(APIInterface.class);
        if(medicsCall == null) medicsCall = service.postMedics(realm.where(Medic.class).findFirst());
        else medicsCall = medicsCall.clone();
    }

    public void postMedics() {
        medicsCall.enqueue(new Callback<Medic>() {
            @Override
            public void onResponse(Call<Medic> call, Response<Medic> response) {
                Toast.makeText(getApplicationContext(), "Info recibida", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Medic> call, Throwable t) {
                Log.e("ERROR", "----------" + t.toString() + "-----------");
            }
        });
    }

    public void postPatients() {

        APIInterface service = retrofit.create(APIInterface.class);
        Call<Medic> call = service.postMedics(realm.where(Medic.class).findFirst());

        call.enqueue(new Callback<Medic>() {
            @Override
            public void onResponse(Call<Medic> call, Response<Medic> response) {
                Toast.makeText(getApplicationContext(), "Info recibida", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Medic> call, Throwable t) {
                Log.e("ERROR", "----------" + t.toString() + "-----------");
            }
        });
    }

    public void postAppointments() {

        APIInterface service = retrofit.create(APIInterface.class);
        Call<Medic> call = service.postMedics(realm.where(Medic.class).findFirst());

        call.enqueue(new Callback<Medic>() {
            @Override
            public void onResponse(Call<Medic> call, Response<Medic> response) {
                Toast.makeText(getApplicationContext(), "Info recibida", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Medic> call, Throwable t) {
                Log.e("ERROR", "----------" + t.toString() + "-----------");
            }
        });
    }
}