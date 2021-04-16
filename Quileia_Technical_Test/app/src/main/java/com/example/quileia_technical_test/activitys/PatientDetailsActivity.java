package com.example.quileia_technical_test.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quileia_technical_test.R;
import com.example.quileia_technical_test.adapters.AppointmentsAdapter;
import com.example.quileia_technical_test.models.Appointment;
import com.example.quileia_technical_test.models.Medic;
import com.example.quileia_technical_test.models.Patient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

public class PatientDetailsActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<Appointment>> {

    private TextView nameTextView;
    private TextView birthDateTextView;
    private TextView idNumberTextView;
    private TextView medicTextView;
    private TextView inTreatmentTextView;
    private TextView moderatedFeeTextView;
    private ListView listView;

    private AppointmentsAdapter appointmentAdapter;
    private RealmList<Appointment> appointments;
    private Realm realm;

    private int patientID;
    private Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);

        realm = Realm.getDefaultInstance();

        if (getIntent().getExtras() != null)
            patientID = getIntent().getExtras().getInt("ID");

        patient = realm.where(Patient.class).equalTo("ID", patientID).findFirst();
        appointments = patient.getAppointments();

        Log.d("Citas paciente", appointments.size() + "");

        nameTextView = findViewById(R.id.textView_PatientDetails_Name);
        birthDateTextView = findViewById(R.id.textView_PatientDetails_BirthDate);
        idNumberTextView = findViewById(R.id.textView_PatientDetails_IDNumber);
        medicTextView = findViewById(R.id.textView_PatientDetails_Medic);
        inTreatmentTextView = findViewById(R.id.textView_PatientDetails_InTreatment);
        moderatedFeeTextView = findViewById(R.id.textView_PatientDetails_ModeratedFee);

        //Appointment list configuration
        appointmentAdapter = new AppointmentsAdapter(this, appointments, R.layout.layout_appointment_list_item);
        listView = findViewById(R.id.listView_PatientDetails_Appointments);
        listView.setAdapter(appointmentAdapter);

        registerForContextMenu(listView);
        this.setTitle(patient.getLastName() + " " + patient.getName());
        setPatientInfo();

    }

    public void setPatientInfo(){
        nameTextView.setText(patient.getLastName() + " " + patient.getName());
        birthDateTextView.setText(patient.getBirthDate().toString());
        idNumberTextView.setText(patient.getIdNumber());
        medicTextView.setText(patient.getMedic().getLastName() + " " + patient.getMedic().getName());
        inTreatmentTextView.setText(patient.isInTreatment() ? "Esta en tratamiento" : "No esta en tratamiento");
        moderatedFeeTextView.setText(String.valueOf(patient.getModeratedFee()));
    }

    /*CRUD actions*/
    /*Edit patient*/
    private void editPatient(String name, String lastName, Date birthDate, String idNumber, Medic medic, boolean inTreatment, double moderatedFee){
        realm.beginTransaction();
        patient.setName(name);
        patient.setLastName(lastName);
        patient.setBirthDate(birthDate);
        patient.setIdNumber(idNumber);
        patient.setMedic(medic);
        patient.setInTreatment(inTreatment);
        patient.setModeratedFee(moderatedFee);
        realm.copyToRealmOrUpdate(patient);
        realm.commitTransaction();
    }

    /*Edit appointment*/
    private void editAppointment(Appointment appointment, Medic medic, Date date){
        realm.beginTransaction();
        appointment.setMedic(medic);
        appointment.setDate(date);
        realm.copyToRealmOrUpdate(appointment);
        realm.commitTransaction();
    }
    /*Delete appointment*/
    private void deleteAppointment(Appointment appointment){
        realm.beginTransaction();
        appointment.deleteFromRealm();
        realm.commitTransaction();
        Toast.makeText(getApplicationContext(), "Cita borrada", Toast.LENGTH_SHORT).show();
    }

    /*Dialogs*/
    /*Shows the dialog to create a new patient*/
    private void showDialogToEditPatient(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(title != null) builder.setTitle(title);
        if(message != null) builder.setMessage(message);

        View inflatedView = LayoutInflater.from(this).inflate(R.layout.layout_create_patient, null);
        builder.setView(inflatedView);

        final EditText nameEditText = inflatedView.findViewById(R.id.editText_CreatePatient_Name);
        nameEditText.setText(patient.getName());
        final EditText lastNameEditText = inflatedView.findViewById(R.id.editText_CreatePatient_lastName);
        lastNameEditText.setText(patient.getLastName());
        final EditText birthDateEditText = inflatedView.findViewById(R.id.editText_CreatePatient_BirthDate);
        birthDateEditText.setText(patient.getBirthDate().toString());
        final EditText iDNumberEditText = inflatedView.findViewById(R.id.editText_CreatePatient_IDNumber);
        iDNumberEditText.setText(patient.getIdNumber());
        final EditText moderatedFeeEditText = inflatedView.findViewById(R.id.editText_CreatePatient_ModeratedFeed);
        moderatedFeeEditText.setText(String.valueOf(patient.getModeratedFee()));
        final CheckBox inTreatmentCheckBox = inflatedView.findViewById(R.id.checkBox_CreatePatient_InTreatment);
        inTreatmentCheckBox.setChecked(patient.isInTreatment());

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

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
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
                    editPatient(name, lastName, birthDate, idNumber, medic, inTreatment, moderatedFee);
                    setPatientInfo();
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
    /*Shows the dialog to edit an appointment*/
    private void showDialogEditAppointment(Appointment appointment, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(title != null) builder.setTitle(title);
        if(message != null) builder.setMessage(message);

        View inflatedView = LayoutInflater.from(this).inflate(R.layout.layout_edit_appointment, null);
        builder.setView(inflatedView);

        final EditText dateEditText = inflatedView.findViewById(R.id.editText_EditAppointment);
        dateEditText.setText(appointment.getDate().toString());
        //DB access to get the list of medics
        RealmResults<Patient> patients = realm.where(Patient.class).findAll();
        ArrayList<String> patientsNames = new ArrayList<>();
        for (Patient patient: patients)
            patientsNames.add(patient.getLastName() + " " + patient.getName());

        //Spinner configuration
        final Spinner spinner = inflatedView.findViewById(R.id.spinner_EditAppointment);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, patientsNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Date date = null;
                try {
                    date = new SimpleDateFormat("dd/MM/yyyy").parse(dateEditText.getText().toString().trim());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (date != null){
                    Patient patient = patients.get(spinner.getSelectedItemPosition());
                    editAppointment(appointment, patient, date);
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

    /*Options menu configuration*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.item_OptionsMenu_Edit:
                showDialogToEditPatient("Editar paciente", "Ingrese los nuevos datos");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /*Context menu configuration*/
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(appointments.get(info.position).getMedic().getSpeciality());
        getMenuInflater().inflate(R.menu.context_menu_appointments, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Appointment appointment = appointments.get(info.position);

        switch (item.getItemId()){
            case R.id.item_ContextMenu_Appointments_Delete:
                deleteAppointment(appointment);
                return true;
            case R.id.item_ContextMenu_Appointments_Edit:
                showDialogEditAppointment(appointment, "Editar cita","Ingrese los nuevos datos");
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    /*Configuration for the listView listener*/
    @Override
    public void onChange(RealmResults<Appointment> appointments) {
        appointmentAdapter.notifyDataSetChanged();
    }

}
