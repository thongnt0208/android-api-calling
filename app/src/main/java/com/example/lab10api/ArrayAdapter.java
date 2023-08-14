package com.example.lab10api;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab10api.api.TraineeRepository;
import com.example.lab10api.api.TraineeService;
import com.example.lab10api.model.Trainee;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArrayAdapter extends BaseAdapter {

    private ViewAll context;
    private int layout;
    private ArrayList<Trainee> traineesList;

    public ArrayAdapter(ViewAll context, int layout, ArrayList<Trainee> traineesList) {
        this.context = context;
        this.layout = layout;
        this.traineesList = traineesList;
    }

    @Override
    public int getCount() {
        return traineesList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        TextView tvName, tvEmail, tvPhone, tvGender;
        ImageView imgDelete, imgEdit;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);

            holder.tvName = convertView.findViewById(R.id.tvName);
            holder.tvEmail = convertView.findViewById(R.id.tvEmail);
            holder.tvPhone = convertView.findViewById(R.id.tvPhone);
            holder.tvGender = convertView.findViewById(R.id.tvGender);
            holder.imgEdit = convertView.findViewById(R.id.imageviewEdit);
            holder.imgDelete = convertView.findViewById(R.id.imageviewDelete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        Trainee trainee = traineesList.get(position);
        holder.tvName.setText(trainee.getName());
        holder.tvEmail.setText(trainee.getEmail());
        holder.tvPhone.setText(trainee.getPhone());
        holder.tvGender.setText(trainee.getGender());

        //Event handler for edit a CongViec
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Edit.class);
//                Log.d("myTag","onClick() returned: " + traineesList.get(position).getId());
                intent.putExtra("choosenTrainee", traineesList.get(position));
                context.startActivity(intent);
            }
        });

//        //Event handler for delete a CongViec
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context.dialogXoa(traineesList.get(position))) {
                    Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }
}
