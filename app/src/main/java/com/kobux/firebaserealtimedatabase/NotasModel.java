package com.kobux.firebaserealtimedatabase;

import java.util.List;
import java.util.Map;

public class NotasModel {
    private String id;
    private String titulo;
    private String fecha;
    private String descripcion;
    private Map<String, TareasModel> tareas; // cambio aquí

    public NotasModel() {
    }

    public NotasModel(String id, String titulo, String fecha, String descripcion, Map<String, TareasModel> tareas) {
        this.id = id;
        this.titulo = titulo;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.tareas = tareas;
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Map<String, TareasModel> getTareas() {
        return tareas;
    }

    public void setTareas(Map<String, TareasModel> tareas) {
        this.tareas = tareas;
    }
}
