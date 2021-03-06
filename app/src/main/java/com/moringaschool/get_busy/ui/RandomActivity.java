package com.moringaschool.get_busy.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moringaschool.get_busy.R;
import com.moringaschool.get_busy.models.Result;
import com.moringaschool.get_busy.network.BoredApi;
import com.moringaschool.get_busy.network.BoredApiClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RandomActivity extends AppCompatActivity {

    @BindView(R.id.textViewTextMultiLine)
    TextView mTextViewTextMultiLin;
    @BindView(R.id.options_display)
    TextView mOptions_display;
    @BindView(R.id.like)
    ExtendedFloatingActionButton mLike;
    @BindView(R.id.reloadRandom)
    FloatingActionButton mReloadRandom;

    BoredApi api;

    Result result;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_view);
        ButterKnife.bind(this);

        Window window = RandomActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(RandomActivity.this, R.color.magenta));

        FirebaseApp.initializeApp(this);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getInstance().getReference("BoredActivities");
        DatabaseReference userRef = firebaseDatabase.getReference("BoredActivities");

        api = BoredApiClient.getClient();
        Call<Result> call = api.getRandomItem();
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.isSuccessful()){
                    result = response.body();
                    String activity = result.getActivity().toString();
                    mTextViewTextMultiLin.setText(activity);
                    String type = result.getType().toString();
                    mOptions_display.setText( "Type: " + type + " activity!");
                }
            }
            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.e("Error Message", "onFailure: ",t );
            }
        });

        mReloadRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                api = BoredApiClient.getClient();
                Call<Result> call = api.getRandomItem();
                call.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        if (response.isSuccessful()){
                            result = response.body();
                            String activity = result.getActivity().toString();
                            mTextViewTextMultiLin.setText(activity);
                            String type = result.getType().toString();
                            mOptions_display.setText( "Type: " + type + " activity!");
                        }
                    }
                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        Log.e("Error Message", "onFailure: ",t );
                    }
                });
            }
        });

        mLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.child(result.getKey()).setValue(result);

                //add item to firebase db

            }
        });
    }

    }
