package com.example.quileia_technical_test.models;

import android.widget.Toast;

import com.example.quileia_technical_test.app.MyApplication;
import com.google.gson.annotations.Expose;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class Medic extends RealmObject {

    @PrimaryKey
    private int ID;
    private String name;
    private String lastName;
    private String proCardCode;
    private String speciality;
    private float experienceYears;
    private String office;
    private boolean domicile;

    private RealmList<Appointment> appointments;

    public Medic() { } //Only for Realm

    public Medic(String name, String lastName, String proCardCode, String speciality, float experienceYears, String office, boolean domicile) {
        this.ID = MyApplication.MedicID.incrementAndGet();
        this.name = name;
        this.lastName = lastName;
        this.proCardCode = proCardCode;
        this.speciality = speciality;
        this.experienceYears = experienceYears;
        this.office = office;
        this.domicile = domicile;
        this.appointments = new RealmList<Appointment>();
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

    public String getProCardCode() {
        return proCardCode;
    }

    public void setProCardCode(String proCardCode) {
        this.proCardCode = proCardCode;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public float getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(float experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public boolean isDomicile() {
        return domicile;
    }

    public void setDomicile(boolean domicile) {
        this.domicile = domicile;
    }

    public RealmList<Appointment> getAppointments() {
        return appointments;
    }

    public String getFullName(){
        return lastName + " " + name;
    }

    /*CRUD actions*/
    /*Edit medic*/
    public static void editMedic(Realm realm, Medic medic, String name, String lastName, String proCardCode, String speciality, float experienceYears, String office, boolean domicile){
        realm.beginTransaction();
        medic.setName(name);
        medic.setLastName(lastName);
        medic.setProCardCode(proCardCode);
        medic.setSpeciality(speciality);
        medic.setExperienceYears(experienceYears);
        medic.setOffice(office);
        medic.setDomicile(domicile);
        realm.copyToRealmOrUpdate(medic);
        realm.commitTransaction();
    }
    /*Create medic*/
    public static void createNewMedic(Realm realm, String name, String lastName, String proCardCode, String speciality, float experienceYears, String office, boolean domicile) {
        realm.beginTransaction();
        Medic medic = new Medic(name, lastName, proCardCode, speciality, experienceYears, office, domicile);
        realm.copyToRealm(medic);
        realm.commitTransaction();
    }
    /*Delete medic*/
    public static void deleteMedic(Realm realm, Medic medic){
        realm.beginTransaction();
        RealmList<Appointment> appointments = medic.getAppointments();
        if (appointments != null){
            for (Appointment appointment: appointments) {
                appointment.deleteFromRealm();
            }
        }
        medic.deleteFromRealm();
        realm.commitTransaction();
    }
}
