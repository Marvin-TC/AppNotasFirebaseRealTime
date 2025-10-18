package com.kobux.firebaserealtimedatabase;

public class TareasModel {
    private String id;

    private String titulo;
    private Long fecha;
    private boolean completado;

    public TareasModel() {
    }

    public TareasModel(String id, String titulo, Long fecha, boolean completado) {
        this.id = id;
        this.titulo = titulo;
        this.fecha = fecha;
        this.completado = completado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Long getFecha() {
        return fecha;
    }

    public void setFecha(Long fecha) {
        this.fecha = fecha;
    }

    public boolean isCompletado() {
        return completado;
    }

    public void setCompletado(boolean completado) {
        this.completado = completado;
    }
}
