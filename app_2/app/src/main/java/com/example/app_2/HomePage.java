package com.example.app_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class HomePage extends AppCompatActivity implements RecyclerViewInterface {

    private Button profile , search, share;

    private RecyclerView recyclerView;

    private RCAdapter2 rcAdapter;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ArrayList<RCModel2> arrayList;
    private HashMap posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        mDatabase= FirebaseDatabase.getInstance().getReference();


        profile= findViewById(R.id.profile_home);
        search= findViewById(R.id.search_home);
        share = findViewById(R.id.share_home);

        recyclerView = findViewById(R.id.recyclerView_search);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        arrayList=new ArrayList<>();
        rcAdapter=new RCAdapter2(this,arrayList, this);
        recyclerView.setAdapter(rcAdapter);

        posts = new HashMap();

        mDatabase.child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String uid = new String();
                for(DataSnapshot s : snapshot.getChildren()){
                    for(DataSnapshot x : s.getChildren()){
                        Posts post = x.getValue(Posts.class);
                        RCModel2 model2 = new RCModel2(post.getTitle(),post.getComment(),post.getDate());
                        model2.setImage(post.getImage());
                        model2.setName(post.getName());
                        arrayList.add(model2);
                    }

                }

                rcAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, Profile.class));
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, SearchActivity.class));
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, SharePostActivity.class));
            }
        });

    }

    @Override
    public void onItemClick(int position) {

    }
}