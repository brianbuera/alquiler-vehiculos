package com.buera.alquiler_vehiculos.model.strategies;

public interface PrecioStrategy {
    String getNombre();
    double calcularPrecio(double precioBase, Long diasReserva);
}
