package com.example.proyectomapsmovil;

import static com.example.proyectomapsmovil.Params.useerApp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdaptadorDatosAdmin extends RecyclerView.Adapter<AdaptadorDatosAdmin.ViewHolderDafos>{
    private ArrayList<UserCiclista> datos;
    private Context myCotext;

    FirebaseAuth firebaseAuth;
    DatabaseReference database;

    public AdaptadorDatosAdmin(ArrayList<UserCiclista> datos, Context myCotext) {
        this.datos = datos;
        this.myCotext=myCotext;
        firebaseAuth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance().getReference();
    }
    @NonNull
    @Override
    public AdaptadorDatosAdmin.ViewHolderDafos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delete,null,false);
        return new ViewHolderDafos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorDatosAdmin.ViewHolderDafos holder, int position) {
        holder.asigarDatos(datos.get(position));

    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public class ViewHolderDafos extends RecyclerView.ViewHolder {
        TextView name ;
        TextView lastName ;
        TextView email ;
        TextView role ;
        Button changePage;
        public ViewHolderDafos(@NonNull View itemView) {
            super(itemView);
            name =itemView.findViewById(R.id.itemName);
            lastName=itemView.findViewById(R.id.itemLastName);
            email =itemView.findViewById(R.id.itemEmail);
            role = itemView.findViewById(R.id.itemRole);
            changePage=itemView.findViewById(R.id.changeNext);

        }

        public void asigarDatos(UserCiclista userCiclista) {
            name.setText(userCiclista.getCiclista().getName());
            lastName.setText(userCiclista.getCiclista().getLastName());
            email.setText(userCiclista.getCiclista().getEmail());
            role.setText(userCiclista.getCiclista().getRole());
            changePage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Params.useerApp=userCiclista.getCiclista();
//                    Intent intent = new Intent(myCotext,LocationScreen.class);
//                    intent.putExtra("userId",userCiclista.getCiclista().getId());
//                    myCotext.startActivity(intent);
                    //setDeleteUserData();
                    database.child("users").child(userCiclista.getCiclista().getId()).removeValue();

                }
            });
        }
    }

    /*public void setDeleteUserData(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Eliminación de usuarios");
        builder.setMessage("¿Está seguro de que que quiere eliminar los datos?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(null, "Eliminando datos . .", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(null, "Operación cancelada", Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                    }
                }).show();

    }*/

}
