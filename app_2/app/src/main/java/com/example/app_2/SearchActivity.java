package com.example.app_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.app_2.data.Result;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends AppCompatActivity implements RecyclerViewInterface{

    private RecyclerView recyclerView;
    private RCAdapter rcAdapter;

    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private HashMap users;

    private ArrayList<RCModel> arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        mDatabase=FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.recyclerView_search);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        arrayList=new ArrayList<>();
        rcAdapter=new RCAdapter(this,arrayList,this);
        recyclerView.setAdapter(rcAdapter);

        users = new HashMap();

        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot s : snapshot.getChildren()){
                    Users user = s.getValue(Users.class);
                    users.put(s.getKey().toString(),user);
                }

                users.forEach((k,v) -> insertRCModel((String) k, (Users) v,arrayList));

                rcAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void insertRCModel (String key, Users u,ArrayList<RCModel> arrayList ){
        RCModel rcModel = new RCModel(u.getName()+" "+u.getSurname(), u.getDataImage());
        rcModel.setUid(key);
        arrayList.add(rcModel);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(SearchActivity.this,ViewProfileActivity.class);
        RCModel rcModel1 = arrayList.get(position);
        intent.putExtra("transfer",rcModel1.uid.toString());
        startActivity(intent);
    }
}