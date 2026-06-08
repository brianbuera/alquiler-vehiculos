package com.buera.alquiler_vehiculos.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public class ReservaRequest {
    @NotNull
    private Long clienteId;
    @NotNull
    private Long vehiculoId;
    @NotNull
    private LocalDate fechaInicio;
    @NotNull
    private LocalDate fechaFin;

    public ReservaRequest() {
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getVehiculoId() {
        return vehiculoId;
    }

    public void setVehiculoId(Long vehiculoId) {
        this.vehiculoId = vehiculoId;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
}
