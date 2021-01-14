package com.play.nettoyer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class phone extends AppCompatActivity {

    EditText txtphone;
    Button otpbtn;
    TextView textView;
    Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_activity);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.darkmode));
        }

        txtphone = findViewById(R.id.phoneenter);
        otpbtn = findViewById(R.id.otpbutton);
        textView = findViewById(R.id.textView3);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(phone.this, login.class);
                startActivity(intent1);
            }
        });


        otpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = txtphone.getText().toString().trim();

                if (number.isEmpty() || number.length() < 10) {
                    txtphone.setError("Not a valid Number");
                    txtphone.requestFocus();
                    return;
                }
                String phoneNumber = "+" + +91 + number;

                Intent intento = new Intent(phone.this, signotp.class);
                intento.putExtra("phonenumber", phoneNumber);
                startActivity(intento);

            }
        });
    }

}