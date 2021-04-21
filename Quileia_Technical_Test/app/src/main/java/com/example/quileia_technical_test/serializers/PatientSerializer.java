package com.example.quileia_technical_test.serializers;

import com.example.quileia_technical_test.models.Medic;
import com.example.quileia_technical_test.models.Patient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class PatientSerializer implements JsonSerializer<Patient> {

    @Override
    public JsonElement serialize(Patient src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonPatient = new JsonObject();
        jsonPatient.addProperty("ID", src.getID());
        jsonPatient.addProperty("name", src.getName());
        jsonPatient.addProperty("lastName", src.getLastName());
        jsonPatient.addProperty("idNumber", src.getIdNumber());
        jsonPatient.addProperty("birthDate", src.getBirthDate().toString());
        jsonPatient.addProperty("moderatedFee", src.getModeratedFee());
        jsonPatient.addProperty("inTreatment", src.isInTreatment());
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Medic.class, new MedicSerializer())
                .create();
        jsonPatient.addProperty("medic", gson.toJson(src.getMedic()));
        return jsonPatient;
    }

}
