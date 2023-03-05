package com.example.proyectomapsmovil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterAdminActivity extends AppCompatActivity {
    Button botonRegistro;
    EditText name;
    EditText lastNme;
    EditText email;
    EditText password;
    FirebaseAuth firebaseAuth;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        botonRegistro = findViewById(R.id.buttonRegistro);
        firebaseAuth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance().getReference();
        name=findViewById(R.id.editTextTextPersonName);
        lastNme= findViewById(R.id.editTextTextPersonLastName);
        email=findViewById(R.id.editTextTextEmailAddress2);
        password=findViewById(R.id.editTextTextPassword2);
        final LoadingCustomer loadingCustomer = new LoadingCustomer(RegisterAdminActivity.this);

        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(name.getText().toString().length()>0&&lastNme.getText().toString().length()>0&& email.getText().toString().length()>0&password.getText().toString().length()>0){
                    loadingCustomer.sartLoading();
                    firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            loadingCustomer.endLoading();
                            if(task.isSuccessful()){

                                Ciclista ciclista = new Ciclista(name.getText().toString(),lastNme.getText().toString(),email.getText().toString(),task.getResult().getUser().getUid());
                                Toast.makeText(RegisterAdminActivity.this, "Usuario Registrado", Toast.LENGTH_LONG).show();
                                ciclista.setRole("A");
                                saveAppUser(ciclista);
//                                Params.UserFirebaseId=ciclista.getId();
                                changeThePage();
                            }else{
                                Toast.makeText(RegisterAdminActivity.this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(), "Ingrese los campos requeridos", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
    public void changeThePage(){
        Intent iniciar = new Intent(this, AdminScreen.class);
        startActivity(iniciar);
    }

    public void IrLoginAct(View view){
        Intent irLog = new Intent(this, AdminScreen.class);
        startActivity(irLog);
    }

    public void IrTabs(View view){
        Intent iniciarTabs = new Intent(this, UsersScreen.class);
        startActivity(iniciarTabs);
    }
    public void saveAppUser(Ciclista ciclista){
        database.child("users").child(ciclista.getId()).setValue(ciclista);
    }
}