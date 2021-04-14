package com.example.quileia_technical_test.activitys;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.quileia_technical_test.R;
import com.example.quileia_technical_test.adapters.MedicsAdapter;
import com.example.quileia_technical_test.models.Medic;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MedicsActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<Medic>>, AdapterView.OnItemClickListener {

    private Realm realm;

    private FloatingActionButton floatingActionButton;
    private ListView listView;
    private MedicsAdapter adapter;

    private RealmResults<Medic> medics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medics);

        realm = Realm.getDefaultInstance();
        medics = realm.where(Medic.class).findAll();
        medics.addChangeListener(this);

        adapter = new MedicsAdapter(this, medics, R.layout.layout_medic_list_item);
        listView = (ListView) findViewById(R.id.listView_Medics);
        listView.setAdapter(adapter);
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

        final EditText nameEditText = inflatedView.findViewById(R.id.editText_Name);
        final EditText lastNameEditText = inflatedView.findViewById(R.id.editText_lastName);
        final EditText proCardCodeEditText = inflatedView.findViewById(R.id.editText_cardCode);
        final EditText specialityEditText = inflatedView.findViewById(R.id.editText_speciality);
        final EditText expYearsEditText = inflatedView.findViewById(R.id.editText_expYears);
        final EditText officeEditText = inflatedView.findViewById(R.id.editText_office);
        final CheckBox domicileCheckBox = inflatedView.findViewById(R.id.checkbox_domicile);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = nameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                String proCardCode = proCardCodeEditText.getText().toString().trim();
                String speciality = specialityEditText.getText().toString().trim();
                float expYears = Float.parseFloat(expYearsEditText.getText().toString().trim());
                String office = officeEditText.getText().toString().trim();
                boolean domicile = domicileCheckBox.isChecked();

                if(name.length() >0 && lastName.length() >0 && proCardCode.length() >0 && speciality.length() >0 && expYears >0 && office.length() >0 && domicile)
                    createNewMedic(name, lastName, proCardCode, speciality, expYears, office, domicile);
                else
                    Toast.makeText(getApplicationContext(), "Algun campo no fue llenado", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.show();

    }

    @Override
    public void onChange(RealmResults<Medic> medics) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(MedicsActivity.this, MedicDetailsActivity.class);
        intent.putExtra("id", medics.get(position).getID());
        startActivity(intent);
    }
}

