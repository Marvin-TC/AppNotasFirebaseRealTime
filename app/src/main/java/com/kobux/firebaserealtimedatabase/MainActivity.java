package com.kobux.firebaserealtimedatabase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
public class MainActivity extends AppCompatActivity implements OnTareaInteractionListener {

    private FirebaseDatabase db;
    private DatabaseReference notasRef;
    private RecyclerView recyclerNotas;
    private FloatingActionButton fabNuevo;
    private List<NotasModel> listaNotas;
    private NotasAdapter adapter;
    private String userUid;
    private TextView textEmpty;
    private MaterialToolbar toolbar;
    private String URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userUid = getIntent().getStringExtra("USER_UID");
        if (userUid == null || userUid.isEmpty()) {
            Toast.makeText(this, "Error: UID de usuario no recibido", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = FirebaseDatabase.getInstance(URL);
        notasRef = db.getReference().child(userUid).child("notas");

        recyclerNotas = findViewById(R.id.recycler);
        fabNuevo = findViewById(R.id.fab);
        textEmpty = findViewById(R.id.textEmpty);

        listaNotas = new ArrayList<>();
        adapter = new NotasAdapter(this, listaNotas, true, this);
        recyclerNotas.setLayoutManager(new LinearLayoutManager(this));
        recyclerNotas.setAdapter(adapter);

        fabNuevo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FormularioNotas.class);
            intent.putExtra("USER_UID", userUid);
            startActivity(intent);
        });

        cargarNotasEnTiempoReal();
    }

    private void cargarNotasEnTiempoReal() {
        notasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaNotas.clear();
                for (DataSnapshot notaSnap : snapshot.getChildren()) {
                    NotasModel nota = notaSnap.getValue(NotasModel.class);
                    if (nota != null) listaNotas.add( nota);
                }
                adapter.notifyDataSetChanged();
                if (listaNotas.isEmpty()) {
                    textEmpty.setVisibility(View.VISIBLE);
                    recyclerNotas.setVisibility(View.GONE);
                } else {
                    textEmpty.setVisibility(View.GONE);
                    recyclerNotas.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error al cargar notas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private DatabaseReference getUserRef() {
        return db.getReference().child(userUid);
    }

    @Override
    public void onTareaChecked(String notaId, String tareaId, boolean isChecked) {
        DatabaseReference ref = getUserRef()
                .child("notas")
                .child(notaId)
                .child("tareas")
                .child(tareaId)
                .child("completado");

        ref.setValue(isChecked)
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Tarea actualizada correctamente"))
                .addOnFailureListener(e -> Log.e("Firebase", "Error al actualizar tarea", e));
    }

    @Override
    public void onTareaEliminada(String notaId, String tareaId) {
        DatabaseReference ref = getUserRef()
                .child("notas")
                .child(notaId)
                .child("tareas")
                .child(tareaId);

        ref.removeValue()
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Tarea eliminada correctamente"))
                .addOnFailureListener(e -> Log.e("Firebase", "Error al eliminar tarea", e));
    }
}