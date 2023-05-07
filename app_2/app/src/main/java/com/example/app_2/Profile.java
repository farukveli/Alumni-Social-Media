package com.example.app_2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class Profile extends AppCompatActivity {

    private ImageView profilePhoto;


    private EditText name, surname, years, degree, job, email, phone;
    private Button edit , save;
    private DatabaseReference mDatabase;
    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;

    private Uri uri;

    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();


        profilePhoto = findViewById(R.id.profilePhoto_Profile);
        name = findViewById(R.id.name_Profile);
        surname = findViewById(R.id.surname_Profile);
        years = findViewById(R.id.years_Profile);
        degree = findViewById(R.id.degree_Profile);
        job = findViewById(R.id.job_Profile);
        email = findViewById(R.id.email_Profile);
        phone = findViewById(R.id.phone_Profile);
        edit = findViewById(R.id.edit_profile);
        save = findViewById(R.id.save_profile);

        profilePhoto.setEnabled(false);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            profilePhoto.setImageURI(uri);
                        }else{
                            Toast.makeText(Profile.this, "No Photo Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });



        mDatabase.child("users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
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
                    Glide.with(Profile.this).load(profileUser.getDataImage().toString()).into(profilePhoto);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setEnabled(true);
                name.setBackgroundResource(android.R.drawable.edit_text);
                surname.setEnabled(true);
                surname.setBackgroundResource(android.R.drawable.edit_text);
                years.setEnabled(true);
                years.setBackgroundResource(android.R.drawable.edit_text);
                degree.setEnabled(true);
                degree.setBackgroundResource(android.R.drawable.edit_text);
                job.setEnabled(true);
                job.setBackgroundResource(android.R.drawable.edit_text);
                email.setEnabled(true);
                email.setBackgroundResource(android.R.drawable.edit_text);
                phone.setEnabled(true);
                phone.setBackgroundResource(android.R.drawable.edit_text);

                edit.setVisibility(View.INVISIBLE);
                edit.setClickable(false);
                save.setVisibility(View.VISIBLE);
                save.setClickable(true);

                profilePhoto.setEnabled(true);


            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(uri != null){
                    storageReference= FirebaseStorage.getInstance().getReference().child("Profile Photos").child(uri.getLastPathSegment());
                    storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            firebaseUser= auth.getCurrentUser();
                            String uid = firebaseUser.getUid();
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isComplete());
                            Uri urlImage = uriTask.getResult();
                            String imageUrl = urlImage.toString();
                            mDatabase.child("users").child(uid).child("dataImage").setValue(imageUrl);
                        }
                    });
                }

                String[] year = years.getText().toString().split(" ",3);

                HashMap data = new HashMap();
                data.put("name",name.getText().toString().trim());
                data.put("surname",surname.getText().toString().trim());
                data.put("enteredYear",year[0]);
                data.put("graduatedYear",year[2]);
                data.put("degree", degree.getText().toString().trim());
                data.put("job", job.getText().toString().trim());
                data.put("email",email.getText().toString().trim());
                data.put("phone", phone.getText().toString().trim());


                mDatabase.child("users").child(firebaseUser.getUid()).updateChildren(data).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Profile.this, "Edit is successful", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Profile.this, "Edit is no successful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                firebaseUser.updateEmail(email.getText().toString().trim());

                name.setEnabled(false);
                name.setBackgroundResource(android.R.color.transparent);
                surname.setEnabled(false);
                surname.setBackgroundResource(android.R.color.transparent);
                years.setEnabled(false);
                years.setBackgroundResource(android.R.color.transparent);
                degree.setEnabled(false);
                degree.setBackgroundResource(android.R.color.transparent);
                job.setEnabled(false);
                job.setBackgroundResource(android.R.color.transparent);
                email.setEnabled(false);
                email.setBackgroundResource(android.R.color.transparent);
                phone.setEnabled(false);
                phone.setBackgroundResource(android.R.color.transparent);
                profilePhoto.setEnabled(false);
                edit.setVisibility(View.VISIBLE);
                edit.setClickable(true);
                save.setVisibility(View.INVISIBLE);
                save.setClickable(false);


            }
        });

    }
}