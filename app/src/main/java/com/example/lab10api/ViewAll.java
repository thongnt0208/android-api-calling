package com.example.lab10api;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lab10api.api.TraineeRepository;
import com.example.lab10api.api.TraineeService;
import com.example.lab10api.model.Trainee;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewAll extends AppCompatActivity {

    static ListView listView;
    ArrayList<Trainee> arrayTrainees;
    static ArrayAdapter adapter;
    static TraineeService traineeService;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        traineeService = TraineeRepository.getTraineeService();
        listView = findViewById(R.id.listview);
        btnAdd = findViewById(R.id.btnAdd);
        arrayTrainees = new ArrayList<>();

        getAllTrainees();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewAll.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllTrainees();
    }

    void getAllTrainees() {
        Call<Trainee[]> call = traineeService.getAllTrainees();
        call.enqueue(new Callback<Trainee[]>() {
            @Override
            public void onResponse(Call<Trainee[]> call, Response<Trainee[]> response) {
                Trainee[] trainees = response.body();
                if (trainees == null) {
                    return;
                }
                // Clear the arrayTrainees list
                arrayTrainees.clear();

                for (Trainee trainee : trainees) {
                    arrayTrainees.add(trainee);
                }
                // Notify the adapter of the data change
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }

                adapter = new ArrayAdapter(ViewAll.this, R.layout.activity_listview_item, arrayTrainees);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Trainee[]> call, Throwable t) {
                Toast.makeText(ViewAll.this, "Get all fail", Toast.LENGTH_SHORT).show();

            }
        });
    }

    boolean dialogXoa(Trainee trainee) {
        final boolean[] result = {false};
        TraineeService traineeService = TraineeRepository.getTraineeService();

        AlertDialog.Builder dialogXoa = new AlertDialog.Builder(this);
        dialogXoa.setMessage("Bạn có muốn xoá công việc " + trainee.getName() + " không?");
        dialogXoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Call<Trainee> call = traineeService.deleteTrainees(trainee.getId());
                    call.enqueue(new Callback<Trainee>() {
                        @Override
                        public void onResponse(Call<Trainee> call, Response<Trainee> response) {
                            if (response.body() != null) {
                                getAllTrainees();
                                result[0] = true;
                            }
                        }

                        @Override
                        public void onFailure(Call<Trainee> call, Throwable t) {
                            throw new RuntimeException(t);
                        }
                    });
                } catch (Exception e) {
                    Log.d("loi", e.getMessage());
                }
            }
        });
        dialogXoa.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogXoa.show();
        return result[0];
    }


}