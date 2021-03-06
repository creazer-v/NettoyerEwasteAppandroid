package com.play.nettoyer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class signotp extends AppCompatActivity {

    private String verificationId;
    private FirebaseAuth mAuth;
    private EditText editText;
    Window window;

    ImageView imageview;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signotp_activity);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.darkmode));
        }
        imageview = findViewById(R.id.imageView3);


        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentw=new Intent(signotp.this,login.class);
                startActivity(intentw);
                overridePendingTransition(R.anim.goup,R.anim.godown);
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        editText = findViewById(R.id.enterotp);
        String phonenumber = getIntent().getStringExtra("phonenumber");
        sendVerificationCode(phonenumber);


        findViewById(R.id.loginbuttoncc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editText.getText().toString().trim();
                if (code.isEmpty()) {
                    editText.setError("Enter the code");
                    editText.requestFocus();
                    return;
                }

                verifyCode(code);
            }
        });


    }


    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);


        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(signotp.this, drawer.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(signotp.this, "Success", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void sendVerificationCode(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(signotp.this, "Failed", Toast.LENGTH_SHORT).show();
        }
    };


    // @Override
    //public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
    //  super.onCodeSent(s, forceResendingToken);
    //verificationId = s;
    //}

    // @Override
    //public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
    //  String code = phoneAuthCredential.getSmsCode();
    //if (code != null) {
    //  verifyCode(code);
    //}

    //@Override
    //public void onVerificationFailed(FirebaseException e); {
    // Toast.makeText(signinotp.this, "Sorry failed", Toast.LENGTH_SHORT).show();
    //}


}

