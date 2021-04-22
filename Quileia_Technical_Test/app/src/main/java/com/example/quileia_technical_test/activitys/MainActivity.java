package com.example.quileia_technical_test.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.quileia_technical_test.services.MyAlarmReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private Realm realm;

    private Button patientsButton;
    private Button medicsButton;
    private Button appointmentButton;
    private AlarmManager alarmMgr;
    PendingIntent alarmIntent;


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
        scheduleAlarm(30);
    }

    /*Dialogs*/
    /*Shows the dialog to create a new appointment*/
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
    /*Shows the dialog to edit the sync interval*/
    private void showDialogEditInterval(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(title != null) builder.setTitle(title);
        if(message != null) builder.setMessage(message);

        View inflatedView = LayoutInflater.from(this).inflate(R.layout.layout_set_alarm_interval, null);
        builder.setView(inflatedView);

        final EditText timeDateEditText = inflatedView.findViewById(R.id.editText_EditInterval);

        builder.setPositiveButton("Establecer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scheduleAlarm(Integer.parseInt(timeDateEditText.getText().toString()));
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
    public void scheduleAlarm(int interval) {
        if (alarmMgr != null) alarmMgr.cancel(alarmIntent);
        alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyAlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime(), interval * 60000, alarmIntent);
    }

    /*Configures the options menu*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu_interval, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_MainMenu_Time:
                showDialogEditInterval("Editar intervalo", "Ingrese el nuevo intervalo en minutos");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}







