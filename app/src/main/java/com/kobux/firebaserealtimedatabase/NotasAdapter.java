package com.kobux.firebaserealtimedatabase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class NotasAdapter extends RecyclerView.Adapter<NotasAdapter.NotaViewHolder> {

    private final Context context;
    private final List<NotasModel> listaNotas;
    private final boolean modoEditable;
    private final OnTareaInteractionListener listener; // ← nuevo

    public NotasAdapter(Context context, List<NotasModel> listaNotas,
                        boolean modoEditable, OnTareaInteractionListener listener) {
        this.context = context;
        this.listaNotas = listaNotas;
        this.modoEditable = modoEditable;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_nota, parent, false);
        return new NotaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotaViewHolder holder, int position) {
        NotasModel nota = listaNotas.get(position);

        holder.textTitulo.setText(nota.getTitulo());
        holder.textDescripcion.setText(nota.getDescripcion());
        holder.textFecha.setText(nota.getFecha());

        if (nota.getTareas() != null && !nota.getTareas().isEmpty()) {
            holder.layoutExpandir.setVisibility(View.VISIBLE);

            List<Map.Entry<String, TareasModel>> entries = new ArrayList<>(nota.getTareas().entrySet());
            Collections.sort(entries, Comparator.comparing(Map.Entry::getKey));

            List<TareasModel> listaTareas = new ArrayList<>();
            for (Map.Entry<String, TareasModel> entry : entries) {
                listaTareas.add(entry.getValue());
            }

            TareasAdapter tareasAdapter = new TareasAdapter(
                    context,
                    listaTareas,
                    modoEditable,
                    nota.getId(),
                    listener
            );
            holder.recyclerTareas.setLayoutManager(new LinearLayoutManager(context));
            holder.recyclerTareas.setAdapter(tareasAdapter);

            holder.btnExpandirTareas.setOnClickListener(v -> {
                if (holder.layoutTareasContainer.getVisibility() == View.VISIBLE) {
                    holder.layoutTareasContainer.setVisibility(View.GONE);
                    holder.btnExpandirTareas.setText("Ver tareas");
                } else {
                    holder.layoutTareasContainer.setVisibility(View.VISIBLE);
                    holder.btnExpandirTareas.setText("Ocultar tareas");
                }
            });
        } else {
            holder.layoutExpandir.setVisibility(View.GONE);
            holder.layoutTareasContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listaNotas.size();
    }

    static class NotaViewHolder extends RecyclerView.ViewHolder {
        TextView textTitulo, textDescripcion, textFecha;
        RecyclerView recyclerTareas;
        Button btnExpandirTareas;
        LinearLayout layoutExpandir, layoutTareasContainer;

        public NotaViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitulo = itemView.findViewById(R.id.textTituloNota);
            textDescripcion = itemView.findViewById(R.id.textDescripcionNota);
            textFecha = itemView.findViewById(R.id.textFechaNota);
            recyclerTareas = itemView.findViewById(R.id.recyclerTareasNota);
            btnExpandirTareas = itemView.findViewById(R.id.btnExpandirTareas);
            layoutExpandir = itemView.findViewById(R.id.layoutExpandir);
            layoutTareasContainer = itemView.findViewById(R.id.layoutTareasContainer);
        }
    }
}
