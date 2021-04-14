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

    public Patient(String name, String lastName, Date birthDate, String idNumber, Medic medic, boolean inTreatment, double moderatedFee) {
        this.ID = MyApplication.PatientID.incrementAndGet();
        this.name = name;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.idNumber = idNumber;
        this.medic = medic;
        this.inTreatment = inTreatment;
        this.moderatedFee = moderatedFee;
        this.newAppointment = new Date();
        this.list = new RealmList<Appointment>();
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Medic getMedic() {
        return medic;
    }

    public void setMedic(Medic medic) {
        this.medic = medic;
    }

    public boolean isInTreatment() {
        return inTreatment;
    }

    public void setInTreatment(boolean inTreatment) {
        this.inTreatment = inTreatment;
    }

    public double getModeratedFee() {
        return moderatedFee;
    }

    public void setModeratedFee(double moderatedFee) {
        this.moderatedFee = moderatedFee;
    }

    public Date getNewAppointment() {
        return newAppointment;
    }

    public void setNewAppointment(Date newAppointment) {
        this.newAppointment = newAppointment;
    }

    public RealmList<Appointment> getList() {
        return list;
    }

}
