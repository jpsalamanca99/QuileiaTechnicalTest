package com.example.quileia_technical_test.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.quileia_technical_test.R;
import com.example.quileia_technical_test.adapters.PatientsAdapter;
import com.example.quileia_technical_test.models.Appointment;
import com.example.quileia_technical_test.models.Medic;
import com.example.quileia_technical_test.models.Patient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

public class PatientsActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<Patient>>, AdapterView.OnItemClickListener {

    private Realm realm;

    private FloatingActionButton floatingActionButton;
    private ListView listView;
    private PatientsAdapter adapter;

    private RealmResults<Patient> patients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients);

        realm = Realm.getDefaultInstance();
        patients = realm.where(Patient.class).findAll();
        patients.addChangeListener(this);

        adapter = new PatientsAdapter(this, patients, R.layout.layout_patient_list_item);
        listView = (ListView) findViewById(R.id.listView_Patients);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        floatingActionButton = findViewById(R.id.floatingActionButton_Patients_Add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUpToCreate("Crear nuevo paciente", "Ingrese los datos del nuevo paciente");
            }
        });

        registerForContextMenu(listView);
        this.setTitle("Lista de pacientes");
    }

    /* Shows the dialog to create a new patient*/
    private void showPopUpToCreate(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(title != null) builder.setTitle(title);
        if(message != null) builder.setMessage(message);

        View inflatedView = LayoutInflater.from(this).inflate(R.layout.layout_create_patient, null);
        builder.setView(inflatedView);

        final EditText nameEditText = inflatedView.findViewById(R.id.editText_CreatePatient_Name);
        final EditText lastNameEditText = inflatedView.findViewById(R.id.editText_CreatePatient_lastName);
        final EditText birthDateEditText = inflatedView.findViewById(R.id.editText_CreatePatient_BirthDate);
        final EditText iDNumberEditText = inflatedView.findViewById(R.id.editText_CreatePatient_IDNumber);
        final EditText moderatedFeeEditText = inflatedView.findViewById(R.id.editText_CreatePatient_ModeratedFeed);
        final CheckBox inTreatmentCheckBox = inflatedView.findViewById(R.id.checkBox_CreatePatient_InTreatment);


        //DB access to get the list of medics
        RealmResults<Medic> medics = realm.where(Medic.class).findAll();
        ArrayList<String> medicsNames = new ArrayList<>();
        for (Medic medic: medics)
            medicsNames.add(medic.getLastName() + " " + medic.getName());

        //Spinner configuration
        final Spinner spinner = inflatedView.findViewById(R.id.spinner_CreatePatient_Medic);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, medicsNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        builder.setPositiveButton("Crear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = nameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                Date birthDate = null;
                try {
                    birthDate = new SimpleDateFormat("dd/MM/yyyy").parse(birthDateEditText.getText().toString().trim());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String idNumber = iDNumberEditText.getText().toString().trim();
                double moderatedFee = moderatedFeeEditText.getText().toString().trim().length() > 0 ? Float.parseFloat(moderatedFeeEditText.getText().toString().trim()) : 0;
                boolean inTreatment = inTreatmentCheckBox.isChecked();

                if (name.length() > 0 && lastName.length() > 0 && idNumber.length() > 0 && moderatedFee > 0 && birthDate != null){
                    Medic medic = medics.get(spinner.getSelectedItemPosition());
                    Patient.createPatient(realm, name, lastName, birthDate, idNumber, medic, inTreatment, moderatedFee);
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
    /*Configures the context menu*/
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(patients.get(info.position).getLastName() + " " + patients.get(info.position).getName());
        getMenuInflater().inflate(R.menu.context_menu_delete, menu);
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()){
            case R.id.item_ContextMenu_Delete:
                Patient.deletePatient(realm, patients.get(info.position));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    /*Configures the onChange of the ListView*/
    @Override
    public void onChange(RealmResults<Patient> patients) {
        adapter.notifyDataSetChanged();
    }
    /*Configures the onItemClick of the ListView*/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(PatientsActivity.this, PatientDetailsActivity.class);
        intent.putExtra("ID", patients.get(position).getID());
        startActivity(intent);
    }

}
