package com.example.recetas_back.model;

import java.util.Date;

public class ComentarioDTO {
    private Long id;
    private String contenido;
    private Date fechaComentario;
    private String nombreUsuario;
    private String apellidoUsuario;
    private String nombreReceta;

    // Constructor
    public ComentarioDTO(Long id, String contenido, Date fechaComentario, String nombreUsuario, String apellidoUsuario,
            String nombreReceta) {
        this.id = id;
        this.contenido = contenido;
        this.fechaComentario = fechaComentario;
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.nombreReceta = nombreReceta;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Date getFechaComentario() {
        return fechaComentario;
    }

    public void setFechaComentario(Date fechaComentario) {
        this.fechaComentario = fechaComentario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getApellidoUsuario() {
        return apellidoUsuario;
    }

    public void setApellidoUsuario(String apellidoUsuario) {
        this.apellidoUsuario = apellidoUsuario;
    }

    public String getNombreReceta() {
        return nombreReceta;
    }

    public void setNombreReceta(String nombreReceta) {
        this.nombreReceta = nombreReceta;
    }
}
