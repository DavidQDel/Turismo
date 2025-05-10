package org.example.model;

public class Cliente {
    private int id;
    private String nombreCompleto;
    private String email;
    private String telefono;

    // Constructor
    public Cliente() {}

    public Cliente(int id, String nombreCompleto, String email, String telefono) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.telefono = telefono;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}