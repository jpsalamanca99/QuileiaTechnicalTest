package com.example.quileia_technical_test.models;

import com.example.quileia_technical_test.app.MyApplication;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Appointment extends RealmObject {

    @PrimaryKey
    private int ID;

    private Patient patient;
    private Medic medic;
    private Date date;
    private boolean status;

    public Appointment() { } //Only for Realm

    public Appointment(Patient patient, Medic medic, Date date, boolean status) {
        this.ID = MyApplication.AppointmentID.incrementAndGet();
        this.patient = patient;
        this.medic = medic;
        this.date = date;
        this.status = status;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
