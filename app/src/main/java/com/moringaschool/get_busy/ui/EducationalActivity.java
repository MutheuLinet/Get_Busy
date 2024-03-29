package com.moringaschool.get_busy.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.moringaschool.get_busy.Adapter.RecyclerViewAdapter;
import com.moringaschool.get_busy.R;
import com.moringaschool.get_busy.databinding.ActivityEducationalBinding;
import com.moringaschool.get_busy.models.ResultOpenDb;
import com.moringaschool.get_busy.models.Result__1;
import com.moringaschool.get_busy.network.EducationalApi;
import com.moringaschool.get_busy.network.EducationalApiClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EducationalActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerViewAdapter adapter;
    ActivityEducationalBinding binding;
    int userScore;
    Call<ResultOpenDb> call1;
    public List<Result__1> allItemsList;
    Result__1 question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEducationalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        allItemsList=new ArrayList<>();

        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        question = (Result__1) intent.getSerializableExtra("result");
        int num =  10 + position;

        binding.submit.setOnClickListener(this);


        EducationalApi client = EducationalApiClient.getClient();
        //https://opentdb.com/api.php?amount=50&category=18&difficulty=medium&type=multiple
         call1 = client.getAllItems(5,num,"medium","multiple"); //amount=1&category=27&difficulty=easy&type=multiple

       getResponse(call1);
    }

    private void getResponse(Call<ResultOpenDb> call) {
        call.clone().enqueue(new Callback<ResultOpenDb>() {
            @Override
            public void onResponse(Call<ResultOpenDb> call, Response<ResultOpenDb> response) {
                if (response.isSuccessful()) {
                    allItemsList = response.body().getResults();
                    Log.d("TAG", "onResponse: " + allItemsList);
                    adapter = new RecyclerViewAdapter(EducationalActivity.this, allItemsList);
                    binding.lvp.setAdapter(adapter);
                    binding.lvp.setLayoutManager(new LinearLayoutManager(EducationalActivity.this));
                    binding.lvp.setHasFixedSize(true);

                    adapter = new RecyclerViewAdapter(EducationalActivity.this, allItemsList);
                    binding.lvp.setAdapter(adapter);
                    binding.lvp.setLayoutManager(new LinearLayoutManager(EducationalActivity.this));
                    binding.lvp.setHasFixedSize(true);
                    successful();
                }else{

                    unSuccessful();
                }
            }
            @Override
            public void onFailure(Call<ResultOpenDb> call, Throwable t) {
                Log.e("Error Message", "onFailure: ",t );
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == binding.submit){
            userScore = RecyclerViewAdapter.getScore();
            Intent intent = new Intent(EducationalActivity.this, AnsActivity.class);
            intent.putExtra("userScore", userScore);
            startActivity(intent);
//            Toast.makeText(this, "Your Score is " + userScore, Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "Keep calm", Toast.LENGTH_SHORT).show();
        }
    }

    public void successful(){
        binding.bored.setVisibility(View.VISIBLE);
        binding.welcome.setVisibility(View.VISIBLE);
        binding.lvp.setVisibility(View.VISIBLE);
        binding.submit.setVisibility(View.VISIBLE);
        binding.progress.setVisibility(View.GONE);
    }
    public void unSuccessful(){
        binding.bored.setVisibility(View.GONE);
        binding.welcome.setVisibility(View.VISIBLE);
        binding.welcome.setText("Please try Again Later");
        binding.lvp.setVisibility(View.GONE);
        binding.submit.setVisibility(View.GONE);
        binding.progress.setVisibility(View.GONE);


    }

}