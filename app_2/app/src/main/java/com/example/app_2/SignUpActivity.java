package com.example.app_2;

import static com.example.app_2.R.id.profilePhoto_signUp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.Serializable;

public class SignUpActivity extends AppCompatActivity {

    private EditText name_signUp, surname_signup, enteredYear_signUp, graduatedYear_signUp
            ,email_signUp,password_signUp;
    private Button create_signUp;
    private ImageView photo;

    private String imageUrl;

    private Uri uri;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;

    private FirebaseDatabase database;
    private DatabaseReference reference, postRef;

    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name_signUp=findViewById(R.id.name_signUp);
        surname_signup=findViewById(R.id.surname_signUp);
        enteredYear_signUp=findViewById(R.id.enteredYear_signUp);
        graduatedYear_signUp=findViewById(R.id.graduatedYear_signUp);
        email_signUp=findViewById(R.id.email_signUp);
        password_signUp=findViewById(R.id.password_signUp);
        create_signUp=findViewById(R.id.create_signUp);
        photo = findViewById(R.id.profilePhoto_signUp);
        auth = FirebaseAuth.getInstance();

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            photo.setImageURI(uri);
                        }else{
                            Toast.makeText(SignUpActivity.this, "No Photo Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });


        create_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                database=FirebaseDatabase.getInstance();
                reference=database.getReference("users");
                postRef=database.getReference("posts");
                storageReference= FirebaseStorage.getInstance().getReference().child("Profile Photos").child(uri.getLastPathSegment());

                String name=name_signUp.getText().toString().trim();
                String surname= surname_signup.getText().toString().trim();
                String enteredYear= enteredYear_signUp.getText().toString().trim();
                String graduatedYear = graduatedYear_signUp.getText().toString().trim();

                String user = email_signUp.getText().toString().trim();
                String pass= password_signUp.getText().toString().trim();

                Users userInf = new Users(name, surname, enteredYear, graduatedYear, user);


                if(user.isEmpty()){
                    name_signUp.setError("E-Mail cannot be empty");
                }

                if(pass.isEmpty()){
                    password_signUp.setError("Password cannot be empty");
                }

                else{
                    auth.createUserWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();


                                storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        firebaseUser = auth.getCurrentUser();
                                        String uid = firebaseUser.getUid();
                                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                        while (!uriTask.isComplete());
                                        Uri urlImage = uriTask.getResult();
                                        imageUrl = urlImage.toString();
                                        userInf.setDataImage(imageUrl);
                                        userInf.setNumberOfPost(0);
                                        reference.child(uid).setValue(userInf);
                                        startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                                    }
                                });

                            }else{
                                Toast.makeText(SignUpActivity.this, "Sign Up Failed !!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }
        });
    }

}