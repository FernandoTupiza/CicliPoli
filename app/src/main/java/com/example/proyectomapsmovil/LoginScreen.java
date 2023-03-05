package com.example.proyectomapsmovil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class LoginScreen extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private ArrayList<UserCiclista> listUsers = new ArrayList<UserCiclista>();
    FirebaseDatabase database;
    DatabaseReference myRef;
    private Button btnIniciarsesion;
    FirebaseAuth firebaseAuth;
    private TextView goResetScreen;
    TextView nameUser;
    TextView lastNameUser;
    TextView emailUser;
    Ciclista userMainApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        firebaseAuth = FirebaseAuth.getInstance();
        RecyclerView recyclerView;
        email=(EditText) findViewById(R.id.editTextTextEmailAddress);
        password=(EditText) findViewById(R.id.editTextTextPassword);
        goResetScreen=findViewById(R.id.passwordReset);
        btnIniciarsesion= findViewById(R.id.button);
        final LoadingCustomer loadingCustomer = new LoadingCustomer(LoginScreen.this);

        btnIniciarsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().length()>0&&password.getText().toString().length()>0){
                    loadingCustomer.sartLoading();
                    firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                loadingCustomer.endLoading();
                                Params.UserFirebaseId=task.getResult().getUser().getUid();
//                                changeThePage();
                                roleUser(task.getResult().getUser().getUid());
                            }else{
                                Toast.makeText(getApplicationContext(), "Usuario/contraseña incorrectos", Toast.LENGTH_SHORT).show();
                                loadingCustomer.endLoading();
                            }
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(), "Ingrese sus credenciales", Toast.LENGTH_SHORT).show();
                }
            }
        });
        goResetScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginScreen.this,ResetPasword.class);
                startActivity(intent);
            }
        });
    }
    public void changeThePage(){
        Intent iniciar = new Intent(this, UsersScreen.class);
        startActivity(iniciar);
    }
    //redirigir al registro
    public void IrRegistro(View view){
        Intent irRegistro = new Intent(this, RegisterActivity.class);
        startActivity(irRegistro);
    }

    public void roleUser(String userid) {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users/"+userid);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
//                    listUsers = new ArrayList<UserCiclista>();
//                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Ciclista cil = dataSnapshot.getValue(Ciclista.class);
                        cil.setId(userid);
                    System.out.println("Cil::::"+cil.toString());
                        if(cil.getRole().equals("A")){
                            System.out.println("Administrador usuario");

                        }else if(cil.getRole().equals("C")){
                            System.out.println("Ciclista usuario");
                            changeThePage();
                        }
//                        ArrayList<LocationUser> lu = new ArrayList<>();
//                        UserCiclista uc = new UserCiclista(cil, lu);
//                        if (cil.getId().equals(Params.UserFirebaseId)) {
//                            userMainApp = cil;
//                            nameUser.setText(cil.getName());
//                            lastNameUser.setText(cil.getLastName());
//                            emailUser.setText(cil.getEmail());
//                        }
//                        else {
//                            listUsers.add(uc);
//                        }
//
//                        //  Toast.makeText(getApplicationContext(), "hay datos en users"+cil.getId(), Toast.LENGTH_SHORT).show();
//
                    }
//                    AdaptadorDatos adaptadorDatos = new AdaptadorDatos(listUsers, UsersScreen.this);
//                    recyclerView.setAdapter(adaptadorDatos);
//                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}