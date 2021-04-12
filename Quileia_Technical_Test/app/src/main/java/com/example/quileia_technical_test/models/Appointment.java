package com.example.quileia_technical_test.models;

import io.realm.RealmObject;

public class Appointment extends RealmObject {

    private Patient patient;
    private Medic medic;

    public Appointment(){ }

    public Appointment(Patient patient, Medic medic) {
        this.patient = patient;
        this.medic = medic;
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
