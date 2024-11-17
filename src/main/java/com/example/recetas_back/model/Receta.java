package com.example.recetas_back.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "recetas")
public class Receta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_receta")
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "tipo_cocina", length = 50)
    private String tipoCocina;

    @Column(name = "ingredientes", nullable = false, columnDefinition = "CLOB")
    private String ingredientes;

    @Column(name = "pais_origen", length = 50)
    private String paisOrigen;

    @Column(name = "dificultad", length = 20)
    private String dificultad;

    @Column(name = "instrucciones", nullable = false, columnDefinition = "CLOB")
    private String instrucciones;

    @Column(name = "tiempo_coccion")
    private Integer tiempoCoccion;

    @Column(name = "imagen_url")
    private String imagenUrl;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "receta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Comentario> comentarios;

    // Constructor vacío para JPA
    public Receta() {
    }

    // Constructor con parámetros
    public Receta(String nombre, String tipoCocina, String ingredientes, String paisOrigen,
            String dificultad, String instrucciones, Integer tiempoCoccion,
            String imagenUrl, String videoUrl, Date fechaCreacion, Usuario usuario) {
        this.nombre = nombre;
        this.tipoCocina = tipoCocina;
        this.ingredientes = ingredientes;
        this.paisOrigen = paisOrigen;
        this.dificultad = dificultad;
        this.instrucciones = instrucciones;
        this.tiempoCoccion = tiempoCoccion;
        this.imagenUrl = imagenUrl;
        this.videoUrl = videoUrl;
        this.fechaCreacion = fechaCreacion;
        this.usuario = usuario;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoCocina() {
        return tipoCocina;
    }

    public void setTipoCocina(String tipoCocina) {
        this.tipoCocina = tipoCocina;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getPaisOrigen() {
        return paisOrigen;
    }

    public void setPaisOrigen(String paisOrigen) {
        this.paisOrigen = paisOrigen;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }

    public Integer getTiempoCoccion() {
        return tiempoCoccion;
    }

    public void setTiempoCoccion(Integer tiempoCoccion) {
        this.tiempoCoccion = tiempoCoccion;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }
}
