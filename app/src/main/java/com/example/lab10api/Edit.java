package com.example.lab10api;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lab10api.api.TraineeRepository;
import com.example.lab10api.api.TraineeService;
import com.example.lab10api.model.Trainee;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Edit extends AppCompatActivity implements View.OnClickListener {

    TraineeService traineeService;
    EditText etName, eteMail, etPhone, etGender;
    Button btnSave, btnCancel;
    Trainee choosenTrainee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        choosenTrainee = (Trainee) intent.getSerializableExtra("choosenTrainee");

        etName = findViewById(R.id.etName);
        eteMail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etGender = findViewById(R.id.etGender);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        etName.setText(choosenTrainee.getName());
        eteMail.setText(choosenTrainee.getEmail());
        etPhone.setText(choosenTrainee.getPhone());
        etGender.setText(choosenTrainee.getGender());

        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Edit.this, ViewAll.class);
                startActivity(intent);
            }
        });

        traineeService = TraineeRepository.getTraineeService();
    }

    private void save() {
        String name = etName.getText().toString();
        String email = eteMail.getText().toString();
        String phone = etPhone.getText().toString();
        String gender = etGender.getText().toString();

        //Cap nhat du lieu dang Trainee (object Trainee)
        Trainee trainee = new Trainee(name, email, phone, gender);
        try {
            Call<Trainee> call = traineeService.updateTrainees(choosenTrainee.getId(), trainee);
            call.enqueue(new Callback<Trainee>() {
                @Override
                public void onResponse(Call<Trainee> call, Response<Trainee> response) {
                    if (response.body() != null) {
                        Toast.makeText(Edit.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Edit.this, ViewAll.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<Trainee> call, Throwable t) {
                    Toast.makeText(Edit.this, "Save fail", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.d("loi", e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }
}