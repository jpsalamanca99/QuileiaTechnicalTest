package com.example.quileia_technical_test.serializers;

import com.example.quileia_technical_test.models.Appointment;
import com.example.quileia_technical_test.models.Medic;
import com.example.quileia_technical_test.models.Patient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class AppointmentSerializer implements JsonSerializer<Appointment> {

    @Override
    public JsonElement serialize(Appointment src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonAppointment = new JsonObject();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Medic.class, new MedicSerializer())
                .registerTypeAdapter(Patient.class, new PatientSerializer())
                .create();
        jsonAppointment.addProperty("medic", gson.toJson(src.getMedic()));
        jsonAppointment.addProperty("patient", gson.toJson(src.getPatient()));
        jsonAppointment.addProperty("date", src.getDate().toString());
        return jsonAppointment;
    }
}
