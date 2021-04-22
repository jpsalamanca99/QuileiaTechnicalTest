package com.example.quileia_technical_test.activitys;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
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
import com.example.quileia_technical_test.serializers.MedicSerializer;
import com.example.quileia_technical_test.services.APIInterface;
import com.example.quileia_technical_test.services.MyAlarmReceiver;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
        scheduleAlarm();
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
                    Appointment.createAppointment(realm, medic, patient, appointmentDate);
                } else {
                    Toast.makeText(getApplicationContext(), "Algun campo no fue llenado", Toast.LENGTH_SHORT).show();
                }
            }

        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.show();

    }

    /*Setup the alarm and the service to sync the data in the webservice*/
    public void scheduleAlarm() {
        AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyAlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 60 * 1000, 60000, alarmIntent);
    }

}







