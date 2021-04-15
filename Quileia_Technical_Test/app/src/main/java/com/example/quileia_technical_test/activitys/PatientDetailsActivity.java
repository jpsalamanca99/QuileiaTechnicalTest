package com.example.quileia_technical_test.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.quileia_technical_test.R;
import com.example.quileia_technical_test.adapters.AppointmentsAdapter;
import com.example.quileia_technical_test.models.Appointment;
import com.example.quileia_technical_test.models.Patient;

import io.realm.Realm;
import io.realm.RealmList;

public class PatientDetailsActivity extends AppCompatActivity {

    private TextView nameTextView;
    private TextView birthDateTextView;
    private TextView idNumberTextView;
    private TextView medicTextView;
    private TextView inTreatmentTextView;
    private TextView moderatedFeeTextView;

    private AppointmentsAdapter appointmentsAdapter;
    private RealmList<Appointment> appointments;
    private Realm realm;

    private int patientID;
    private Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);

        nameTextView = findViewById(R.id.textView_PatientDetails_Name);
        birthDateTextView = findViewById(R.id.textView_PatientDetails_BirthDate);
        idNumberTextView = findViewById(R.id.textView_PatientDetails_IDNumber);
        medicTextView = findViewById(R.id.textView_PatientDetails_Medic);
        inTreatmentTextView = findViewById(R.id.textView_PatientDetails_InTreatment);
        moderatedFeeTextView = findViewById(R.id.textView_PatientDetails_ModeratedFee);

        realm = Realm.getDefaultInstance();

        if (getIntent().getExtras() != null)
            patientID = getIntent().getExtras().getInt("ID");

        patient = realm.where(Patient.class).equalTo("ID", patientID).findFirst();
        appointments = patient.getAppointments();
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

}
