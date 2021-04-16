package com.example.quileia_technical_test.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quileia_technical_test.R;
import com.example.quileia_technical_test.adapters.AppointmentsAdapter;
import com.example.quileia_technical_test.models.Appointment;
import com.example.quileia_technical_test.models.Medic;

import io.realm.Realm;
import io.realm.RealmList;

public class MedicDetailsActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medic_details);

        realm = Realm.getDefaultInstance();

        if (getIntent().getExtras() != null)
            medicID = getIntent().getExtras().getInt("ID");

        medic = realm.where(Medic.class).equalTo("ID", medicID).findFirst();
        appointments = medic.getAppointments();

        Log.d("Numero de citas", appointments.size() + "");

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

        this.setTitle(medic.getLastName() + " " + medic.getName());
        setMedicInfo();

    }

    public void setMedicInfo(){
        nameTextView.setText(medic.getLastName() + " " + medic.getName());
        specialityTextView.setText(medic.getSpeciality());
        proCardCodeTextView.setText(medic.getProCardCode());
        expYearsTextView.setText(String.valueOf(medic.getExperienceYears()));
        officeTextView.setText(medic.getOffice());
        domicileTextView.setText(medic.isDomicile() ? "Trabaja a domicilio" : "No trabaja a domicilio");
    }

    /*CRUD actions*/
    /*Edit*/
    private void editMedic(String name, String lastName, String proCardCode, String speciality, float experienceYears, String office, boolean domicile){
        realm.beginTransaction();
        medic.setName(name);
        medic.setLastName(lastName);
        medic.setProCardCode(proCardCode);
        medic.setSpeciality(speciality);
        medic.setExperienceYears(experienceYears);
        medic.setOffice(office);
        medic.setDomicile(domicile);
        realm.copyToRealmOrUpdate(medic);
        realm.commitTransaction();
    }

    /*Edit dialog*/
    private void showPopUpToEditMedic(String title, String message){
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

                if(name.length() > 0 && lastName.length() > 0 && proCardCode.length() > 0 && speciality.length() > 0 && office.length() > 0)
                    editMedic(name, lastName, proCardCode, speciality, expYears, office, domicile);
                else
                    Toast.makeText(getApplicationContext(), "Algun campo no fue llenado", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.item_OptionsMenu_Edit:
                showPopUpToEditMedic("Editar medico", "Ingrese los nuevos datos");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
