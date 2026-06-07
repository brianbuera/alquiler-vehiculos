package com.buera.alquiler_vehiculos.model;
import com.buera.alquiler_vehiculos.model.enums.TipoCliente;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String email;
    @Enumerated(EnumType.STRING)
    private TipoCliente tipoCliente;

    public Cliente() {
    }
    public Cliente( String nombre, String email, TipoCliente tipoCliente) {
        this.nombre = nombre;
        this.email = email;
        this.tipoCliente = tipoCliente;
    }

    public Cliente(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
        this.tipoCliente = TipoCliente.ESTANDAR;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
    }


    



}
