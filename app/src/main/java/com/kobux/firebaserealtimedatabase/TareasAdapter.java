package com.kobux.firebaserealtimedatabase;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TareasAdapter extends RecyclerView.Adapter<TareasAdapter.TareaViewHolder> {

    private final Context context;
    private final List<TareasModel> listaTareas;
    private final boolean modoEditable;
    private final String notaId;
    private final OnTareaInteractionListener listener;

    public TareasAdapter(Context context, List<TareasModel> listaTareas,
                         boolean modoEditable, String notaId,
                         OnTareaInteractionListener listener) {
        this.context = context;
        this.listaTareas = listaTareas;
        this.modoEditable = modoEditable;
        this.notaId = notaId;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tarea, parent, false);
        return new TareaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {
        TareasModel tarea = listaTareas.get(position);

        holder.textTitulo.setText(tarea.getTitulo());
        if (tarea.getFecha() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            holder.textFecha.setText(sdf.format(new Date(tarea.getFecha())));
        } else {
            holder.textFecha.setText("Sin fecha");
        }

        holder.checkCompletado.setOnCheckedChangeListener(null);
        holder.checkCompletado.setChecked(tarea.isCompletado());

        if (tarea.isCompletado()) {
            holder.textTitulo.setPaintFlags(holder.textTitulo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.textTitulo.setTextColor(Color.GRAY);
        } else {
            holder.textTitulo.setPaintFlags(holder.textTitulo.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.textTitulo.setTextColor(Color.BLACK);
        }

        if (modoEditable) {
            holder.checkCompletado.setEnabled(true);
            holder.btnEliminar.setVisibility(View.VISIBLE);

            holder.checkCompletado.setOnCheckedChangeListener((buttonView, isChecked) -> {
                tarea.setCompletado(isChecked);
                if (listener != null)
                    listener.onTareaChecked(notaId, tarea.getId(), isChecked);
            });

            holder.btnEliminar.setOnClickListener(v -> {
                if (listener != null)
                    listener.onTareaEliminada(notaId, tarea.getId());
                listaTareas.remove(position);
                notifyItemRemoved(position);
            });

        } else {
            holder.checkCompletado.setEnabled(false);
            holder.btnEliminar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listaTareas.size();
    }

    static class TareaViewHolder extends RecyclerView.ViewHolder {
        TextView textTitulo, textFecha;
        CheckBox checkCompletado;
        ImageButton btnEliminar;

        public TareaViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitulo = itemView.findViewById(R.id.textTitulo);
            textFecha = itemView.findViewById(R.id.textFecha);
            checkCompletado = itemView.findViewById(R.id.checkCompletado);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}