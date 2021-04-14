package com.example.quileia_technical_test.app;

import android.app.Application;

import com.example.quileia_technical_test.models.Appointment;
import com.example.quileia_technical_test.models.Medic;
import com.example.quileia_technical_test.models.Patient;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class MyApplication extends Application {

    public static AtomicInteger PatientID = new AtomicInteger();
    public static AtomicInteger MedicID = new AtomicInteger();
    public static AtomicInteger AppointmentID = new AtomicInteger();

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(getApplicationContext());
        setUpRealmConfig();
        Realm realm = Realm.getDefaultInstance();
        PatientID = getIdByTable(realm, Patient.class);
        MedicID = getIdByTable(realm, Medic.class);
        AppointmentID = getIdByTable(realm, Appointment.class);
        realm.close();
    }

    private void setUpRealmConfig(){
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    private <T extends RealmObject> AtomicInteger getIdByTable(Realm realm, Class<T> anyClass){
        RealmResults<T> results = realm.where(anyClass).findAll();
        return (results.size() > 0) ? new AtomicInteger(results.max("ID").intValue()) : new AtomicInteger();
    }

}
