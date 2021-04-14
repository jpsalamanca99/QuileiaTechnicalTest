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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.realm.Realm;

public class MedicsActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medics);

        realm = Realm.getDefaultInstance();
        floatingActionButton = findViewById(R.id.floatingactionbutton_Add);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUpToCreate("Crear nuevo médico", "Ingrese los datos del nuevo médico");
            }
        });
    }

    /*CRUD actions*/
    private void createNewMedic(String name, String lastName, String proCardCode, String speciality, float experienceYears, String office, boolean domicile) {
        realm.beginTransaction();
        Medic medic = new Medic(name, lastName, proCardCode, speciality, experienceYears, office, domicile);
        realm.copyToRealm(medic);
        realm.commitTransaction();
    }

    /* Shows the dialog to create a new medic*/
    private void showPopUpToCreate(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(title != null) builder.setTitle(title);
        if(message != null) builder.setMessage(message);

        View inflatedView = LayoutInflater.from(this).inflate(R.layout.layout_create_medic, null);
        builder.setView(inflatedView);

        final EditText nameET = inflatedView.findViewById(R.id.editText_Name);
        final EditText lastNameET = inflatedView.findViewById(R.id.editText_lastName);
        final EditText proCardCodeET = inflatedView.findViewById(R.id.editText_cardCode);
        final EditText specialityET = inflatedView.findViewById(R.id.editText_speciality);
        final EditText expYearsET = inflatedView.findViewById(R.id.editText_expYears);
        final EditText officeET = inflatedView.findViewById(R.id.editText_office);
        final CheckBox domicileCB = inflatedView.findViewById(R.id.checkbox_domicile);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = nameET.getText().toString().trim();
                String lastName = lastNameET.getText().toString().trim();
                String proCardCode = proCardCodeET.getText().toString().trim();
                String speciality = specialityET.getText().toString().trim();
                float expYears = Float.parseFloat(expYearsET.getText().toString().trim());
                String office = officeET.getText().toString().trim();
                boolean domicile = domicileCB.isChecked();

                if(name.length() >0 && lastName.length() >0 && proCardCode.length() >0 && speciality.length() >0 && expYears >0 && office.length() >0 && domicile)
                    createNewMedic(name, lastName, proCardCode, speciality, expYears, office, domicile);
                else
                    Toast.makeText(getApplicationContext(), "Algun campo no fue llenado", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

}

