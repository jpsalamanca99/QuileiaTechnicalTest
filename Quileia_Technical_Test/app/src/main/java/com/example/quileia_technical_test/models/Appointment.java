package com.example.quileia_technical_test.models;

import android.content.Context;
import android.widget.Toast;

import com.example.quileia_technical_test.app.MyApplication;

import java.util.Date;

import io.realm.Realm;
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

    public Appointment(Patient patient, Medic medic, Date date) {
        this.ID = MyApplication.AppointmentID.incrementAndGet();
        this.patient = patient;
        this.medic = medic;
        this.date = date;
        this.status = false;
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

    /*CRUD actions*/
    /*Create appointment*/
    public static void createAppointment(Realm realm, Medic medic, Patient patient, Date date){
        realm.beginTransaction();
        Appointment appointment = new Appointment(patient, medic, date);
        realm.copyToRealm(appointment);
        medic.getAppointments().add(appointment);
        patient.getAppointments().add(appointment);
        realm.commitTransaction();
    }
    /*Edit appointment from the medic list*/
    public static void editAppointment(Realm realm, Appointment appointment, Patient patient, Date date){
        realm.beginTransaction();

        //Remove from previous patient
        Patient prevPatient = appointment.getPatient();
        prevPatient.getAppointments().remove(prevPatient.getAppointments().indexOf(appointment));

        //Add to the new patient
        patient.getAppointments().add(appointment);
        appointment.setPatient(patient);
        appointment.setDate(date);
        realm.copyToRealmOrUpdate(appointment);
        realm.commitTransaction();
    }
    /*Edit appointment from patient view*/
    public static void editAppointment(Realm realm, Appointment appointment, Medic medic, Date date){
        realm.beginTransaction();

        //Remove from the previous medic
        Medic prevMedic = appointment.getMedic();
        prevMedic.getAppointments().remove(prevMedic.getAppointments().indexOf(appointment));

        //Add to the new medic
        medic.getAppointments().add(appointment);
        appointment.setMedic(medic);
        appointment.setDate(date);
        realm.copyToRealmOrUpdate(appointment);
        realm.commitTransaction();
    }
    /*Delete appointment*/
    public static void deleteAppointment(Realm realm, Appointment appointment){
        realm.beginTransaction();
        appointment.deleteFromRealm();
        realm.commitTransaction();
    }



}
