package com.example.app_2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.ref.Reference;
import java.util.Random;

public class ForgotPasswordAtivity extends AppCompatActivity {

    private TextView text_forgot;
    private EditText email_forgot;
    private Button  sendNewPassword_forgot;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        text_forgot = findViewById(R.id.text_forgot);
        email_forgot=findViewById(R.id.email_forgot);
        sendNewPassword_forgot=findViewById(R.id.sendNewPassword_forgot);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        sendNewPassword_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_forgot.getText().toString().trim();
                if(email.isEmpty()){
                    email_forgot.setError("E-Mail connot empty");
                }



                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ForgotPasswordAtivity.this, "New Password Sent", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ForgotPasswordAtivity.this, MainActivity.class) );
                        }else{
                            Toast.makeText(ForgotPasswordAtivity.this, "Mail Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }
        });


    }

}