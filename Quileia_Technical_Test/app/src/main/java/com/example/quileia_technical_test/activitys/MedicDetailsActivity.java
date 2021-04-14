package com.example.quileia_technical_test.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.quileia_technical_test.R;
import com.example.quileia_technical_test.adapters.AppointmentAdapter;
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

    private AppointmentAdapter appointmentAdapter;
    private RealmList<Appointment> appointments;
    private Realm realm;

    private int medicID;
    private Medic medic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medic_details);

        nameTextView = findViewById(R.id.textView_Name);
        specialityTextView = findViewById(R.id.textView_Speciality);
        proCardCodeTextView = findViewById(R.id.textView_ProCardCode);
        expYearsTextView = findViewById(R.id.textView_ExpYears);
        officeTextView = findViewById(R.id.textView_Office);
        domicileTextView = findViewById(R.id.textView_Domicile);
        listView = findViewById(R.id.listView_Appointments);

        realm = Realm.getDefaultInstance();

        if (getIntent().getExtras() != null)
            medicID = getIntent().getExtras().getInt("ID");

        medic = realm.where(Medic.class).equalTo("ID", medicID).findFirst();
        appointments = medic.getAppointments();

        this.setTitle(medic.getLastName() + " " + medic.getName());
        setMedicInfo();

    }

    public void setMedicInfo(){
        nameTextView.setText(medic.getLastName() + " " + medic.getName());
        specialityTextView.setText(medic.getSpeciality());
        proCardCodeTextView.setText(medic.getProCardCode());
        expYearsTextView.setText(String.valueOf(medic.getExperienceYears()));
        officeTextView.setText(medic.getOffice());
        domicileTextView.setText(String.valueOf(medic.isDomicile()));
    }
}

























