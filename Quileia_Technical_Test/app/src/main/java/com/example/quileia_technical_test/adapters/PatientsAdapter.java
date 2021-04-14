package com.example.quileia_technical_test.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.quileia_technical_test.R;
import com.example.quileia_technical_test.models.Patient;

import java.util.List;

public class PatientsAdapter extends BaseAdapter {

    private Context context;
    private List<Patient> list;
    private int layout;

    public PatientsAdapter(Context context, List<Patient> list, int layout) {
        this.context = context;
        this.list = list;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PatientItemViewHolder viewHolder;

        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(layout, null);
            viewHolder = new PatientItemViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.textView_PatientItem_Name);
            viewHolder.idNumber = (TextView) convertView.findViewById(R.id.textView_PatientItem_IDNumber);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PatientItemViewHolder) convertView.getTag();
        }

        Patient patient = list.get(position);
        viewHolder.name.setText(patient.getLastName() + " " + patient.getLastName());
        viewHolder.idNumber.setText(patient.getIdNumber());

        return convertView;
    }

    public class PatientItemViewHolder {

        TextView name;
        TextView idNumber;

    }

}
