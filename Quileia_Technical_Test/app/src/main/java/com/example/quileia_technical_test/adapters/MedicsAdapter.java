package com.example.quileia_technical_test.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.quileia_technical_test.R;
import com.example.quileia_technical_test.models.Medic;

import java.util.List;

public class MedicsAdapter extends BaseAdapter {

    private Context context;
    private List<Medic> list;
    private int layout;

    public MedicsAdapter(Context context, List<Medic> list, int layout) {
        this.context = context;
        this.list = list;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Medic getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(layout, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.textView_Name);
            viewHolder.speciality = (TextView) convertView.findViewById(R.id.textView_Speciality);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Medic medic = list.get(position);
        viewHolder.name.setText(medic.getLastName() + " " + medic.getName());
        viewHolder.speciality.setText(medic.getSpeciality());

        return convertView;
    }

    public class ViewHolder {

        TextView name;
        TextView speciality;

    }

}






















