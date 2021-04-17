package com.example.quileia_technical_test.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.quileia_technical_test.R;
import com.example.quileia_technical_test.models.Appointment;
import com.example.quileia_technical_test.models.Medic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class AppointmentsAdapter extends BaseAdapter {

    private Context context;
    private List<Appointment> list;
    private int layout;
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public AppointmentsAdapter(Context context, List<Appointment> list, int layout) {
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
        AppointmentItemViewHolder viewHolder;

        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(layout, null);
            viewHolder = new AppointmentItemViewHolder();
            viewHolder.patientName = (TextView) convertView.findViewById(R.id.textView_AppointmentsItem_PatientName);
            viewHolder.medicName = (TextView) convertView.findViewById(R.id.textView_AppointmentsItem_MedicName);
            viewHolder.speciality = (TextView) convertView.findViewById(R.id.textView_AppointmentsItem_Speciality);
            viewHolder.date = (TextView) convertView.findViewById(R.id.textView_AppointmentsItem_Date);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (AppointmentItemViewHolder) convertView.getTag();
        }

        Appointment appointment = list.get(position);
        viewHolder.patientName.setText(appointment.getPatient().getLastName() + " " + appointment.getPatient().getName());
        viewHolder.medicName.setText(appointment.getMedic().getLastName() + " " + appointment.getMedic().getName());
        viewHolder.speciality.setText(appointment.getMedic().getSpeciality());
        viewHolder.date.setText(dateFormat.format(appointment.getDate()));


        return convertView;
    }

    public class AppointmentItemViewHolder {

        TextView patientName;
        TextView medicName;
        TextView speciality;
        TextView date;

    }
}



















