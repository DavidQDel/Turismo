package org.example.model;

import java.util.Date;

public class Reserva {
    private int id;
    private int idCliente;
    private int idPaquete;
    private Date fechaReserva;
    private String estado; // PENDIENTE, CONFIRMADA, CANCELADA

    //Constructor


    public Reserva(int id, int idCliente, int idPaquete, Date fechaReserva, String estado) {
        this.id = id;
        this.idCliente = idCliente;
        this.idPaquete = idPaquete;
        this.fechaReserva = fechaReserva;
        this.estado = estado;
    }

    //Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(int idPaquete) {
        this.idPaquete = idPaquete;
    }

    public Date getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(Date fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
