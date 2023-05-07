package com.example.app_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.HashSet;

public class SharePostActivity extends AppCompatActivity {

    private EditText title_sharePost,comment_sharePost,date_sharePost;

    private TextView postNumber, image_sharePost, name_sharePost;
    private Button share_sharePost;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_post);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference= database.getReference();
        firebaseUser=auth.getCurrentUser();

        title_sharePost= findViewById(R.id.title_sharePost);
        comment_sharePost = findViewById(R.id.comment_sharePost);
        date_sharePost= findViewById(R.id.date_sharePost);
        share_sharePost = findViewById(R.id.share_sharePost);
        postNumber = findViewById(R.id.postNumber);
        image_sharePost = findViewById(R.id.image_sharePost);
        name_sharePost = findViewById(R.id.name_sharePost);

        reference.child("users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users profileUser = new Users();
                profileUser = snapshot.getValue(Users.class);
                Log.i("Records Fragment", profileUser.toString());
                postNumber.setText(String.valueOf(profileUser.getNumberOfPost()+1));
                image_sharePost.setText(profileUser.getDataImage());
                name_sharePost.setText(profileUser.getName()+" "+profileUser.getSurname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        share_sharePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String title = title_sharePost.getText().toString().trim();
                String comment = comment_sharePost.getText().toString().trim();
                String date = date_sharePost.getText().toString().trim();
                String number = postNumber.getText().toString().trim();
                String image = image_sharePost.getText().toString().trim();
                String name = name_sharePost.getText().toString().trim();

                HashMap num = new HashMap();
                num.put("numberOfPost",Integer.parseInt(number));

                Posts post = new Posts();
                post.setUid(firebaseUser.getUid());
                post.setTitle(title);
                post.setComment(comment);
                post.setDate(date);
                post.setPostNumber(Integer.parseInt(number));
                post.setImage(image);
                post.setName(name);
                reference.child("users").child(firebaseUser.getUid()).updateChildren(num);
                reference.child("posts").child(firebaseUser.getUid()).child(""+number).setValue(post);
                startActivity(new Intent(SharePostActivity.this,HomePage.class));

            }
        });

    }
}