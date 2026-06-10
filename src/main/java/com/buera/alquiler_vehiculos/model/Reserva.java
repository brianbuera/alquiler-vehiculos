package com.buera.alquiler_vehiculos.model;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Reserva {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    @ManyToOne
    @JoinColumn(name = "vehiculo_id")
    private Vehiculo vehiculo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private boolean telepaseIlimitado;
    private double cargoAdicionalTelepase;
    private double precioBase;
    private double precioTotal;

    public Reserva() {
    }

    public Reserva(
            Cliente cliente,
            Vehiculo vehiculo,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            boolean telepaseIlimitado,
            double precioBase,
            double precioTotal
    ) {
        this.cliente = cliente;
        this.vehiculo = vehiculo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.telepaseIlimitado = telepaseIlimitado;
        this.cargoAdicionalTelepase = 0;
        this.precioBase = precioBase;
        this.precioTotal = precioTotal;
  

    }



    public Long getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    //getters y setters para precioTotal
    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    //getter y setter para telepaseIlimitado
    public boolean isTelepaseIlimitado() {
        return telepaseIlimitado;
    }

    public void setTelepaseIlimitado(boolean telepaseIlimitado) {
        this.telepaseIlimitado = telepaseIlimitado;
    }
    
    public double getCargoAdicionalTelepase() {
        return cargoAdicionalTelepase;
    }

    public void setCargoAdicionalTelepase(double cargoAdicionalTelepase) {
        this.cargoAdicionalTelepase = cargoAdicionalTelepase;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }   



}