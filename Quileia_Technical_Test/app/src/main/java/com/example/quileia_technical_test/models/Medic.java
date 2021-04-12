package com.example.quileia_technical_test.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Medic extends RealmObject {

    @PrimaryKey
    private int ID;
    @Required
    private String proCardCode;
    @Required
    private String speciality;
    @Required
    private float experienceYears;
    @Required
    private String office;
    @Required
    private boolean domicile;

    public Medic() { } //Only for Realm

    public Medic(String proCardCode, String speciality, float experienceYears, String office, boolean domicile) {
        this.proCardCode = proCardCode;
        this.speciality = speciality;
        this.experienceYears = experienceYears;
        this.office = office;
        this.domicile = domicile;
    }

    public int getID() {
        return ID;
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
}
