package com.example.quileia_technical_test.serializers;

import android.util.Log;

import com.example.quileia_technical_test.models.Medic;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class MedicSerializer implements JsonSerializer<Medic> {

    @Override
    public JsonElement serialize(Medic src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonMedic = new JsonObject();
        jsonMedic.addProperty("ID", src.getID());
        jsonMedic.addProperty("name", src.getName());
        jsonMedic.addProperty("lastName", src.getLastName());
        jsonMedic.addProperty("proCardCode", src.getProCardCode());
        jsonMedic.addProperty("speciality", src.getSpeciality());
        jsonMedic.addProperty("expYears", src.getExperienceYears());
        jsonMedic.addProperty("office", src.getOffice());
        jsonMedic.addProperty("domicile", src.isDomicile());
        return jsonMedic;
    }

}
