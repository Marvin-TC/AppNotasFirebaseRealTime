package com.kobux.firebaserealtimedatabase;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FormularioNotas extends AppCompatActivity {

    private FirebaseDatabase db;
    private DatabaseReference notasRef;
    EditText editTexCampoTarea;
    EditText editTextDescripcion;
    TextView textFechaHora;
    EditText editTitulo;
    RecyclerView recyclerView;
    List<TareasModel> tareas;
    Button btnregistrarNota;
    Button btnRegistrarTarea;
    private String keyNota = null;
    private String userUid;
    private String URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_formulario_notas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTexCampoTarea = findViewById(R.id.editTarea);
        editTextDescripcion = findViewById(R.id.editTextDescripcion);
        textFechaHora = findViewById(R.id.textClock);
        editTitulo = findViewById(R.id.editTitulo);
        recyclerView = findViewById(R.id.lista_tareas);
        btnregistrarNota = findViewById(R.id.registrarNota);
        btnRegistrarTarea = findViewById(R.id.agregar);

        tareas = new ArrayList<>();

        userUid = getIntent().getStringExtra("USER_UID");

        db = FirebaseDatabase.getInstance(URL);
        notasRef = db.getReference().child(userUid).child("notas");

        keyNota = notasRef.push().getKey();

        TareasAdapter adapter = new TareasAdapter(FormularioNotas.this, tareas, false, null, new com.kobux.firebaserealtimedatabase.OnTareaInteractionListener() {
            @Override
            public void onTareaChecked(String notaId, String tareaId, boolean isChecked) {

            }

            @Override
            public void onTareaEliminada(String notaId, String tareaId) {

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(FormularioNotas.this));
        recyclerView.setAdapter(adapter);

        btnRegistrarTarea.setOnClickListener(v -> {
            String tareaTexto = editTexCampoTarea.getText().toString().trim();
            if (!tareaTexto.isEmpty()) {
                String keyTarea = notasRef.child(keyNota).child("tareas").push().getKey();
                TareasModel tareaNueva = new TareasModel();
                tareaNueva.setId(keyTarea);
                tareaNueva.setTitulo(tareaTexto);
                tareaNueva.setCompletado(false);
                tareaNueva.setFecha(System.currentTimeMillis());
                tareas.add(tareaNueva);
                adapter.notifyItemInserted(tareas.size() - 1);
                recyclerView.scrollToPosition(tareas.size() - 1);
                editTexCampoTarea.setText("");
            } else {
                Toast.makeText(FormularioNotas.this, "Ingrese un título", Toast.LENGTH_SHORT).show();
            }
        });

        btnregistrarNota.setOnClickListener(v -> {
            String titulo = editTitulo.getText().toString().trim();
            String descripcion = editTextDescripcion.getText().toString().trim();

            if (titulo.isEmpty()) {
                Toast.makeText(FormularioNotas.this, "Ingrese un título para la nota", Toast.LENGTH_SHORT).show();
                editTitulo.requestFocus();
                return;
            }

            String fechaTexto = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    .format(new Date());

            Map<String, Object> tareasMap = new HashMap<>();
            for (TareasModel tarea : tareas) {
                Map<String, Object> t = new HashMap<>();
                t.put("id", tarea.getId());
                t.put("titulo", tarea.getTitulo());
                t.put("completado", tarea.isCompletado());
                t.put("fecha", tarea.getFecha());
                tareasMap.put(tarea.getId(), t);
            }

            Map<String, Object> notaData = new HashMap<>();
            notaData.put("id", keyNota);
            notaData.put("titulo", titulo);
            notaData.put("descripcion", descripcion);
            notaData.put("fecha", fechaTexto);
            notaData.put("tareas", tareasMap);

            notasRef.child(keyNota).setValue(notaData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(FormularioNotas.this, "Nota guardada con tareas", Toast.LENGTH_SHORT).show();
                        editTitulo.setText("");
                        editTextDescripcion.setText("");
                        tareas.clear();
                        recyclerView.getAdapter().notifyDataSetChanged();
                        keyNota = notasRef.push().getKey();
                        onBackPressed();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(FormularioNotas.this, "Error al guardar", Toast.LENGTH_SHORT).show());
        });
    }
}