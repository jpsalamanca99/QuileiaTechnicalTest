package com.example.quileia_technical_test.models;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Patient extends RealmObject {

    @PrimaryKey
    private int ID;
    @Required
    private String names;
    @Required
    private String lastNames;
    @Required
    private Date birthDate;
    @Required
    private String idNumber;
    @Required
    private Medic medic;
    @Required
    private boolean inTreatment;
    @Required
    private RealmList<Appointment>list;

    public Patient(){}

    public Patient(String names, String lastNames, Date birthDate, String idNumber, Medic medic, boolean inTreatment) {
        this.names = names;
        this.lastNames = lastNames;
        this.birthDate = birthDate;
        this.idNumber = idNumber;
        this.medic = medic;
        this.inTreatment = inTreatment;
    }

    public int getID() {
        return ID;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getLastNames() {
        return lastNames;
    }

    public void setLastNames(String lastNames) {
        this.lastNames = lastNames;
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

    public RealmList<Appointment> getList() {
        return list;
    }

    public void setList(RealmList<Appointment> list) {
        this.list = list;
    }
}
