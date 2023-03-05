package com.example.proyectomapsmovil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminScreen extends AppCompatActivity {
    private ArrayList<UserCiclista> listUsers = new ArrayList<UserCiclista>();
    private RecyclerView recyclerView;
    FirebaseDatabase database;
    DatabaseReference myRef;
    TextView nameUser;
    TextView lastNameUser;
    TextView emailUser;
    Ciclista userMainApp;
    Button newAdd;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_screen);
        recyclerView = findViewById(R.id.listaUsuarios2);
        nameUser = findViewById(R.id.itemName2);
        lastNameUser = findViewById(R.id.itemLastName2);
        emailUser = findViewById(R.id.itemEmail2);
        newAdd = findViewById(R.id.addAdmin);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getUsersDataBase();

        newAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeThePage();
            }
        });
    }

    public void getUsersDataBase() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    listUsers = new ArrayList<UserCiclista>();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Ciclista cil = postSnapshot.getValue(Ciclista.class);
                        ArrayList<LocationUser> lu = new ArrayList<>();
                        UserCiclista uc = new UserCiclista(cil, lu);
                        if (cil.getId().equals(Params.UserFirebaseId)) {
                            userMainApp = cil;
                            nameUser.setText(cil.getName());
                            lastNameUser.setText(cil.getLastName());
                            emailUser.setText(cil.getEmail());

                        } else {
                            listUsers.add(uc);
                        }

                        //  Toast.makeText(getApplicationContext(), "hay datos en users"+cil.getId(), Toast.LENGTH_SHORT).show();
                    }
                    System.out.println("Lista Usuarios"+listUsers);
                    AdaptadorDatos adaptadorDatos = new AdaptadorDatos(listUsers, AdminScreen.this);
                    recyclerView.setAdapter(adaptadorDatos);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void changeThePage(){
        Intent iniciar = new Intent(this, RegisterAdminActivity.class);
        startActivity(iniciar);
    }

}