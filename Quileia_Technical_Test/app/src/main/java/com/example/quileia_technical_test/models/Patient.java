package com.example.quileia_technical_test.models;

import com.example.quileia_technical_test.app.MyApplication;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Patient extends RealmObject {

    @PrimaryKey
    private int ID;

    private String name;
    private String lastName;
    private Date birthDate;
    private String idNumber;
    private Medic medic;
    private boolean inTreatment;
    private double moderatedFee;
    private Date newAppointment;

    private RealmList<Appointment>list;

    public Patient() { } //Only for Realm

    public Patient(String name, String lastNames, Date birthDate, String idNumber, Medic medic, boolean inTreatment, double moderatedFee, Date newAppointment) {
        this.ID = MyApplication.PatientID.incrementAndGet();
        this.name = name;
        this.lastName = lastNames;
        this.birthDate = birthDate;
        this.idNumber = idNumber;
        this.medic = medic;
        this.inTreatment = inTreatment;
        this.moderatedFee = moderatedFee;
        this.newAppointment = newAppointment;
        this.list = new RealmList<Appointment>();
    }

}
