package com.play.nettoyer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.play.nettoyer.ui.loading;

public class login extends AppCompatActivity {

    Window window;

    TextView textview,txtview;
    EditText txtemail,txtpass;
    Button loginbtn,otp;
    private FirebaseAuth firebaseAuth;
    TextView textViewp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);






        if(android.os.Build.VERSION.SDK_INT>=21){
            window=this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.darkmode));
        }
        // actionBar=getSupportActionBar();
        //  actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        setContentView(R.layout.login_activity);
        txtemail= findViewById(R.id.username);
        txtpass= findViewById(R.id.password);
        loginbtn= findViewById(R.id.loginbutton);
        textViewp= findViewById(R.id.otp33);
        textview= findViewById(R.id.textView3);
        txtview=findViewById(R.id.textView5);
        final loading loading =new  loading(login.this);
        textview.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(login.this,signup.class);
                startActivity(intent);
                overridePendingTransition(R.anim.goup,R.anim.godown);
                login.super.finish();

            }

        });
        textViewp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(login.this,phone.class);
                startActivity(intent);
                login.super.finish();

            }

        });



        firebaseAuth = FirebaseAuth.getInstance();
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtemail.getText().toString().trim();
                String password = txtpass.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {

                    Toast.makeText(login.this, "Enter Email and Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {

                    Toast.makeText(login.this, "Enter Email and Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    startActivity(new Intent(getApplicationContext(),drawer.class));
                                    Toast.makeText(login.this, "Successfull", Toast.LENGTH_SHORT).show();

                                    finish();
                                } else {
                                    Toast.makeText(login.this, "Wrong Username or password", Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });
            }
        });


    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        login.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

}
