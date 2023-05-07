package com.example.app_2;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText email_login, password_login;

    private Button login, signUp_login, forgot;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email_login=findViewById(R.id.email_login);
        password_login=findViewById(R.id.password_login);
        forgot=findViewById(R.id.forgot_main);
        login=findViewById(R.id.login);
        signUp_login=findViewById(R.id.signUp_login);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_login.getText().toString().trim();
                String password= password_login.getText().toString().trim();

                auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(MainActivity.this, HomePage.class));
                    }
                });
            }
        });

        signUp_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUp= new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(signUp);
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ForgotPasswordAtivity.class));
            }
        });

    }


}