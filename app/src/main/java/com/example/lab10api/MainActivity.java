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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TraineeService traineeService;
    EditText etName, eteMail, etPhone, etGender;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        eteMail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etGender = findViewById(R.id.etGender);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        traineeService = TraineeRepository.getTraineeService();
        //then
    }

    private void save() {
        String name = etName.getText().toString();
        String email = eteMail.getText().toString();
        String phone = etPhone.getText().toString();
        String gender = etGender.getText().toString();

        //Tao du lieu dang Trainee (object Trainee)
        Trainee trainee = new Trainee(name, email, phone, gender);
        try {
            Call<Trainee> call = traineeService.createTrainees(trainee);
            call.enqueue(new Callback<Trainee>() {
                @Override
                public void onResponse(Call<Trainee> call, Response<Trainee> response) {
                    if (response.body() != null) {
                        Toast.makeText(MainActivity.this, "Save successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, ViewAll.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Trainee> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Save fail", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.d("loi", e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        save();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}