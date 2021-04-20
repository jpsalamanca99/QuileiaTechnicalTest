package com.example.quileia_technical_test.activitys;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.quileia_technical_test.R;
import com.example.quileia_technical_test.models.Appointment;
import com.example.quileia_technical_test.models.Medic;
import com.example.quileia_technical_test.models.Patient;
import com.example.quileia_technical_test.services.APIInterface;
import com.example.quileia_technical_test.services.MyAlarmReceiver;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Realm realm;

    private Button patientsButton;
    private Button medicsButton;
    private Button appointmentButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realm = Realm.getDefaultInstance();

        /*
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();*/

        patientsButton = findViewById(R.id.button_Main_Patients);
        medicsButton = findViewById(R.id.button_Main_Medics);
        appointmentButton = findViewById(R.id.button_Main_Appointments);

        patientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PatientsActivity.class);
                startActivity(intent);
            }
        });
        medicsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MedicsActivity.class);
                startActivity(intent);
            }
        });
        appointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUpToCreate("Nueva cita","Ingrese los datos de la cita");
            }
        });

        this.setTitle("Menu principal");

        testHTTP();

        //scheduleAlarm();
    }

    /*CRUD actions*/
    /*Create appointment*/
    private  void createAppointment(Medic medic, Patient patient, Date date){
        realm.beginTransaction();
        Appointment appointment = new Appointment(patient, medic, date);
        realm.copyToRealm(appointment);
        medic.getAppointments().add(appointment);
        patient.getAppointments().add(appointment);
        realm.commitTransaction();
    }

    /* Shows the dialog to create a new appointment*/
    private void showPopUpToCreate(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(title != null) builder.setTitle(title);
        if(message != null) builder.setMessage(message);

        View inflatedView = LayoutInflater.from(this).inflate(R.layout.layout_create_appointment, null);
        builder.setView(inflatedView);

        final EditText appointmentDateEditText = inflatedView.findViewById(R.id.editText_CreateAppointment_Date);

        //DB access to get the list of medics
        RealmResults<Medic> medics = realm.where(Medic.class).findAll();
        ArrayList<String> medicsNames = new ArrayList<>();
        for (Medic medic: medics)
            medicsNames.add(medic.getLastName() + " " + medic.getName());

        //DB access to get patients list
        RealmResults<Patient> patients = realm.where(Patient.class).findAll();
        ArrayList<String> patientsNames = new ArrayList<>();
        for (Patient patient: patients)
            patientsNames.add(patient.getLastName() + " " + patient.getName());

        //Medics spinner configuration
        final Spinner medicsSpinner = inflatedView.findViewById(R.id.spinner_CreateAppointment_Medic);
        ArrayAdapter<String> medicsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, medicsNames);
        medicsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medicsSpinner.setAdapter(medicsAdapter);

        //Medics spinner configuration
        final Spinner patientsSpinner = inflatedView.findViewById(R.id.spinner_CreateAppointment_Patient);
        ArrayAdapter<String> patientsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, patientsNames);
        patientsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        patientsSpinner.setAdapter(patientsAdapter);

        builder.setPositiveButton("Crear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Medic medic = medics.get(medicsSpinner.getSelectedItemPosition());
                Patient patient = patients.get(patientsSpinner.getSelectedItemPosition());
                Date appointmentDate = null;
                try {
                    appointmentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(appointmentDateEditText.getText().toString().trim());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (medic != null && patient != null && appointmentDate != null){
                    createAppointment(medic, patient, appointmentDate);
                } else {
                    Toast.makeText(getApplicationContext(), "Algun campo no fue llenado", Toast.LENGTH_SHORT).show();
                }
            }

        });

        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.show();

    }


    private void testHTTP(){
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls()
                .create();

        Medic m = realm.where(Medic.class).findFirst();

        String s = gson.toJson(m);

        Log.d("XXXXXXXXXXXXXXXXXXXXXXX", m.getName());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.12:3000/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        APIInterface service = retrofit.create(APIInterface.class);
        Call<Medic> call = service.postMedics(realm.where(Medic.class).findFirst());

        call.enqueue(new Callback<Medic>() {
            @Override
            public void onResponse(Call<Medic> call, Response<Medic> response) {
                Toast.makeText(getApplicationContext(), "Info recibida", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Medic> call, Throwable t) {
                Log.e("ERROR", "-------------------------------" + t.toString() + "------------------------------------------------");
            }
        });
    }

    /*Setup the alarm and the service to sync the data in the webservice*/
    public void scheduleAlarm() {
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long firstMillis = System.currentTimeMillis();
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstMillis, 60000, pIntent);
    }

}







