package com.example.quileia_technical_test.activitys;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quileia_technical_test.R;
import com.example.quileia_technical_test.models.Medic;
import com.example.quileia_technical_test.models.Patient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;

import io.realm.Realm;

public class PatientsActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients);

        realm = Realm.getDefaultInstance();
        floatingActionButton = findViewById(R.id.floatingactionbutton_Add);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUpToCreate("Crear nuevo paciente", "Ingrese los datos del nuevo paciente");
            }
        });
    }

    /*CRUD actions*/
    private void createNewPatient(String name, String lastNames, Date birthDate, String idNumber, Medic medic, boolean inTreatment, double moderatedFee, Date newAppointment) {
        realm.beginTransaction();
        Patient patient = new Patient(name, lastNames, birthDate, idNumber, medic, inTreatment, moderatedFee, newAppointment);
        realm.copyToRealm(patient);
        realm.commitTransaction();
    }

    /* Shows the dialog to create a new medic*/
    private void showPopUpToCreate(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(title != null) builder.setTitle(title);
        if(message != null) builder.setMessage(message);

        View inflatedView = LayoutInflater.from(this).inflate(R.layout.layout_create_medic, null);
        builder.setView(inflatedView);

        //Obtener los view del layout

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Obtener los valores de los view, comprovar que no sean vacios y pasarlos al metodo createNewPatient
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}