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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

public class MedicDetailsActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<Appointment>> {

    private TextView nameTextView;
    private TextView specialityTextView;
    private TextView proCardCodeTextView;
    private TextView expYearsTextView;
    private TextView officeTextView;
    private TextView domicileTextView;
    private ListView listView;

    private AppointmentsAdapter appointmentAdapter;
    private RealmList<Appointment> appointments;
    private Realm realm;

    private int medicID;
    private Medic medic;
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medic_details);

        realm = Realm.getDefaultInstance();

        if (getIntent().getExtras() != null)
            medicID = getIntent().getExtras().getInt("ID");

        medic = realm.where(Medic.class).equalTo("ID", medicID).findFirst();
        appointments = medic.getAppointments();

        nameTextView = findViewById(R.id.textView_MedicDetails_Name);
        specialityTextView = findViewById(R.id.textView_MedicDetails_Speciality);
        proCardCodeTextView = findViewById(R.id.textView_MedicDetails_ProCardCode);
        expYearsTextView = findViewById(R.id.textView_MedicDetails_ExpYears);
        officeTextView = findViewById(R.id.textView_MedicDetails_Office);
        domicileTextView = findViewById(R.id.textView_MedicDetails_Domicile);

        //Appointment list configuration
        appointmentAdapter = new AppointmentsAdapter(this, appointments, R.layout.layout_appointment_list_item);
        listView = findViewById(R.id.listView_MedicDetails_Appointments);
        listView.setAdapter(appointmentAdapter);

        registerForContextMenu(listView);
        this.setTitle(medic.getFullName());
        setMedicInfo();

    }

    public void setMedicInfo(){
        nameTextView.setText(medic.getLastName() + " " + medic.getName());
        specialityTextView.setText("Especialidad: " + medic.getSpeciality());
        proCardCodeTextView.setText("Tarjeta profesional: " + medic.getProCardCode());
        expYearsTextView.setText("Años de experiencia: " + String.valueOf(medic.getExperienceYears()));
        officeTextView.setText("Oficina: " + medic.getOffice());
        domicileTextView.setText(medic.isDomicile() ? "Trabaja a domicilio" : "No trabaja a domicilio");
    }

    /*Dialogs*/
    /*Shows the dialog to edit the medic*/
    private void showDialogEditMedic(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(title != null) builder.setTitle(title);
        if(message != null) builder.setMessage(message);

        View inflatedView = LayoutInflater.from(this).inflate(R.layout.layout_create_medic, null);
        builder.setView(inflatedView);

        final EditText nameEditText = inflatedView.findViewById(R.id.editText_CreateMedic_Name);
        nameEditText.setText(medic.getName());
        final EditText lastNameEditText = inflatedView.findViewById(R.id.editText_CreateMedic_LastName);
        lastNameEditText.setText(medic.getLastName());
        final EditText proCardCodeEditText = inflatedView.findViewById(R.id.editText_CreateMedic_CardCode);
        proCardCodeEditText.setText(medic.getProCardCode());
        final EditText specialityEditText = inflatedView.findViewById(R.id.editText_CreateMedic_Speciality);
        specialityEditText.setText(medic.getSpeciality());
        final EditText expYearsEditText = inflatedView.findViewById(R.id.editText_CreateMedic_ExpYears);
        expYearsEditText.setText(String.valueOf(medic.getExperienceYears()));
        final EditText officeEditText = inflatedView.findViewById(R.id.editText_CreateMedic_Office);
        officeEditText.setText(medic.getOffice());
        final CheckBox domicileCheckBox = inflatedView.findViewById(R.id.checkBox_CreateMedic_Domicile);
        domicileCheckBox.setChecked(medic.isDomicile());

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = nameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                String proCardCode = proCardCodeEditText.getText().toString().trim();
                String speciality = specialityEditText.getText().toString().trim();
                float expYears = Float.parseFloat(expYearsEditText.getText().toString().trim());
                String office = officeEditText.getText().toString().trim();
                boolean domicile = domicileCheckBox.isChecked();

                if(name.length() > 0 && lastName.length() > 0 && proCardCode.length() > 0 && speciality.length() > 0 && office.length() > 0){
                    Medic.editMedic(realm, medic, name, lastName, proCardCode, speciality, expYears, office, domicile);
                    setMedicInfo();
                }
                else
                    Toast.makeText(getApplicationContext(), "Algun campo no fue llenado", Toast.LENGTH_SHORT).show();
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
        dateEditText.setText(dateFormat.format(appointment.getDate()));
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
                    Appointment.editAppointment(realm, appointment, patient, date);
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
                showDialogEditMedic("Editar medico", "Ingrese los nuevos datos");
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
                Appointment.deleteAppointment(realm, appointment);
                return true;
            case R.id.item_ContextMenu_Appointments_Edit:
                showDialogEditAppointment(appointment, "Editar cita","Ingrese los nuevos datos");
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /*Configures the onChange method for the ListView*/
    @Override
    public void onChange(RealmResults<Appointment> appointments) {
        appointmentAdapter.notifyDataSetChanged();
    }

}
