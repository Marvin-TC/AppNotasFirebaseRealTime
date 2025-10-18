package com.kobux.firebaserealtimedatabase;

public interface OnTareaInteractionListener {
    void onTareaChecked(String notaId, String tareaId, boolean isChecked);
    void onTareaEliminada(String notaId, String tareaId);
}