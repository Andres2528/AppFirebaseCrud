package com.example.appfirebasecrud;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.appfirebasecrud.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        setTheme(R.style.Theme_AppFirebaseCrud);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        botonBuscar();
        botonModificar();
        botonRegistrar();
        botonEliminar();
        listarEstudiantes();
    }

    private void botonBuscar(){

        binding.btnbus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(binding.txtid.getText().toString().trim().isEmpty()){

                    ocultarTeclado();
                    Toast.makeText(MainActivity.this, "Digite el ID del estudiante a buscar", Toast.LENGTH_SHORT).show();

                }else{

                    int id = Integer.parseInt(binding.txtid.getText().toString());
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Estudiante.class.getSimpleName());

                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String aux = Integer.toString(id);
                            boolean res = false;
                            for (DataSnapshot x : snapshot.getChildren()){
                                if(aux.equalsIgnoreCase(x.child("id").getValue().toString())){
                                    res = true;
                                    ocultarTeclado();
                                    binding.txtnom.setText(x.child("nombres").getValue().toString());
                                    binding.txtape.setText(x.child("apellidos").getValue().toString());
                                    binding.txtfechanaci.setText(x.child("fechaNacimiento").getValue().toString());
                                    binding.txtdireccion.setText(x.child("direccion").getValue().toString());
                                    binding.txtnumtelefono.setText(x.child("telefono").getValue().toString());
                                    break;
                                }
                            }

                            if(res == false){
                                ocultarTeclado();
                                Toast.makeText(MainActivity.this, "ID ("+ aux +") No Encontrado", Toast.LENGTH_SHORT).show();
                                limpiarCampos();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }

    private void botonModificar(){

        binding.btnmod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(binding.txtid.getText().toString().trim().isEmpty() || binding.txtnom.toString().trim().isEmpty()
                        || binding.txtape.getText().toString().trim().isEmpty() || binding.txtfechanaci.getText().toString().trim().isEmpty()
                        || binding.txtdireccion.getText().toString().trim().isEmpty() || binding.txtnumtelefono.getText().toString().trim().isEmpty()){
                    ocultarTeclado();
                    Toast.makeText(MainActivity.this,"Complete Los Campos Faltantes Para Actualizar",Toast.LENGTH_SHORT).show();
                }else{
                    int id = Integer.parseInt(binding.txtid.getText().toString());
                    String nom = binding.txtnom.getText().toString();
                    String ape = binding.txtape.getText().toString();
                    String fecha = binding.txtfechanaci.getText().toString();
                    String dir = binding.txtdireccion.getText().toString();
                    String tel = binding.txtnumtelefono.getText().toString();

                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Estudiante.class.getSimpleName());

                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String aux = Integer.toString(id);
                            boolean res = false;
                            for(DataSnapshot x : snapshot.getChildren()){
                                if(x.child("id").getValue().toString().equalsIgnoreCase(aux)){
                                    res = true;
                                    ocultarTeclado();
                                    x.getRef().child("nombres").setValue(nom);
                                    x.getRef().child("apellidos").setValue(ape);
                                    x.getRef().child("fechaNacimiento").setValue(fecha);
                                    x.getRef().child("direccion").setValue(dir);
                                    x.getRef().child("telefono").setValue(tel);
                                    limpiarCampos();
                                    listarEstudiantes();
                                    break;
                                }
                            }

                            if(res == false){
                                ocultarTeclado();
                                Toast.makeText(MainActivity.this, "ID no encontrado, Imposible modificar", Toast.LENGTH_SHORT).show();
                                limpiarCampos();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }

    private void botonRegistrar(){

        binding.btnreg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(binding.txtid.getText().toString().trim().isEmpty() || binding.txtnom.toString().trim().isEmpty()
                        || binding.txtape.getText().toString().trim().isEmpty() || binding.txtfechanaci.getText().toString().trim().isEmpty()
                        || binding.txtdireccion.getText().toString().trim().isEmpty() || binding.txtnumtelefono.getText().toString().trim().isEmpty()){
                    ocultarTeclado();
                    Toast.makeText(MainActivity.this,"Complete Los Campos Faltantes",Toast.LENGTH_SHORT).show();
                }else{
                    int id = Integer.parseInt(binding.txtid.getText().toString());
                    String nom = binding.txtnom.getText().toString();
                    String ape = binding.txtape.getText().toString();
                    String fecha = binding.txtfechanaci.getText().toString();
                    String dir = binding.txtdireccion.getText().toString();
                    String tel = binding.txtnumtelefono.getText().toString();

                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Estudiante.class.getSimpleName());

                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String aux = Integer.toString(id);
                            boolean res = false;
                            for(DataSnapshot x : snapshot.getChildren()){
                                if(x.child("id").getValue().toString().equalsIgnoreCase(aux)){
                                    res = true;
                                    ocultarTeclado();
                                    Toast.makeText(MainActivity.this, "Error. El ID ("+ aux +") Ya Existe!!", Toast.LENGTH_SHORT).show();
                                    limpiarCampos();
                                    break;
                                }
                            }

                            if(res == false){
                                Estudiante est = new Estudiante(id,nom,ape,fecha,dir,tel);
                                dbref.push().setValue(est);
                                ocultarTeclado();
                                Toast.makeText(MainActivity.this, "Estudiante Registrado Correctamente!!", Toast.LENGTH_SHORT).show();
                                limpiarCampos();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }

    private void listarEstudiantes(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbref = db.getReference(Estudiante.class.getSimpleName());

        ArrayList <Estudiante> lisest = new ArrayList <Estudiante> ();
        ArrayAdapter <Estudiante> ada = new ArrayAdapter <Estudiante> (MainActivity.this, android.R.layout.simple_list_item_1, lisest);
        binding.lvDatos.setAdapter(ada);

        dbref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Estudiante est = snapshot.getValue(Estudiante.class);
                lisest.add(est);
                ada.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ada.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.lvDatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Estudiante est = lisest.get(i);
                AlertDialog.Builder a = new AlertDialog.Builder(MainActivity.this);
                a.setCancelable(true);
                a.setTitle("Estudiante Seleccionado");

                String msg = "Id : " + est.getId() + "\n\n";
                msg += "Nombres : " + est.getNombres() + "\n\n";
                msg += "Apellidos : " + est.getApellidos() + "\n\n";
                msg += "Fecha Nacimiento : " + est.getFechaNacimiento() + "\n\n";
                msg += "Direccion : " + est.getDireccion() + "\n\n";
                msg += "Telefono : " + est.getTelefono() + "\n\n";

                a.setMessage(msg);
                a.show();
            }
        });
    }

    private void botonEliminar(){

        binding.btneli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(binding.txtid.getText().toString().trim().isEmpty()){

                    ocultarTeclado();
                    Toast.makeText(MainActivity.this, "Digite el ID del estudiante a eliminar", Toast.LENGTH_SHORT).show();

                }else{

                    int id = Integer.parseInt(binding.txtid.getText().toString());
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Estudiante.class.getSimpleName());

                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String aux = Integer.toString(id);
                            final boolean[] res = {false};
                            for (DataSnapshot x : snapshot.getChildren()){
                                if(aux.equalsIgnoreCase(x.child("id").getValue().toString())){

                                    AlertDialog.Builder a = new AlertDialog.Builder(MainActivity.this);
                                    a.setCancelable(false);
                                    a.setTitle("Pregunta");
                                    a.setMessage("¿Esta Segur@ de querer eliminar el registro?");

                                    a.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    a.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            res[0] = true;
                                            ocultarTeclado();
                                            x.getRef().removeValue();
                                            listarEstudiantes();
                                        }
                                    });

                                    a.show();
                                    break;
                                }
                            }

                            if(res[0] == false){
                                ocultarTeclado();
                                Toast.makeText(MainActivity.this, "ID ("+ aux +") No Encontrado, Imposible eliminar.", Toast.LENGTH_SHORT).show();
                                limpiarCampos();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });

    }

    private void limpiarCampos(){
        binding.txtid.setText("");
        binding.txtnom.setText("");
        binding.txtape.setText("");
        binding.txtfechanaci.setText("");
        binding.txtdireccion.setText("");
        binding.txtnumtelefono.setText("");
    }

    private void ocultarTeclado(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    } // Cierra el método ocultarTeclado.

}