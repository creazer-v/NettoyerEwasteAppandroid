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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {

    TextView textView;
    EditText txtmail, txtpass, txtconfirmpass,txtphn;
    Button signinbtn;
    ProgressBar loading;
    private FirebaseAuth firebaseAuth;
    Window window;
    ImageView imageView;
    FirebaseFirestore fStore;
    String userID;
    TextView textusername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.darkmode));
        }

        txtmail = findViewById(R.id.username);
        txtpass = findViewById(R.id.password);
        txtconfirmpass = findViewById(R.id.confirmpass);
        loading = findViewById(R.id.loading);
        signinbtn = findViewById(R.id.button2);
        imageView = findViewById(R.id.imageView3);
        textusername =findViewById(R.id.personname);
txtphn=findViewById(R.id.phoneNo);

        textView = findViewById(R.id.textView3);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(signup.this, login.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.goup, R.anim.godown);

                finish();

            }


        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent0 = new Intent(signup.this, login.class);
                startActivity(intent0);
                overridePendingTransition(R.anim.goup, R.anim.godown);
                finish();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = txtmail.getText().toString().trim();
                String passwords = txtpass.getText().toString().trim();
                String confirm = txtconfirmpass.getText().toString().trim();
                final String fullname = textusername.getText().toString();
                final String phonenow=txtphn.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {

                    Toast.makeText(signup.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(passwords)) {

                    Toast.makeText(signup.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(confirm)) {

                    Toast.makeText(signup.this, "Confirm Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (passwords.length() < 8) {
                    Toast.makeText(signup.this, "must be 8 characters", Toast.LENGTH_SHORT).show();
                }
                loading.setVisibility(View.VISIBLE);
                if (passwords.equals(confirm)) {

                    firebaseAuth.createUserWithEmailAndPassword(email, passwords)
                            .addOnCompleteListener(signup.this, new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    loading.setVisibility(View.GONE);

                                    if (task.isSuccessful()) {
                                        Toast.makeText(signup.this, "Signup success", Toast.LENGTH_SHORT).show();
                                        userID = firebaseAuth.getCurrentUser().getUid();
                                        DocumentReference documentReference = fStore.collection("users").document(userID);
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("userName", fullname);
                                        user.put("email", email);
                                        user.put("Phone",phonenow);
                                        documentReference.set(user);
                                        startActivity(new Intent(getApplicationContext(), drawer.class));
                                    }


                                }
                            });


                } else {
                    loading.setVisibility(View.VISIBLE);
                    Toast.makeText(signup.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);


                }
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
                        signup.this.finish();
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