package com.example.quileia_technical_test.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
        listView.setOnItemClickListener(this);

        floatingActionButton = findViewById(R.id.floatingActionButton_Medics_Add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUpToCreate("Crear nuevo médico", "Ingrese los datos del nuevo médico");
            }
        });

        registerForContextMenu(listView);
        this.setTitle("Lista de medicos");
    }

    /*CRUD actions*/
    /*Create*/
    private void createNewMedic(String name, String lastName, String proCardCode, String speciality, float experienceYears, String office, boolean domicile) {
        realm.beginTransaction();
        Medic medic = new Medic(name, lastName, proCardCode, speciality, experienceYears, office, domicile);
        realm.copyToRealm(medic);
        realm.commitTransaction();
    }
    /*Delete*/
    private void deleteMedic(Medic medic){
        realm.beginTransaction();
        medic.deleteFromRealm();
        realm.commitTransaction();
    }

    /* Shows the dialog to create a new medic*/
    private void showPopUpToCreate(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(title != null) builder.setTitle(title);
        if(message != null) builder.setMessage(message);

        View inflatedView = LayoutInflater.from(this).inflate(R.layout.layout_create_medic, null);
        builder.setView(inflatedView);

        final EditText nameEditText = inflatedView.findViewById(R.id.editText_CreateMedic_Name);
        final EditText lastNameEditText = inflatedView.findViewById(R.id.editText_CreateMedic_LastName);
        final EditText proCardCodeEditText = inflatedView.findViewById(R.id.editText_CreateMedic_CardCode);
        final EditText specialityEditText = inflatedView.findViewById(R.id.editText_CreateMedic_Speciality);
        final EditText expYearsEditText = inflatedView.findViewById(R.id.editText_CreateMedic_ExpYears);
        final EditText officeEditText = inflatedView.findViewById(R.id.editText_CreateMedic_Office);
        final CheckBox domicileCheckBox = inflatedView.findViewById(R.id.checkBox_CreateMedic_Domicile);

        builder.setPositiveButton("Crear", new DialogInterface.OnClickListener() {
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(medics.get(info.position).getLastName() + " " + medics.get(info.position).getName());
        getMenuInflater().inflate(R.menu.context_menu_delete, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()){
            case R.id.item_ContextMenu_Delete:
                deleteMedic(medics.get(info.position));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onChange(RealmResults<Medic> medics) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(MedicsActivity.this, MedicDetailsActivity.class);
        intent.putExtra("ID", medics.get(position).getID());
        startActivity(intent);
    }
}
