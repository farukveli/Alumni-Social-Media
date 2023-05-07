package com.example.app_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewProfileActivity extends AppCompatActivity {


    private ImageView profilePhoto;

    private TextView name, surname, years, degree, job, email, phone;

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        String uid = getIntent().getStringExtra("transfer");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        profilePhoto = findViewById(R.id.profilePhoto_viewProfile);
        name = findViewById(R.id.name_viewProfile);
        surname = findViewById(R.id.surname_viewProfile);
        years = findViewById(R.id.years_viewProfile);
        degree = findViewById(R.id.degree_viewProfile);
        job = findViewById(R.id.job_viewProfile);
        email = findViewById(R.id.email_viewProfile);
        phone = findViewById(R.id.phone_viewProfile);

        profilePhoto.setEnabled(false);


        mDatabase.child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users profileUser = new Users();
                profileUser = snapshot.getValue(Users.class);
                Log.i("Records Fragment", profileUser.toString());
                name.setText(profileUser.getName());
                surname.setText(profileUser.getSurname());
                years.setText(profileUser.getEnteredYear()+" - "+profileUser.getGraduatedYear());
                if(profileUser.getDegree() == null){
                    degree.setHint("Degree is not Specified");
                }else{
                    degree.setText(profileUser.getDegree());
                }
                if(profileUser.getJob() == null){
                    job.setHint("Job is not Specified");
                }else{
                    job.setText(profileUser.getJob());
                }

                if(profileUser.getPhone() == null){
                    phone.setHint("Phone is not Specified");
                }else{
                    phone.setText(profileUser.getPhone());
                }

                email.setText(profileUser.getEmail());
                if(profileUser.getDataImage() != null){
                    Glide.with(ViewProfileActivity.this).load(profileUser.getDataImage()).into(profilePhoto);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}