package com.example.quileia_technical_test.models;

import com.example.quileia_technical_test.app.MyApplication;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Appointment extends RealmObject {

    @PrimaryKey
    private int ID;

    private Patient patient;
    private Medic medic;

    public Appointment() { } //Only for Realm

    public Appointment(Patient patient, Medic medic) {
        this.ID = MyApplication.AppointmentID.incrementAndGet();
        this.patient = patient;
        this.medic = medic;
    }

    public int getID() {
        return ID;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Medic getMedic() {
        return medic;
    }

    public void setMedic(Medic medic) {
        this.medic = medic;
    }
}
