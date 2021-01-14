package com.play.nettoyer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class image extends AppCompatActivity {
ImageView backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

     //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        backbtn=findViewById(R.id.imageView1);
      backbtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent =  new Intent(image.this,drawer.class);
            startActivity(intent);
            finish();
          }
      });
    }

}