package com.play.nettoyer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    Boolean firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        // sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        //firstTime = sharedPreferences.getBoolean("firstTime", firstTime);
        if (firebaseUser == null) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {

                    // SharedPreferences.Editor editor = sharedPreferences.edit();
                    //firstTime = false;
                    //editor.putBoolean("firstTime", firstTime);
                    //editor.apply();
                    Intent intent = new Intent(MainActivity.this, login.class);

                    startActivity(intent);
                    finish();
                }
            }, 2500);
        } else {
            Intent intent = new Intent(MainActivity.this, drawer.class);
            startActivity(intent);
            finish();
        }
    }
}

